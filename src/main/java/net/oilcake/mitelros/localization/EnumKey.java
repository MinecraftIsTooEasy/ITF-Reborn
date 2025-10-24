package net.oilcake.mitelros.localization;

import net.minecraft.I18n;

public interface EnumKey {
    String getKey();

    default String translate() {
        return I18n.getString(getKey());
    }

    default String translate(Object... objects) {
        return I18n.getStringParams(getKey(), objects);
    }
}
