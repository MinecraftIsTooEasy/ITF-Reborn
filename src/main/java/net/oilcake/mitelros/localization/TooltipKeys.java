package net.oilcake.mitelros.localization;

public enum TooltipKeys implements EnumKey {
    WATER_ADD("item.tooltip.water.add"),
    WATER_MINUS("item.tooltip.water.minus"),
    WATER_CHANCE("item.tooltip.water.chance"),
    ;

    private final String key;

    TooltipKeys(String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return this.key;
    }
}
