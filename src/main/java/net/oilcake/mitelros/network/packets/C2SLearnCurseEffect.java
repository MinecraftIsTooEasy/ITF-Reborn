package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.Curse;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.network.ITFNetwork;

import java.util.ArrayList;
import java.util.List;

public class C2SLearnCurseEffect implements Packet {
    private final int curseId;

    public C2SLearnCurseEffect(PacketByteBuf packetByteBuf) {
        this(packetByteBuf.readInt());
    }

    public C2SLearnCurseEffect(int curseId) {
        this.curseId = curseId;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.curseId);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        if (!(entityPlayer instanceof ServerPlayer player) || !(player.worldObj instanceof WorldServer worldServer)) {
            return;
        }

        List<Curse> activeCurses = new ArrayList<>();
        for (Curse curse : (List<Curse>) worldServer.getWorldInfo().getCurses()) {
            if (!curse.cursed_player_username.equals(player.getEntityName()) || !curse.has_been_realized) {
                continue;
            }
            if (curse.id == this.curseId) {
                curse.effect_known = true;
                curse.effect_has_already_been_learned = true;
            }
            activeCurses.add(curse);
        }

        ITFEntityPlayer itfPlayer = (ITFEntityPlayer) player;
        itfPlayer.itf$SetActiveCurses(activeCurses);
        ITFNetwork.sendToClient(player, new S2CUpdateCurses(itfPlayer.itf$GetActiveCurses()));
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.LearnCurseEffect;
    }
}
