package net.oilcake.mitelros.event;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemBowl;
import net.minecraft.ItemStack;
import net.minecraft.Material;
import net.oilcake.mitelros.feat.FoodWater;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;

public class Hooks {

    public static void onBowlUse(ItemBowl itemBowl, ItemStack item_stack, EntityPlayer player) {
        if (player.onServer()) {
            FoodWater.onWaterDrunk(item_stack.getItem(), player);
            if (!itemBowl.contains(Material.water) && !itemBowl.contains(Material.milk)) {
                ITFEntityPlayer.cast(player).itf$getPlayerExtension().getFeastManager().update(itemBowl);
            }
        }
    }
}
