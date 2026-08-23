package net.oilcake.mitelros.compat.waila;

import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;

public final class WailaPlugin implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        WailaCompat.register(registrar);
    }
}
