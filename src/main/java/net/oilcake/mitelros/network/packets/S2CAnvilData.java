package net.oilcake.mitelros.network.packets;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.ContainerRepair;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.feat.anvil.AnvilStatus;
import net.oilcake.mitelros.mixin.interfaces.ITFContainerRepair;
import net.oilcake.mitelros.network.ITFNetwork;

public class S2CAnvilData implements Packet {
    private final int windowId;
    private final boolean active;
    private final int xpDifference;
    private final int anvilStatus;

    public S2CAnvilData(PacketByteBuf packetByteBuf) {
        this.windowId = packetByteBuf.readInt();
        this.active = packetByteBuf.readBoolean();
        this.xpDifference = packetByteBuf.readInt();
        this.anvilStatus = packetByteBuf.readInt();
    }

    public S2CAnvilData(int windowId, boolean active, int xpDifference, AnvilStatus anvilStatus) {
        this.windowId = windowId;
        this.active = active;
        this.xpDifference = xpDifference;
        this.anvilStatus = anvilStatus.ordinal();
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        packetByteBuf.writeInt(this.windowId);
        packetByteBuf.writeBoolean(this.active);
        packetByteBuf.writeInt(this.xpDifference);
        packetByteBuf.writeInt(this.anvilStatus);
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        AnvilStatus[] statuses = AnvilStatus.values();
        if (this.anvilStatus < 0 || this.anvilStatus >= statuses.length) return;
        if (entityPlayer.openContainer instanceof ContainerRepair container && container.windowId == this.windowId) {
            ((ITFContainerRepair) container).itf$setAnvilData(this.active, this.xpDifference, statuses[this.anvilStatus]);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return ITFNetwork.AnvilData;
    }
}
