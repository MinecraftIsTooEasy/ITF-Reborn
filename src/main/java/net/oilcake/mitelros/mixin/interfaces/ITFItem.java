package net.oilcake.mitelros.mixin.interfaces;

public interface ITFItem {
    default int itf$GetFoodWater() {
        return 0;
    }

    default void itf$SetFoodWater(int water) {
    }
}
