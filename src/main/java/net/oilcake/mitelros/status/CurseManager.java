package net.oilcake.mitelros.status;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Curse;
import net.minecraft.EntityPlayer;
import net.minecraft.EntityWitch;
import net.minecraft.ServerPlayer;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.C2SLearnCurseEffect;
import net.oilcake.mitelros.network.packets.S2CUpdateCurses;
import net.oilcake.mitelros.util.CurseExtend;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

public class CurseManager {
    private final EntityPlayer player;

    private final List<Curse> activeCurses = new ArrayList<>();

    private static final UUID CLIENT_CURSE_SOURCE = new UUID(0L, 0L);

    public CurseManager(EntityPlayer player) {
        this.player = player;
    }

    public List<Curse> getActiveCurses() {
        List<Curse> list = this.activeCurses;
        EntityPlayer player = this.player;

        if (player.worldObj != null && player.worldObj.isRemote) {
            if (!player.is_cursed) {
                list.clear();
            } else if (player.curse_id > 0 && list.isEmpty()) {
                Curse curse = this.createLocalCurse(player.curse_id, player.curse_effect_known);
                if (curse != null) {
                    list.add(curse);
                }
            }
            if (player.curse_id > 0) {
                boolean curseFound = false;
                for (Curse curse : list) {
                    if (curse.id == player.curse_id) {
                        curse.effect_known = player.curse_effect_known;
                        curseFound = true;
                        break;
                    }
                }
                if (!curseFound) {
                    Curse curse = this.createLocalCurse(player.curse_id, player.curse_effect_known);
                    if (curse != null) {
                        list.add(curse);
                    }
                }
            }
        }
        return list;
    }

    public void setActiveCurses(List<Curse> curses) {
        List<Curse> list = this.activeCurses;
        EntityPlayer player = this.player;

        list.clear();
        HashSet<Integer> added = new HashSet<>();
        int maxCurseCount = player.worldObj != null && player.worldObj.isRemote ? curses.size() : CurseExtend.getMaxCurseCount();
        for (Curse curse : curses) {
            if (list.size() >= maxCurseCount) {
                break;
            }
            if (added.add(curse.id)) {
                list.add(curse);
            }
        }
        if (list.isEmpty()) {
            player.is_cursed = false;
            player.curse_id = 0;
            player.curse_effect_known = false;
        } else {
            player.is_cursed = true;
            player.curse_id = list.get(0).id;
            player.curse_effect_known = list.get(0).effect_known;
        }
    }

    public boolean hasCurse(Curse curse, boolean learnEffectIfSo) {
        for (Curse c : this.getActiveCurses()) {
            if (c.id == curse.id) {
                if (learnEffectIfSo && !c.effect_known) {
                    learnCurseEffect(c);
                }
                return true;
            }
        }
        return false;
    }

    public void removeCurse(Curse curse) {
        List<Curse> remaining = new ArrayList<>(this.activeCurses);
        remaining.removeIf(c -> c.id == curse.id);
        this.setActiveCurses(remaining);
    }

    public void clearCurses() {
        this.setActiveCurses(List.of());
    }

    private Curse getActiveCurse(int curseId) {
        for (Curse activeCurse : this.getActiveCurses()) {
            if (activeCurse.id == curseId) {
                return activeCurse;
            }
        }
        return null;
    }

    public void learnCurseEffect(Curse curse) {
        Curse activeCurse = this.getActiveCurse(curse.id);
        if (activeCurse != null) {
            curse = activeCurse;
        }
        if (!curse.effect_known) {
            curse.effect_known = true;
            curse.effect_has_already_been_learned = true;
            if (this.player.worldObj.isRemote) {
                ITFNetwork.sendToServer(new C2SLearnCurseEffect(curse.id));
            } else if (this.player instanceof ServerPlayer serverPlayer) {
                ITFNetwork.sendToClient(serverPlayer, new S2CUpdateCurses(this.getActiveCurses()));
            }
        }
    }

    public Curse createLocalCurse(int curseId, boolean effectKnown) {
        if (curseId <= 0 || curseId >= Curse.cursesList.length || Curse.cursesList[curseId] == null) {
            return null;
        }
        Curse curse = new Curse(
                this.player.getEntityName(),
                CLIENT_CURSE_SOURCE,
                Curse.cursesList[curseId],
                0L,
                true,
                effectKnown
        );
        curse.effect_has_already_been_learned = effectKnown;
        return curse;
    }

