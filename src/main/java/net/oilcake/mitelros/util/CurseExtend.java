package net.oilcake.mitelros.util;

import net.minecraft.Curse;
import net.oilcake.mitelros.config.ITFConfig;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import java.util.ArrayList;
import java.util.List;

public class CurseExtend extends Curse {
    public static List<Curse> activeCurses = new ArrayList<>();

    private static int getNextCurseID() {
        return IdUtil.getNextCurseId();
    }
    public CurseExtend(int id, String key) {
        super(id, key);
    }

//    public static final Curse fear_of_darkness = new Curse(getNextCurseID(), "fearOfDarkness");
//
//    public static final Curse fear_of_light = new Curse(getNextCurseID(), "fearOfLight");

    public static int getMaxCurseCount() {
        int configValue = ITFConfig.TagRejection.getIntegerValue();
        return configValue > 0 ? configValue : 1;
    }
}
