package net.oilcake.mitelros.item;

import net.minecraft.Item;
import net.minecraft.Material;
import net.oilcake.mitelros.material.Materials;

public class ItemDetector extends Item {
    public ItemDetector(int id, Material material, String texture) {
        super(id, material, texture);
        this.setMaxStackSize(1);
        this.setCraftingDifficultyAsComponent(800.0F);
        this.addMaterial(Material.ancient_metal,Material.gold, Materials.crystal);
    }

}
