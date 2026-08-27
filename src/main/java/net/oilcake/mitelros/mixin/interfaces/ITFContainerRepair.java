package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.EntityPlayer;
import net.oilcake.mitelros.feat.anvil.AnvilStatus;

public interface ITFContainerRepair {
    boolean itf$isAnvilSystemActive();

    int itf$getXPDifference();

    AnvilStatus itf$getAnvilStatus();

    void itf$setAnvilData(boolean active, int xpDifference, AnvilStatus anvilStatus);

    void itf$onTakeOutput(EntityPlayer player);
}
