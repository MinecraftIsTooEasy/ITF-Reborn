package net.oilcake.mitelros.util;

import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.NBTTagList;

import java.util.function.Consumer;

public class InventoryUtil {
    public static void load(IInventory inventory, NBTTagCompound tagCompound) {
        if (tagCompound != null && tagCompound.hasKey("Items")) {
            NBTTagList minePocket = tagCompound.getTagList("Items");
            for (int index = 0; index < minePocket.tagCount(); index++) {
                NBTTagCompound nbtBase = (NBTTagCompound) minePocket.tagAt(index);
                int slot = nbtBase.getByte("Slot");
                inventory.setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(nbtBase));
            }
        }
    }

    public static void save(IInventory inventory, NBTTagCompound tagCompound) {
        NBTTagList var2 = new NBTTagList();
        for (int var3 = 0; var3 < inventory.getSizeInventory(); ++var3) {
            ItemStack var4 = inventory.getStackInSlot(var3);
            if (var4 == null) continue;
            NBTTagCompound var5 = new NBTTagCompound();
            var5.setByte("Slot", (byte) var3);
            var4.writeToNBT(var5);
            var2.appendTag(var5);
        }
        tagCompound.setTag("Items", var2);
    }

    public static void stream(IInventory inventory, Consumer<ItemStack> action) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (stack == null) continue;
            action.accept(stack);
        }
    }
}
