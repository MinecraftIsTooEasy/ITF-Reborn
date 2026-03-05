package net.oilcake.mitelros.unsafe;

import cn.wensc.mitemod.extreme.api.IEXPlayer;
import cn.wensc.mitemod.extreme.inventory.InventoryJewelry;
import cn.wensc.mitemod.extreme.register.EXBlocksRegistryInit;
import cn.wensc.mitemod.extreme.register.EXItemsRegistryInit;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.api.ITFRegistry;
import net.oilcake.mitelros.item.ItemTotem;

public class ExtremeAccess {
    public static void register(ITFRegistry registry) {
        registry.registerOreMelting(EXBlocksRegistryInit.fancyRed, EXItemsRegistryInit.fancyRed.itemID);
        registry.registerOreAbsorbing(EXBlocksRegistryInit.fancyRed, new ItemStack(EXItemsRegistryInit.fancyRed));
    }

    public static ItemStack findTotem(EntityPlayer player) {
        InventoryJewelry jewelry = IEXPlayer.getInventoryJewelryStatic(player);
        for (int i = 0; i < jewelry.getSizeInventory(); i++) {
            ItemStack stack = jewelry.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof ItemTotem) return stack;
        }
        return null;
    }

    public static void consumeTotem(EntityPlayer player, ItemStack itemStack) {
        InventoryJewelry jewelry = IEXPlayer.getInventoryJewelryStatic(player);
        for (int i = 0; i < jewelry.getSizeInventory(); i++) {
            ItemStack stack = jewelry.getStackInSlot(i);
            if (stack == itemStack) {
                jewelry.setInventorySlotContents(i, null);
                return;
            }
        }
    }
}
