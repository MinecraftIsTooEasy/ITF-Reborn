package net.oilcake.mitelros.unsafe;

import com.m.offhand.api.core.EnumHand;
import com.m.offhand.api.core.OffhandUtils;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;

public class OffhandAccess {
    public static ItemStack getOffhandItem(EntityPlayer player) {
        return EnumHand.OFF_HAND.getItem(player);
    }

    public static void setOffhandItem(EntityPlayer player, ItemStack stack) {
        OffhandUtils.setPlayerOffhandItem(player, stack);
    }
}
