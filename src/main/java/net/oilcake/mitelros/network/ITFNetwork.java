package net.oilcake.mitelros.network;

import moddedmite.rustedironcore.network.Network;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.minecraft.ServerPlayer;
import net.oilcake.mitelros.ITFStart;
import net.oilcake.mitelros.network.packets.*;
import net.xiaoyu233.fml.FishModLoader;

public class ITFNetwork {
    public static final ResourceLocation BossInfo = ITFStart.identifier("BossInfo");
    public static final ResourceLocation SetGlowing = ITFStart.identifier("SetGlowing");
    public static final ResourceLocation DecreaseWater = ITFStart.identifier("DecreaseWater");
    public static final ResourceLocation LearnCurseEffect = ITFStart.identifier("LearnCurseEffect");
    public static final ResourceLocation EnchantmentSeed = ITFStart.identifier("EnchantmentSeed");
    public static final ResourceLocation EnchantmentReserverInfo = ITFStart.identifier("EnchantmentReserverInfo");
    public static final ResourceLocation UpdateITFStatus = ITFStart.identifier("UpdateITFStatus");
    public static final ResourceLocation UpdateCurses = ITFStart.identifier("UpdateCurses");
    public static final ResourceLocation OpenWindow = ITFStart.identifier("OpenWindow");
    public static final ResourceLocation AnvilData = ITFStart.identifier("AnvilData");

    public static void sendToClient(ServerPlayer player, Packet packet) {
        Network.sendToClient(player, packet);
    }

    public static void sendToServer(Packet packet) {
        Network.sendToServer(packet);
    }

    public static void init() {
        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
        PacketReader.registerClientPacketReader(BossInfo, S2CBossInfo::new);
        PacketReader.registerClientPacketReader(SetGlowing, S2CSetGlowing::new);
        PacketReader.registerClientPacketReader(EnchantmentSeed, S2CEnchantmentSeed::new);
        PacketReader.registerClientPacketReader(EnchantmentReserverInfo, S2CEnchantReserverInfo::new);
        PacketReader.registerClientPacketReader(UpdateITFStatus, S2CUpdateITFStatus::new);
        PacketReader.registerClientPacketReader(UpdateCurses, S2CUpdateCurses::new);
        PacketReader.registerClientPacketReader(OpenWindow, S2COpenWindow::new);
        PacketReader.registerClientPacketReader(AnvilData, S2CAnvilData::new);
    }

    private static void initServer() {
        PacketReader.registerServerPacketReader(DecreaseWater, C2SDecreaseWater::new);
        PacketReader.registerServerPacketReader(LearnCurseEffect, C2SLearnCurseEffect::new);
    }
}
