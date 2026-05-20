package net.oilcake.mitelros.mixins.entity.player;

import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.container.ContainerEnchantReserver;
import net.oilcake.mitelros.container.ContainerMinePocket;
import net.oilcake.mitelros.inventory.EnchantReserverInventory;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.mixin.interfaces.ITFPlayer;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.S2COpenWindow;
import net.oilcake.mitelros.network.packets.S2CUpdateITFStatus;
import net.oilcake.mitelros.status.EnchantmentManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityPlayer implements ICrafting, ITFPlayer {
    public ServerPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Shadow
    protected abstract void incrementWindowID();

    @Unique
    private int last_water;

    @Shadow
    private int currentWindowId;

    @Shadow
    private int protein;

    @Shadow
    private int phytonutrients;

    @Shadow
    public abstract boolean isMalnourished();

    @Inject(method = "onDeath", at = @At("RETURN"))
    public void onDeath(DamageSource par1DamageSource, CallbackInfo callbackInfo) {
        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            EnchantmentManager.vanish(this.inventory);
        }
    }

    @Inject(method = "onUpdate", at = @At("RETURN"))
    public void onUpdate(CallbackInfo callbackInfo) {
        this.sendPacket((new Packet85SimpleSignal(EnumSignal.malnourished)).setInteger(((this.protein <= 800000) ? 1 : 0) | ((this.phytonutrients <= 800000) ? 4 : 0) | (this.getCurrent_insulin_resistance_lvl()) << 3 | getInsulinResistance() << 8));
    }

    @Inject(method = "onUpdateEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/FoodStats;getHunger()F"))
    private void inject(CallbackInfo ci) {
        if (ITFConfig.TagRejection.isEnable()) {
            List<Curse> playerCurses = this.itf$getPersistedCurses();
            int targetCurseCount = ITFConfig.TagRejection.getIntegerValue();
            if (playerCurses.size() < targetCurseCount) {
                EntityWitch temp = new EntityWitch(this.worldObj);
                Set<Integer> added = new HashSet<>();
                for (Curse c : playerCurses) {
                    added.add(c.id);
                    this.itf$LearnPersistedCurseEffect(c.id);
                }
                int existingCurseCount = playerCurses.size();
                int cursesToAdd = targetCurseCount - existingCurseCount;
                Random rand = new Random(this.itf$getCurseSeed(existingCurseCount));
                for (int i = 0; i < cursesToAdd; i++) {
                    Curse randomCurse = this.itf$getRandomAvailableCurse(rand, added);
                    if (randomCurse == null) {
                        break;
                    }
                    Curse persistedCurse = this.itf$CreatePersistedCurse(temp, randomCurse, existingCurseCount + i);
                    this.worldObj.getWorldInfo().getCurses().add(persistedCurse);
                    playerCurses.add(persistedCurse);
                    added.add(randomCurse.id);
                }
                ((ITFEntityPlayer) this).itf$SetActiveCurses(this.itf$getRealizedCurses());
            }
        }
        int water = this.itf$GetWater();
        if (water != this.last_water) {
            ITFNetwork.sendToClient(this.getAsEntityPlayerMP(), new S2CUpdateITFStatus(water));
            this.last_water = water;
        }
    }

    @Unique
    private List<Curse> itf$getPersistedCurses() {
        List<Curse> playerCurses = new ArrayList<>();
        for (Curse curse : (List<Curse>) this.worldObj.getWorldInfo().getCurses()) {
            if (curse.cursed_player_username.equals(this.getEntityName())) {
                playerCurses.add(curse);
            }
        }
        return playerCurses;
    }

    @Unique
    private List<Curse> itf$getRealizedCurses() {
        List<Curse> playerCurses = new ArrayList<>();
        for (Curse curse : this.itf$getPersistedCurses()) {
            if (curse.has_been_realized) {
                playerCurses.add(curse);
            }
        }
        return playerCurses;
    }

    @Unique
    private Curse itf$getRandomAvailableCurse(Random rand, Set<Integer> excludedCurseIds) {
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

    @Unique
    private Curse itf$CreatePersistedCurse(EntityWitch cursingWitch, Curse curse, int timeOffset) {
        Curse persistedCurse = new Curse(
                this.getEntityName(),
                cursingWitch.getUniqueID(),
                curse,
                this.worldObj.getTotalWorldTime() + timeOffset,
                false,
                true
        );
        persistedCurse.effect_has_already_been_learned = true;
        return persistedCurse;
    }

    @Unique
    private void itf$LearnPersistedCurseEffect(int curseId) {
        for (Curse curse : (List<Curse>) this.worldObj.getWorldInfo().getCurses()) {
            if (curse.cursed_player_username.equals(this.getEntityName()) && curse.id == curseId) {
                curse.effect_known = true;
                curse.effect_has_already_been_learned = true;
            }
        }
    }

    @Unique
    private long itf$getCurseSeed(int existingCurseCount) {
        long seed = this.worldObj.getSeed();
        seed ^= ((long) this.getEntityName().hashCode()) << 32;
        seed ^= this.worldObj.getWorldInfo().getWorldCreationTime();
        seed ^= this.worldObj.getTotalWorldTime();
        seed ^= ((long) existingCurseCount) * 0x9E3779B97F4A7C15L;
        return seed;
    }

    @Override
    public void itf$DisplayGUIEnchantReserver(int x, int y, int z, EnchantReserverInventory slots) {
        incrementWindowID();
        TileEntity tile_entity = this.worldObj.getBlockTileEntity(x, y, z);
        Network.sendToClient(this.getAsEntityPlayerMP(), (new S2COpenWindow(this.currentWindowId, S2COpenWindow.EnumInventoryType.EnchantReserver, tile_entity.getCustomInvName(), 9, tile_entity.hasCustomName())).setCoords(x, y, z));
        this.openContainer = new ContainerEnchantReserver(slots, this, x, y, z);
        this.openContainer.windowId = this.currentWindowId;
        this.sendContainerAndContentsToPlayer(this.openContainer, ((ContainerEnchantReserver) this.openContainer).getInventory());
        this.openContainer.addCraftingToCrafters(this);
    }

    @Override
    public void itf$DisplayGuiMinePocket(IInventory minePocketInventory) {
        this.incrementWindowID();
        Network.sendToClient(this.getAsEntityPlayerMP(), new S2COpenWindow(this.currentWindowId, S2COpenWindow.EnumInventoryType.MinePocket, minePocketInventory.getCustomNameOrUnlocalized(), 5, true));
        this.openContainer = new ContainerMinePocket(this, minePocketInventory);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.addCraftingToCrafters(this);
    }

    @Inject(method = "isMalnourished", at = @At("HEAD"), cancellable = true)
    private void inject_1(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.protein <= 800000 || this.phytonutrients <= 800000);
    }

    @Inject(method = "isDoubleMalnourished", at = @At("HEAD"), cancellable = true)
    private void inject_2(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.protein <= 800000 && this.phytonutrients <= 800000);
    }

    @Override
    public int itf$MalnourishedLevel() {
        int min = Math.min(this.protein, this.phytonutrients);
        if (min < 160000) return 3;
        if (min < 320000) return 2;
        if (min < 800000) return 1;
        return 0;
    }

    public boolean itf$IsMalnourishedFinal() {
        if (this.protein == 0)
            return true;
        return (this.phytonutrients == 0);
    }

    @Unique
    private int getCurrent_insulin_resistance_lvl() {
        if (this.insulin_resistance_level == null)
            return 0;
        return this.insulin_resistance_level.ordinal() + 1;
    }

    @Inject(method = "getWetnessAndMalnourishmentHungerMultiplier", at = @At("RETURN"), cancellable = true)
    private void malnourishment(CallbackInfoReturnable<Float> cir) {
        float original = cir.getReturnValue();
        float oldFactor = this.isMalnourished() ? 0.5F : 0.0F;
        original -= oldFactor;
        float newFactor;
        if (this.itf$IsMalnourishedFinal()) {
            newFactor = 31.0F;
        } else {
            int level = this.itf$MalnourishedLevel();
            newFactor = level == 3 ? 3.0F : (level == 2 ? 1.0F : (level == 1 ? 0.5F : 0.0F));
        }
        cir.setReturnValue(original + newFactor);
    }

    @Inject(method = "travelInsideDimension", at = @At("HEAD"))
    private void onTravelInsideDimension(double x, double y, double z, CallbackInfo ci) {
        this.last_water = -1;
    }

    @Inject(method = "travelToDimension", at = @At(value = "FIELD", target = "Lnet/minecraft/ServerPlayer;last_nutrition:I", opcode = Opcodes.PUTFIELD))
    private void onTravelToDimension(int par1, CallbackInfo ci) {
        this.last_water = -1;
    }

    @Inject(method = "clonePlayer", at = @At(value = "FIELD", target = "Lnet/minecraft/ServerPlayer;last_nutrition:I", opcode = Opcodes.PUTFIELD))
    private void onClonePlayer(EntityPlayer par1EntityPlayer, boolean par2, CallbackInfo ci) {
        this.last_water = -1;
    }
}
