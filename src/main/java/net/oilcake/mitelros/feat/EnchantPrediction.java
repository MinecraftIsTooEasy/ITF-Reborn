package net.oilcake.mitelros.feat;

import net.minecraft.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnchantPrediction {
    public static int[] predict(long seed, ItemStack itemStack, int[] levels, boolean extended) {
        int[] results = new int[12];
        Arrays.fill(results, -1);

        for (int line = 0; line < 3; line++) {
            Random copiedRandom = new Random(seed);
            List enchantmentList = EnchantmentHelper.buildEnchantmentList(copiedRandom, itemStack, levels[line]);
            if (enchantmentList == null) continue;

            boolean isBook = itemStack.itemID == Item.book.itemID;
            int onlyEnchantment = isBook ? copiedRandom.nextInt(enchantmentList.size()) : -1;
            if (isBook) {
                enchantmentList = List.of(enchantmentList.get(onlyEnchantment));
            }

            for (int index = 0; (index < enchantmentList.size()) && (index < (extended ? 2 : 1)); ++index) {
                EnchantmentData enchantmentData = (EnchantmentData) enchantmentList.get(index);
                results[line * 4 + 2 * index] = enchantmentData.enchantmentobj.effectId;
                results[line * 4 + 2 * index + 1] = enchantmentData.enchantmentLevel;
            }
        }
        return results;
    }

    /**
     * For the 12 size array, every 4 ints are a group.
     * <br>
     * In 4 ints, they may carry two enchantments, where a single enchantment is descript by id and level.
     * <br>
     * If id is -1, it means no enchantment.
     */
    public static String readInfo(int[] info, int line) {
        if (info[4 * line] == -1) return null;
        String enchant1 = Enchantment.get(info[4 * line]).toString()
                + StatCollector.translateToLocal("enchantment.level." + info[4 * line + 1]);
        if (info[4 * line + 2] == -1) return enchant1;
        String enchant2 = Enchantment.get(info[4 * line + 2]).toString()
                + StatCollector.translateToLocal("enchantment.level." + info[4 * line + 3]);
        return enchant1 + ", " + enchant2;
    }
}
