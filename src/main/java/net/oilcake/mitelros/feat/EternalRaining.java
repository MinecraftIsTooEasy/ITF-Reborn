package net.oilcake.mitelros.feat;

import net.minecraft.World;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.mixin.interfaces.ITFWorld;

public class EternalRaining {
    public static int modifyRain(World world, int original) {
        boolean tag = ITFConfig.TagEternalRaining.getBooleanValue();
        int duration_static = 6000 * (tag ? 6 : 1);
        int duration_random = original * (tag ? 2 : 1);
        int duration = duration_random + duration_static;
        duration = (int) (duration * getRainDurationModify(ITFWorld.getSeason(world)));
        return duration - 6000;
    }

    public static float getRainDurationModify(EnumSeason season) {
        return switch (season) {
            case SPRING -> 1.0F;
            case SUMMER -> 2.25F;
            case AUTUMN -> 0.75F;
            case WINTER -> 0.5F;
        };
    }
}
