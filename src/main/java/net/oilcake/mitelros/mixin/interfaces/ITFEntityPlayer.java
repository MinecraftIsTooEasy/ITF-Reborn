package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.Curse;
import net.minecraft.EntityPlayer;
import net.oilcake.mitelros.extension.PlayerExtension;

import java.util.List;

public interface ITFEntityPlayer {
    PlayerExtension itf$getPlayerExtension();

    int itf$GetWater();

    int itf$AddWater(int water);

    void itf$DecreaseWaterServerSide(float hungerWater);

    boolean itf$IsMalnourishedFinal();

    int itf$MalnourishedLevel();

    static ITFEntityPlayer cast(EntityPlayer player) {
        return player;
    }

    List<Curse> itf$GetActiveCurses();

    void itf$SetActiveCurses(List<Curse> curses);

    void itf$ClearCurses();

    void itf$LearnCurseEffect(Curse curse);
}