    public boolean onLearnCurseEffect() {
        List<Curse> activeCurses = this.getActiveCurses();
        if (activeCurses.isEmpty()) {
            return false;
        }
        for (Curse c : activeCurses) {
            if (!c.effect_known) {
                learnCurseEffect(c);
            }
        }
        return true;
    }

    @Environment(EnvType.SERVER)
    public void serverTick() {
        if (ITFConfig.TagRejection.isEnable()) {
            List<Curse> playerCurses = this.getPersistedCurses();
            int targetCurseCount = ITFConfig.TagRejection.getIntegerValue();
            if (playerCurses.size() < targetCurseCount) {
                EntityWitch temp = new EntityWitch(this.player.worldObj);
                Set<Integer> added = new HashSet<>();
                for (Curse c : playerCurses) {
                    added.add(c.id);
                    this.learnPersistedCurseEffect(c.id);
                }
                int existingCurseCount = playerCurses.size();
                int cursesToAdd = targetCurseCount - existingCurseCount;
                Random rand = new Random(this.getCurseSeed(existingCurseCount));
                for (int i = 0; i < cursesToAdd; i++) {
                    Curse randomCurse = this.getRandomAvailableCurse(rand, added);
                    if (randomCurse == null) {
                        break;
                    }
                    Curse persistedCurse = this.createPersistedCurse(temp, randomCurse, existingCurseCount + i);
                    this.player.worldObj.getWorldInfo().getCurses().add(persistedCurse);
                    playerCurses.add(persistedCurse);
                    added.add(randomCurse.id);
                }
                ((ITFEntityPlayer) this).itf$SetActiveCurses(this.getRealizedCurses());
            }
        }
    }

    private List<Curse> getPersistedCurses() {
        List<Curse> playerCurses = new ArrayList<>();
        for (Curse curse : (List<Curse>) this.player.worldObj.getWorldInfo().getCurses()) {
            if (curse.cursed_player_username.equals(this.player.getEntityName())) {
                playerCurses.add(curse);
            }
        }
        return playerCurses;
    }


    @Unique
    private List<Curse> getRealizedCurses() {
        List<Curse> playerCurses = new ArrayList<>();
        for (Curse curse : this.getPersistedCurses()) {
            if (curse.has_been_realized) {
                playerCurses.add(curse);
            }
        }
        return playerCurses;
    }

    @Unique
    private Curse getRandomAvailableCurse(Random rand, Set<Integer> excludedCurseIds) {
        List<Curse> availableCurses = new ArrayList<>();
        for (Curse curse : Curse.cursesList) {
            if (curse != null && !excludedCurseIds.contains(curse.id)) {
                availableCurses.add(curse);
            }
        }
        if (availableCurses.isEmpty()) {
            return null;
        }
        return availableCurses.get(rand.nextInt(availableCurses.size()));
    }

    private Curse createPersistedCurse(EntityWitch cursingWitch, Curse curse, int timeOffset) {
        Curse persistedCurse = new Curse(
                this.player.getEntityName(),
                cursingWitch.getUniqueID(),
                curse,
                this.player.worldObj.getTotalWorldTime() + timeOffset,
                false,
                true
        );
        persistedCurse.effect_has_already_been_learned = true;
        return persistedCurse;
    }

    private void learnPersistedCurseEffect(int curseId) {
        for (Curse curse : (List<Curse>) this.player.worldObj.getWorldInfo().getCurses()) {
            if (curse.cursed_player_username.equals(this.player.getEntityName()) && curse.id == curseId) {
                curse.effect_known = true;
                curse.effect_has_already_been_learned = true;
            }
        }
    }

    private long getCurseSeed(int existingCurseCount) {
        long seed = this.player.worldObj.getSeed();
        seed ^= ((long) this.player.getEntityName().hashCode()) << 32;
        seed ^= this.player.worldObj.getWorldInfo().getWorldCreationTime();
        seed ^= this.player.worldObj.getTotalWorldTime();
        seed ^= ((long) existingCurseCount) * 0x9E3779B97F4A7C15L;
        return seed;
    }
}
