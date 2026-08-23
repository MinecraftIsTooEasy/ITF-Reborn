package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.Curse;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.network.ITFNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class S2CUpdateCurses implements Packet {
    private static final UUID CLIENT_CURSE_SOURCE = new UUID(0L, 0L);
    private final int[] curseIds;
    private final boolean[] effectKnown;

    public S2CUpdateCurses(PacketByteBuf packetByteBuf) {
        int count = packetByteBuf.readInt();
        this.curseIds = new int[count];
        this.effectKnown = new boolean[count];
        for (int i = 0; i < count; i++) {
            this.curseIds[i] = packetByteBuf.readInt();
            this.effectKnown[i] = packetByteBuf.readBoolean();
        }
    }

    public S2CUpdateCurses(List<Curse> curses) {
        this.curseIds = new int[curses.size()];
        this.effectKnown = new boolean[curses.size()];
        for (int i = 0; i < curses.size(); i++) {
            Curse curse = curses.get(i);
            this.curseIds[i] = curse.id;
            this.effectKnown[i] = curse.effect_known;
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.curseIds.length);
        for (int i = 0; i < this.curseIds.length; i++) {
            packetByteBuf.writeInt(this.curseIds[i]);
            packetByteBuf.writeBoolean(this.effectKnown[i]);
        }
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        List<Curse> curses = new ArrayList<>();
        for (int i = 0; i < this.curseIds.length; i++) {
            int curseId = this.curseIds[i];
            if (curseId <= 0 || curseId >= Curse.cursesList.length) {
                continue;
            }
            Curse templateCurse = Curse.cursesList[curseId];
            if (templateCurse != null) {
                Curse curse = new Curse(
                        entityPlayer.getEntityName(),
                        CLIENT_CURSE_SOURCE,
                        templateCurse,
                        0L,
                        true,
                        this.effectKnown[i]
                );
                curse.effect_has_already_been_learned = this.effectKnown[i];
                curses.add(curse);
            }
        }
        ((ITFEntityPlayer) entityPlayer).itf$SetActiveCurses(curses);
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.UpdateCurses;
    }
}
