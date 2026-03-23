package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.feat.anvil.AnvilStatus;

public interface ITFContainerRepair {
    int itf$getXPDifference();

    AnvilStatus itf$getAnvilStatus();

    void itf$onTakeOutput(EntityPlayer player, ItemStack stack);
}
