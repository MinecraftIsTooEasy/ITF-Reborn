package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.EntityPlayer;
import net.oilcake.mitelros.status.DrunkManager;
import net.oilcake.mitelros.status.FeastManager;
import net.oilcake.mitelros.status.HuntManager;
import net.oilcake.mitelros.status.MiscManager;

public interface ITFEntityPlayer {
    MiscManager itf_GetMiscManager();

    int itf$GetWater();

    int itf$AddWater(int water);

    FeastManager itf$GetFeastManager();

    void itf$DecreaseWaterServerSide(float hungerWater);

    boolean itf$IsMalnourishedFinal();

    int itf$MalnourishedLevel();

    DrunkManager itf$GetDrunkManager();

    HuntManager itf$GetHuntManager();

    static ITFEntityPlayer cast(EntityPlayer player) {
        return player;
    }
}
