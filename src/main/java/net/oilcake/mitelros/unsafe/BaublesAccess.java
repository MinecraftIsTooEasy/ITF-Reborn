package net.oilcake.mitelros.unsafe;

import baubles.api.BaublesApi;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;

public class BaublesAccess {
    public static IInventory getInventory(EntityPlayer player) {
        return BaublesApi.getBaubles(player);
    }

    public static boolean isAmuletIndex(int index) {
        return index == 1;
    }

    public static ItemStack getStackInAmulet(EntityPlayer player) {
        IInventory baubles = BaublesApi.getBaubles(player);
        return baubles.getStackInSlot(1);
    }

    public static void clearAmulet(EntityPlayer player) {
        IInventory baubles = BaublesApi.getBaubles(player);
        baubles.setInventorySlotContents(0, null);
    }
}
