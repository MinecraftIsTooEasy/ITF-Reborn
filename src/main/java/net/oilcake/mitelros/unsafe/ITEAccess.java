package net.oilcake.mitelros.unsafe;

import net.minecraft.Item;
import net.oilcake.mitelros.api.ITFRegistry;
import net.xiaoyu233.mitemod.miteite.block.MITEITEBlockRegistryInit;

public class ITEAccess {
    public static void register(ITFRegistry registry) {
        registry.registerOreMelting(MITEITEBlockRegistryInit.netherAdamantiumOre, Item.ingotAdamantium.itemID);
    }
}
