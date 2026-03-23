package net.oilcake.mitelros.registry;

import net.minecraft.*;
import net.oilcake.mitelros.api.ITFRegistry;
import net.oilcake.mitelros.registry.property.ITFProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ITFRegistryImpl implements ITFRegistry {
    @Override
    public void registerItemWater(Item item, int water) {
        ITFProperties.WATER.register(item, water);
    }

    @Override
    public void registerItemWaterChance(Item item, float chance) {
        ITFProperties.WATER_CHANCE.register(item, chance);
    }

    public static final List<Class<? extends Entity>> meatAnimals = new ArrayList<>();

    @Override
    public void registerMeatAnimal(Class<? extends Entity> clazz) {
        meatAnimals.add(clazz);
    }

    @Override
    public void registerOrePiece(Block blockOre, int dropItemID) {
        ITFProperties.ORE_PIECE_MAP.register(blockOre, dropItemID);
    }

    @Override
    public void registerOreMelting(Block blockOre, int dropItemID) {
        ITFProperties.ORE_MELTING_MAP.register(blockOre, dropItemID);
    }

    @Override
    public void registerOreMeltingSilkTouch(Block blockOre, int dropItemID) {
        ITFProperties.ORE_MELTING_SILK_TOUCH_MAP.register(blockOre, dropItemID);
    }

    public static final Map<Block, ItemStack> ABSORBING_MAP = new HashMap<>();

    @Override
    public void registerOreAbsorbing(Block blockOre, ItemStack output) {
        ABSORBING_MAP.put(blockOre, output);
    }

    @Override
    public void registerMaterialWater(Material material, int water) {
        ITFProperties.MATERIAL_WATER.register(material, water);
    }

    @Override
    public void registerMaterialCraftingSpeedModifier(Material material, float speed) {
        ITFProperties.CRAFTING_SPEED.register(material, speed);
    }

    @Override
    public void registerMaterialBowPullTicks(Material material, int ticks) {
        ITFProperties.BOW_PULL_TICKS.register(material, ticks);
    }

    @Override
    public void registerMaterialBowDamageModifier(Material material, float modifier) {
        ITFProperties.BOW_DAMAGE_MODIFIER.register(material, modifier);
    }
}
