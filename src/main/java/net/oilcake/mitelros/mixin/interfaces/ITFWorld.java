package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.Entity;
import net.minecraft.Explosion;
import net.minecraft.World;
import net.oilcake.mitelros.feat.EnumSeason;

public interface ITFWorld {
    Explosion itf$ExplosionC(Entity exploder, double posX, double posY, double posZ, float explosion_size_vs_blocks, float explosion_size_vs_living_entities, boolean b);

    static EnumSeason getSeason(World world) {
        return EnumSeason.getForCode((world.getDayOfWorld() % 128) / 32);
    }

    static float getSeasonGrowModifier(World world) {
        return (float) Math.sin(0.0490873852123 * (world.getDayOfWorld() - 16));
    }

    static float getSeasonGrowFactor(World world) {
        return Math.max(0.0F, 1.0F + getSeasonGrowModifier(world));
    }
}
