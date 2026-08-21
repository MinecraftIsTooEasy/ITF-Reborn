package net.oilcake.mitelros.util;

import net.minecraft.BiomeGenBase;
import net.minecraft.Block;
import net.minecraft.IBlockAccess;
import net.minecraft.Material;
import net.oilcake.mitelros.material.Materials;

public final class WaterHelper {

    public static final int PURE_WATER_BLEND_LEVELS = 4;

    public static boolean isWater(Block block) {
        return block == Block.waterMoving || block == Block.waterStill;
    }

    public static boolean isPureWater(BiomeGenBase biome) {
        return biome == BiomeGenBase.river || biome == BiomeGenBase.desertRiver;
    }

    public static boolean isPureWater(IBlockAccess blockAccess, int x, int z) {
        return isPureWater(blockAccess.getBiomeGenForCoords(x, z));
    }

    public static Material getWaterMaterial(IBlockAccess blockAccess, int x, int z) {
        return isPureWater(blockAccess, x, z) ? Materials.pure_water : Material.water;
    }

    public static int getPureWaterBlendLevel(IBlockAccess blockAccess, int x, int z) {
        int pureWaterBiomes = 0;
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
                if (isPureWater(blockAccess, x + offsetX, z + offsetZ)) {
                    pureWaterBiomes++;
                }
            }
        }
        return Math.round(pureWaterBiomes * (float) PURE_WATER_BLEND_LEVELS / 9.0F);
    }
}
