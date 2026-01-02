package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.client.gui.GuiEnchantReserver;
import net.oilcake.mitelros.network.ITFNetwork;

public class S2CEnchantReserverInfo implements Packet {
    private final int exp;

    public S2CEnchantReserverInfo(PacketByteBuf packetByteBuf) {
        this(packetByteBuf.readInt());
    }

    public S2CEnchantReserverInfo(int exp) {
        this.exp = exp;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.exp);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        GuiScreen openingGUI = Minecraft.getMinecraft().currentScreen;
        if (openingGUI instanceof GuiEnchantReserver) {
            ((GuiEnchantReserver) openingGUI).setEXP(this.exp);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.EnchantmentReserverInfo;
    }
}
