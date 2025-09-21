package net.oilcake.mitelros;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.material.EnumEquipmentMaterials;

public class EarlyRiser implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        ITFConfig.getInstance().load();
        EnumEquipmentMaterials.register();
    }

}
