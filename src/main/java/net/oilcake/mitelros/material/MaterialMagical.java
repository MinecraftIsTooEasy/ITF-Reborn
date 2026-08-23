package net.oilcake.mitelros.material;

import huix.glacier.api.extension.material.IArrowMaterial;
import net.minecraft.EnumEquipmentMaterial;
import net.minecraft.Material;

public class MaterialMagical extends Material implements IArrowMaterial {

    public MaterialMagical(EnumEquipmentMaterial enum_crafting_material) {
        super(enum_crafting_material);
    }

    @Override
    public float getDamageVsEntity() {
        return 0.0F;
    }

    @Override
    public float getChanceOfRecovery() {
        return 0.3F;
    }
}
