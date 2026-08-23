package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiEnchantment;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.mixin.interfaces.ITFGuiEnchantment;
import net.oilcake.mitelros.network.ITFNetwork;

public class S2CEnchantmentSeed implements Packet {

    private final long seed;

    public S2CEnchantmentSeed(PacketByteBuf packetByteBuf) {
        this.seed = packetByteBuf.readLong();
    }

    public S2CEnchantmentSeed(long seed) {
        this.seed = seed;
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeLong(this.seed);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof GuiEnchantment guiEnchantment) {
            ((ITFGuiEnchantment) guiEnchantment).itf$SetEnchantmentSeed(this.seed);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.EnchantmentSeed;
    }
}
