package net.oilcake.mitelros;

import net.fabricmc.api.ModInitializer;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.registry.Bootstrap;
import net.xiaoyu233.fml.FishModLoader;

public class ITFStart implements ModInitializer {
    public static final String LEGACY_CONFIG_NAME = "MITE-ITF-Reborn";
    public static final String MOD_ID = "mite-itf-reborn";
    public static final String MOD_VERSION = FishModLoader.getModContainer(MOD_ID).orElseThrow().getMetadata().getVersion().getFriendlyString();
    public static final String NameSpace = "ITF Reborn";
    public static final String ResourceDomain = "miteitfrb";
    public static final String ResourceDomainColon = "miteitfrb:";

    @Override
    public void onInitialize() {
        Bootstrap.bootstrap();
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(ResourceDomain, path);
    }
}
