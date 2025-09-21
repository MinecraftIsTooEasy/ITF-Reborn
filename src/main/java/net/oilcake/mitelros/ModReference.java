package net.oilcake.mitelros;

import moddedmite.rustedironcore.api.util.FabricUtil;

public class ModReference {
    public static final String EMI = "emi";
    public static final String RWG = "rwg";
    public static final String ITE = "mite_ite";
    public static final String EXTREME = "extreme";

    public static boolean hasMod(String modId) {
        return FabricUtil.isModLoaded(modId);
    }
}
