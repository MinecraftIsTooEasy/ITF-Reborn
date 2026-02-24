package net.oilcake.mitelros.api;

import net.minecraft.Item;
import net.oilcake.mitelros.feat.FoodWater;
import net.oilcake.mitelros.registry.property.ITFProperties;

public interface ITFApi {
    static int getItemWater(Item item) {
        return FoodWater.getWater(item);
    }

    static float getItemWaterChance(Item item) {
        return FoodWater.getWaterChance(item);
    }

    static void setItemWater(Item item, int water) {
        ITFProperties.WATER.register(item, water);
    }

    static void setItemWaterChance(Item item, float chance) {
        ITFProperties.WATER_CHANCE.register(item, chance);
    }
}
