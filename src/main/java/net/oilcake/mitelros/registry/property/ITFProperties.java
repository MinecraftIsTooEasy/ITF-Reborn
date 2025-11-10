package net.oilcake.mitelros.registry.property;

import moddedmite.rustedironcore.property.FloatProperty;
import moddedmite.rustedironcore.property.IntegerProperty;
import net.minecraft.Block;
import net.minecraft.Item;

public class ITFProperties {
    public static final IntegerProperty<Item> WATER = IntegerProperty.of("Water", 0);
    public static final FloatProperty<Item> WATER_CHANCE = FloatProperty.of("Water Chance", 0);
    public static final IntegerProperty<Block> ORE_PIECE_MAP = IntegerProperty.of("Ore Piece", 0);
    public static final IntegerProperty<Block> ORE_MELTING_MAP = IntegerProperty.of("Ore Melting", 0);
}
