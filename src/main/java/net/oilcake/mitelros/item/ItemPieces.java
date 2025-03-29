package net.oilcake.mitelros.item;

import moddedmite.rustedironcore.api.util.FabricUtil;
import net.minecraft.Item;
import net.minecraft.Material;

public class ItemPieces extends Item {
    public ItemPieces(int id, Material material, String texture) {
        super(id, material, texture);
        if (FabricUtil.isModLoaded("vanilla_stack")) setMaxStackSize(64);
        else setMaxStackSize(32);
        setCraftingDifficultyAsComponent(20.0F);
    }
}
