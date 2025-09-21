package net.oilcake.mitelros.mixin.interfaces;

public interface ITFEnchantment {
    default boolean isCurse() {
        return false;
    }

    default boolean isTreasure() {
        return false;
    }
}
