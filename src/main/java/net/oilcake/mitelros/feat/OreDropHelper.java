package net.oilcake.mitelros.feat;

import net.minecraft.*;
import net.oilcake.mitelros.registry.ITFRegistryImpl;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.registry.property.ITFProperties;

import java.util.Map;
import java.util.Random;

public class OreDropHelper {
    public static boolean isVulnerableToExplosion(BlockOre blockOre) {
        return blockOre == Blocks.blockAzurite;
    }

    public static int getRawPieceItemID(BlockOre blockOre, int metadata) {
        if (blockOre == Block.oreGold) {
            int type = metadata & 0b10;
            return type == 0b10 ? Items.pieceGoldNether.itemID : Items.pieceGold.itemID;
        }
        return ITFProperties.ORE_PIECE_MAP.getOrDefault(blockOre);
    }

    public static int getMeltPieceItemID(BlockOre blockOre) {
        return ITFProperties.ORE_MELTING_MAP.getOrDefault(blockOre);
    }

    public static int getMeltIngotItemID(BlockOre blockOre) {
        return ITFProperties.ORE_MELTING_SILK_TOUCH_MAP.getOrDefault(blockOre);
    }

    public static boolean canAbsorb(BlockOre blockOre) {
        return calcAbsorbXP(blockOre, 1.0F) != 0;
//        return this == Block.oreDiamond || this == Block.oreEmerald || this == Blocks.blockAzurite || this == Block.oreNetherQuartz || this == Block.oreLapis;
    }

    public static int calcAbsorbXP(BlockOre blockOre, float chance) {
        if (blockOre == Blocks.blockAzurite)
            return (int) (((3 + new Random().nextInt(5)) * 1.1F * ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Items.shardAzurite))) * chance);
        if (blockOre == Block.oreLapis)
            return (int) (((3 + new Random().nextInt(3)) * 1.2F * ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.dyePowder, 1, 4))) * chance);

        Map<Block, ItemStack> map = ITFRegistryImpl.ABSORBING_MAP;
        if (map.containsKey(blockOre)) {
            ItemStack itemStack = map.get(blockOre);
            return (int) (1.1F * ItemRock.getExperienceValueWhenSacrificed(itemStack) * chance);
        }
        return 0;
    }

}
