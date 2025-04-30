package net.oilcake.mitelros.world.biome;

public enum BiomeMode {
    NORMAL,
    COMPAT,
    ;

    public boolean isCompat() {
        return this == COMPAT;
    }
}
