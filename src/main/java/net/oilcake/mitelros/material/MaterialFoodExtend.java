package net.oilcake.mitelros.material;

import net.minecraft.MaterialFood;

public class MaterialFoodExtend extends MaterialFood implements IWateryMaterial {
    private final int water;

    public MaterialFoodExtend(String name, int water) {
        super(name);
        this.water = water;
    }

    @Override
    public int getWater() {
        return this.water;
    }
}
