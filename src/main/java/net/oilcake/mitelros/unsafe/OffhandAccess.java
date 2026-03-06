package net.oilcake.mitelros.unsafe;

import com.m.offhand.api.core.EnumHand;
import com.m.offhand.api.core.OffhandUtils;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.Slot;

public class OffhandAccess {
    public static Slot getOffhandSlot(EntityPlayer player) {
        return player.inventoryContainer.getSlot(36);
    }

    public static ItemStack getOffhandItem(EntityPlayer player) {
        return EnumHand.OFF_HAND.getItem(player);
    }

    public static void setOffhandItem(EntityPlayer player, ItemStack stack) {
        OffhandUtils.setPlayerOffhandItem(player, stack);
    }
}
