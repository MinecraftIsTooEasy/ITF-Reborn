package net.oilcake.mitelros.feat.anvil;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.mixin.interfaces.ITFEnchantment;

import java.util.Map;

public class AnvilSystem {
    public static boolean isActive() {
        return ITFConfig.ITFAnvilSystem.getBooleanValue();
    }

    public static int calcXPDiffOnEnchanting(ItemStack item_stack_in_first_slot, NBTTagList enchantmentsOfBook) {
        int xpDifference = 0;
        Map<?, ?> enchantmentOnItem = EnchantmentHelper.getEnchantmentsMap(item_stack_in_first_slot);
        int xpMultiplier = enchantmentOnItem.isEmpty() ? ITFConfig.AnvilXPMultiplierInit.getIntegerValue() : ITFConfig.AnvilXPMultiplier.getIntegerValue();// if not enchanted, we take less
        for (int i = 0; i < enchantmentsOfBook.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound) enchantmentsOfBook.tagAt(i);
            int id = tag.getShort("id");
            int bookLevel = tag.getShort("lvl");
            Enchantment enchantment = Enchantment.enchantmentsList[id];
            if (enchantmentOnItem.containsKey(id)) {
                int originalLevel = (int) enchantmentOnItem.get(id);
                if (bookLevel > originalLevel) {
                    xpDifference -= calculateEquivalentLevel(id, bookLevel) * xpMultiplier;
                } else if (bookLevel == originalLevel) {
                    if (bookLevel < enchantment.getNumLevels()) {
                        xpDifference -= calculateEquivalentLevel(id, bookLevel + 1) * xpMultiplier;
                    }
                }
            } else if (enchantment.canEnchantItem(item_stack_in_first_slot.getItem()) && canApplyTogether(enchantmentOnItem, enchantment)) {
                xpDifference -= calculateEquivalentLevel(id, bookLevel) * xpMultiplier;
            }
        }
        return xpDifference;
    }

    public static int calcXPDiffOnDisenchanting(ItemStack item_stack_in_first_slot) {
        Map mapBefore = EnchantmentHelper.getEnchantmentsMap(item_stack_in_first_slot);
        int xpDifference = 0;
        for (Object o : mapBefore.keySet()) {
            xpDifference += AnvilSystem.calculateEquivalentLevel((int) o, (int) mapBefore.get(o)) * ITFConfig.AnvilXPMultiplierReward.getIntegerValue();
        }
        return xpDifference;
    }

    public static int calculateEquivalentLevel(int id, int level) {
        Enchantment enchantment = Enchantment.get(id);
        if (((ITFEnchantment) enchantment).isCurse()) return 0;
        int original = enchantment.hasLevels() ?
                enchantment.getMinEnchantmentLevelsCost(level) :
                enchantment.getMinEnchantmentLevelsCost();
        return original * (((ITFEnchantment) enchantment).isTreasure() ? 2 * ITFConfig.AnvilXPMultiplierTreasure.getIntegerValue() : 2);
    }

    public static boolean canApplyTogether(Map enchantmentMap, Enchantment enchantment) {
        for (Object o : enchantmentMap.keySet()) {
            Enchantment enchantmentInMap = Enchantment.get((int) o);
            if (enchantmentInMap.effectId == enchantment.effectId) continue;
            if (!enchantmentInMap.canApplyTogether(enchantment)) return false;
        }
        return true;
    }

    public static String getString(EntityPlayer player, int xpDifference) {
        int hypothetical_level = player.getExperienceLevel(player.experience + xpDifference);
        int level_cost = player.getExperienceLevel() - hypothetical_level;
        String text;
        if (level_cost < 0) {
            text = I18n.getStringParams("gui.repair.rewardMoreThanOneLevel", -level_cost);
        } else if (level_cost > 0) {
            text = I18n.getStringParams("gui.repair.costMoreThanOneLevel", level_cost);
        } else {
            text = I18n.getString("gui.repair.effectLessThanOneLevel");
        }
        return EnumChatFormatting.YELLOW + text;
    }
}
