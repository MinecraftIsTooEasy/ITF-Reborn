package net.oilcake.mitelros.feat;

import net.minecraft.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnchantPrediction {
    public static int[] predict(Random random, ItemStack itemStack, int[] levels, boolean extended) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(random);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int[] results = new int[12];
        Arrays.fill(results, -1);

        Random copiedRandom;
        for (int line = 0; line < 3; line++) {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                copiedRandom = (Random) objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

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
        String enchant1 = Enchantment.get(info[4 * line]).toString() + info[4 * line + 1];
        if (info[4 * line + 2] == -1) return enchant1;
        String enchant2 = Enchantment.get(info[4 * line + 2]).toString() + info[4 * line + 3];
        return enchant1 + ", " + enchant2;
    }
}
