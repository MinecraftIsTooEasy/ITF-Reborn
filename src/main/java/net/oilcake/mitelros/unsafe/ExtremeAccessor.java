package net.oilcake.mitelros.unsafe;

import cn.wensc.mitemod.extreme.register.EXBlocksRegistryInit;
import cn.wensc.mitemod.extreme.register.EXItemsRegistryInit;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.api.ITFRegistry;

public class ExtremeAccessor {
    public static void register(ITFRegistry registry) {
        registry.registerOreMelting(EXBlocksRegistryInit.fancyRed, EXItemsRegistryInit.fancyRed.itemID);
        registry.registerOreAbsorbing(EXBlocksRegistryInit.fancyRed, new ItemStack(EXItemsRegistryInit.fancyRed));
    }
}
