package net.oilcake.mitelros.material;

import huix.glacier.api.extension.material.IArmorMaterial;
import net.minecraft.EnumEquipmentMaterial;
import net.minecraft.Material;

public class MaterialWolfFur extends Material implements IArmorMaterial {
    public MaterialWolfFur(EnumEquipmentMaterial enum_crafting_material) {
        super(enum_crafting_material);
    }

    @Override
    public int getProtection() {
        return 3;
    }
}
