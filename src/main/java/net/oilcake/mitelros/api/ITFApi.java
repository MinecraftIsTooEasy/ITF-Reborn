package net.oilcake.mitelros.api;

import net.minecraft.Item;
import net.oilcake.mitelros.registry.property.ITFProperties;

public interface ITFApi {
    static int getItemWater(Item item) {
        return ITFProperties.WATER.getOrDefault(item);
    }

    static float getItemWaterChance(Item item) {
        Float v = ITFProperties.WATER_CHANCE.get(item);
        if (v != null) return v;
        return 0.0F;
    }

    static void setItemWater(Item item, int water) {
        ITFProperties.WATER.register(item, water);
    }

    static void setItemWaterChance(Item item, float chance) {
        ITFProperties.WATER_CHANCE.register(item, chance);
    }
}
