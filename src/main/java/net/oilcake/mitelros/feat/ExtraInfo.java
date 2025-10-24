package net.oilcake.mitelros.feat;

import net.minecraft.Item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtraInfo {
    private static final Map<Item, String> MAP = new HashMap<>();

    public static Optional<String> getExtraInfo(Item item) {
        return Optional.ofNullable(MAP.getOrDefault(item, null));
    }

    public static void register(Item item, String key) {
        MAP.put(item, key);
    }
}
