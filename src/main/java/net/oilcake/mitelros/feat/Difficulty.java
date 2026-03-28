package net.oilcake.mitelros.feat;

import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.oilcake.mitelros.config.ConfigBooleanChallenge;
import net.oilcake.mitelros.config.ITFConfig;

public class Difficulty {
    public static int ultimateDifficulty;

    public static int calculateCurrentDifficulty() {
        int difficulty = 0;
        for (ConfigBase<?> configBase : ITFConfig.challenge) {
            if (configBase instanceof ConfigBooleanChallenge challenge && challenge.getBooleanValue()) {
                difficulty += challenge.getLevel();
            }
            if (configBase instanceof ConfigInteger configInteger) {
                difficulty += configInteger.getIntegerValue();
            }
        }
        return difficulty;
    }

    public static int calculateUltimateDifficulty() {
        int difficulty = 0;
        for (ConfigBase<?> configBase : ITFConfig.spite) {
            if (configBase instanceof ConfigBooleanChallenge configBooleanChallenge) {
                difficulty += configBooleanChallenge.getLevel();
            }
            if (configBase instanceof ConfigInteger configInteger) {
                difficulty += configInteger.getMaxIntegerValue();
            }
        }
        for (ConfigBase<?> configBase : ITFConfig.enemy) {
            if (configBase instanceof ConfigBooleanChallenge configBooleanChallenge) {
                difficulty += configBooleanChallenge.getLevel();
            }
            if (configBase instanceof ConfigInteger configInteger) {
                difficulty += configInteger.getMaxIntegerValue();
            }
        }
        return difficulty;
    }
}
