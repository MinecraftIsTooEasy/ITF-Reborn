package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.ContainerEnchantment;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiEnchantment;
import net.minecraft.GuiScreen;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.mixin.interfaces.ITFGuiEnchantment;
import net.oilcake.mitelros.network.ITFNetwork;

import java.util.Arrays;

public class S2CEnchantmentPrediction implements Packet {
    private static final int INFO_LENGTH = 12;

    private final int windowId;
    private final int[] info;

    public S2CEnchantmentPrediction(PacketByteBuf packetByteBuf) {
        this.windowId = packetByteBuf.readInt();
        this.info = new int[INFO_LENGTH];
        for (int index = 0; index < INFO_LENGTH; index++) {
            this.info[index] = packetByteBuf.readInt();
        }
    }

    public S2CEnchantmentPrediction(int windowId, int[] info) {
        this.windowId = windowId;
        this.info = new int[INFO_LENGTH];
        Arrays.fill(this.info, -1);
        if (info != null) {
            System.arraycopy(info, 0, this.info, 0, Math.min(info.length, INFO_LENGTH));
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.windowId);
        for (int value : this.info) {
            packetByteBuf.writeInt(value);
        }
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (!(currentScreen instanceof GuiEnchantment)
                || !(entityPlayer.openContainer instanceof ContainerEnchantment container)
                || container.windowId != this.windowId
                || !(currentScreen instanceof ITFGuiEnchantment enchantmentGui)) {
            return;
        }
        enchantmentGui.itf$SetEnchantmentInfo(this.info);
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.EnchantmentPrediction;
    }
}
