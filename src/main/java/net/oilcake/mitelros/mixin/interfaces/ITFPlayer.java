package net.oilcake.mitelros.mixin.interfaces;

import net.minecraft.IInventory;
import net.oilcake.mitelros.inventory.EnchantReserverInventory;

public interface ITFPlayer {
    void itf$DisplayGUIEnchantReserver(int x, int y, int z, EnchantReserverInventory slots);

    void itf$DisplayGuiMinePocket(IInventory minePocketInventory);
}
