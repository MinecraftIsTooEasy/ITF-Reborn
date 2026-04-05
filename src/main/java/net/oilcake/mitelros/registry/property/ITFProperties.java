package net.oilcake.mitelros.registry.property;

import moddedmite.rustedironcore.property.FloatProperty;
import moddedmite.rustedironcore.property.IntegerProperty;
import net.minecraft.Block;
import net.minecraft.Item;
import net.minecraft.Material;

public class ITFProperties {
    public static final IntegerProperty<Item> WATER = IntegerProperty.of("Water", 0);
    public static final FloatProperty<Item> WATER_CHANCE = FloatProperty.of("Water Chance", 0);
    public static final IntegerProperty<Block> ORE_PIECE_MAP = IntegerProperty.of("Ore Piece", 0);
    public static final IntegerProperty<Block> ORE_MELTING_MAP = IntegerProperty.of("Ore Melting", 0);
    public static final IntegerProperty<Material> MATERIAL_WATER = IntegerProperty.of("Material Water", 0);
    public static final IntegerProperty<Block> ORE_MELTING_SILK_TOUCH_MAP = IntegerProperty.of("Ore Melting Silk Touch", 0);
    public static final FloatProperty<Material> CRAFTING_SPEED = FloatProperty.of("Workbench Crafting Speed", 0);
    public static final IntegerProperty<Material> BOW_PULL_TICKS = IntegerProperty.of("Bow Pull Ticks", 0);
    public static final FloatProperty<Material> BOW_DAMAGE_MODIFIER = FloatProperty.of("Bow Damage Modifier", 0.75F);
    public static final FloatProperty<Item> SMELTING_SPEED_MODIFIER = FloatProperty.of("Smelting Speed Modifier", 1.0F);
}
