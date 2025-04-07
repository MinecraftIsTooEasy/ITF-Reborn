package net.oilcake.mitelros.material;

import net.minecraft.MapColor;
import net.minecraft.MaterialLiquid;

public class MaterialPureWater extends MaterialLiquid implements IWateryMaterial {
    public MaterialPureWater(String name) {
        super(name, MapColor.waterColor);
        this.setCanDouseFire().setDrinkable();
    }

    @Override
    public int getWater() {
        return 2;
    }
}
