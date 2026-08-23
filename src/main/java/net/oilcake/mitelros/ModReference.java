package net.oilcake.mitelros;

import moddedmite.rustedironcore.api.util.FabricUtil;

public class ModReference {
    public static final String EMI = "emi";
    public static final String RWG = "rwg";
    public static final String ITE = "mite_ite";
    public static final String EXTREME = "extreme";
    public static final String BREAD_SKIN = "bread_skin";
    public static final String BAUBLES = "baubles";
    public static final String OFFHAND = "offhand";
    public static final String WAILA = "waila";
    public static final String ENCHANT_DIVINE = "enchantdivine";

    public static boolean hasMod(String modId) {
        return FabricUtil.isModLoaded(modId);
    }
}
