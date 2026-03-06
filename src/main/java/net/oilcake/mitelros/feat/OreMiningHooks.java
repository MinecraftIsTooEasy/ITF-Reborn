package net.oilcake.mitelros.feat;

import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.*;
import net.oilcake.mitelros.enchantment.Enchantments;
import net.oilcake.mitelros.mixins.block.IMixinBlock;

public class OreMiningHooks {
    public static void setIdMeta(BlockBreakInfo info, BlockOre ore, LocalIntRef id_dropped, LocalIntRef quantity_dropped) {
        if (info.wasExploded()) {
            if (OreDropHelper.isVulnerableToExplosion(ore)) {
                id_dropped.set(0);
            } else {
                int pieceID = OreDropHelper.getRawPieceItemID(ore, info.getMetadata());
                if (pieceID != 0) {
                    id_dropped.set(pieceID);
                    quantity_dropped.set(1 + info.world.rand.nextInt(2));
                }
            }
            return;
        }
        boolean hasAbsorb = EnchantmentHelper.hasEnchantment(info.responsible_item_stack, Enchantments.enchantmentAbsorb);
        if (hasAbsorb && OreDropHelper.canAbsorb(ore)) {
            id_dropped.set(0);
            if (ore == Block.oreEmerald) {
                info.getResponsiblePlayer().triggerAchievement(AchievementList.emeralds);
            } else if (ore == Block.oreDiamond) {
                info.getResponsiblePlayer().triggerAchievement(AchievementList.diamonds);
            }
        } else {
            int pieceID = OreDropHelper.getRawPieceItemID(ore, info.getMetadata());
            if (pieceID != 0) {
                id_dropped.set(pieceID);
                quantity_dropped.set(4 + info.world.rand.nextInt(4));
            }
        }
    }


    public static int modifyFinalDropId(BlockOre ore, int id_dropped, BlockBreakInfo info, boolean suppress_fortune) {
        float chance = suppress_fortune ? 1.0F : (1.0F + info.getHarvesterFortune() * 0.2F);
        if (OreDropHelper.canAbsorb(ore) && EnchantmentHelper.hasEnchantment(info.responsible_item_stack, Enchantments.enchantmentAbsorb)) {
            int xp = OreDropHelper.calcAbsorbXP(ore, chance);
            ((IMixinBlock) ore).invokeDropXpOnBlockBreak(info.world, info.x, info.y, info.z, xp);
        }
        Enchantment melting = Enchantments.enchantmentMelting;
        if (EnchantmentHelper.hasEnchantment(info.responsible_item_stack, melting)) {
            float melting_chance = EnchantmentHelper.getEnchantmentLevelFraction(melting, info.responsible_item_stack);
            melting_chance *= (info.responsible_item_stack.getItemAsTool().getMaterialHarvestLevel() - ore.getMinHarvestLevel(0));
            if (info.world.rand.nextFloat() < melting_chance) {
                int itemID = OreDropHelper.getMeltPieceItemID(ore);
                if (itemID != 0) return itemID;
            }
        }
        return id_dropped;
    }


    /**
     * -1: skip this
     */
    public static int onSilkTouch(BlockBreakInfo info, BlockOre ore) {
        Enchantment melting = Enchantments.enchantmentMelting;
        if (EnchantmentHelper.hasEnchantment(info.responsible_item_stack, melting)) {
            float melting_chance = EnchantmentHelper.getEnchantmentLevelFraction(melting, info.responsible_item_stack);
            melting_chance *= (info.responsible_item_stack.getItemAsTool().getMaterialHarvestLevel() - info.block.getMinHarvestLevel(0));
            if (info.world.rand.nextFloat() < melting_chance) {
                int itemID = OreDropHelper.getMeltIngotItemID(ore);
                if (itemID != 0) {
                    return ore.dropBlockAsEntityItem(info, Item.getItem(itemID));
                }
            }
        }

        return -1;
    }
}
