package net.oilcake.mitelros.feat.anvil;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.mixin.interfaces.ITFEnchantment;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnvilSystem {
    public static boolean isActive() {
        return ITFConfig.ITFAnvilSystem.getBooleanValue();
    }

    public static int calcXPDiffOnEnchanting(ItemStack item_stack_in_first_slot, NBTTagList enchantmentsOfBook) {
        int xpDifference = 0;
        Map<Enchantment, Integer> enchantmentOnItem = getEnchantments(item_stack_in_first_slot);
        int xpMultiplier = enchantmentOnItem.isEmpty() ? ITFConfig.AnvilXPMultiplierInit.getIntegerValue() : ITFConfig.AnvilXPMultiplier.getIntegerValue();// if not enchanted, we take less

        for (Map.Entry<Enchantment, Integer> entry : asEnchantments(enchantmentsOfBook).entrySet()) {
            Enchantment enchantment = entry.getKey();
            int bookLevel = entry.getValue();
            if (enchantmentOnItem.containsKey(enchantment)) {
                int originalLevel = enchantmentOnItem.get(enchantment);
                if (bookLevel > originalLevel) {
                    xpDifference -= calculateEquivalentLevel(enchantment, bookLevel) * xpMultiplier;
                } else if (bookLevel == originalLevel) {
                    if (bookLevel < enchantment.getNumLevels()) {
                        xpDifference -= calculateEquivalentLevel(enchantment, bookLevel + 1) * xpMultiplier;
                    }
                }
            } else if (enchantment.canEnchantItem(item_stack_in_first_slot.getItem()) && canApplyTogether(enchantmentOnItem, enchantment)) {
                xpDifference -= calculateEquivalentLevel(enchantment, bookLevel) * xpMultiplier;
            }
        }
        return xpDifference;
    }

    public static int calcXPDiffOnDisenchanting(ItemStack item_stack_in_first_slot) {
        Map<Enchantment, Integer> mapBefore = getEnchantments(item_stack_in_first_slot);
        int xpDifference = 0;
        for (Map.Entry<Enchantment, Integer> entry : mapBefore.entrySet()) {
            xpDifference += AnvilSystem.calculateEquivalentLevel(entry.getKey(), entry.getValue()) * ITFConfig.AnvilXPMultiplierReward.getIntegerValue();
        }
        return xpDifference;
    }

    public static int calculateEquivalentLevel(Enchantment enchantment, int level) {
        if (((ITFEnchantment) enchantment).isCurse()) return 0;
        int original = enchantment.hasLevels() ?
                enchantment.getMinEnchantmentLevelsCost(level) :
                enchantment.getMinEnchantmentLevelsCost();
        return original * (((ITFEnchantment) enchantment).isTreasure() ? 2 * ITFConfig.AnvilXPMultiplierTreasure.getIntegerValue() : 2);
    }

    public static boolean canApplyTogether(ItemStack stack, Enchantment enchantment) {
        return canApplyTogether(getEnchantments(stack), enchantment);
    }

    public static boolean canApplyTogether(Map<Enchantment, Integer> enchantmentMap, Enchantment enchantment) {
        for (Enchantment enchantmentInMap : enchantmentMap.keySet()) {
            if (enchantmentInMap.effectId == enchantment.effectId) continue;
            if (!enchantmentInMap.canApplyTogether(enchantment)) return false;
        }
        return true;
    }

    public static String getString(EntityPlayer player, int xpDifference) {
        int currentLevel = player.getExperienceLevel();
        int resultingLevel = player.getExperienceLevel(player.experience + xpDifference);
        String text;
        if (xpDifference > 0) {
            text = I18n.getStringParams("gui.repair.reward_xp", xpDifference, currentLevel, resultingLevel);
        } else if (xpDifference < 0) {
            text = I18n.getStringParams("gui.repair.cost_xp", -xpDifference, currentLevel, resultingLevel);
        } else {
            text = I18n.getString("gui.repair.no_xp_change");
        }
        return EnumChatFormatting.YELLOW + text;
    }

    private static Map<Enchantment, Integer> asEnchantments(NBTTagList nbtTagList) {
        LinkedHashMap<Enchantment, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < nbtTagList.tagCount(); ++i) {
            NBTTagCompound tag = (NBTTagCompound) nbtTagList.tagAt(i);
            int id = tag.getShort("id");
            int lvl = tag.getShort("lvl");
            Enchantment enchantment = Enchantment.get(id);
            if (enchantment != null) map.put(enchantment, lvl);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private static Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
        LinkedHashMap<Enchantment, Integer> map = new LinkedHashMap<>();
        Map<Integer, Integer> rawMap = EnchantmentHelper.getEnchantmentsMap(stack);
        rawMap.forEach(
                (id, level) -> {
                    Enchantment enchantment = Enchantment.get(id);
                    if (enchantment != null) map.put(enchantment, level);
                }
        );
        return map;
    }
}
