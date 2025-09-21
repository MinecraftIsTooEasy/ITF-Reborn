package net.oilcake.mitelros.compat;

import cn.wensc.mitemod.shop.api.ShopPlugin;
import cn.wensc.mitemod.shop.api.ShopRegistry;
import net.oilcake.mitelros.registry.item.Items;

public class ShopPluginImpl implements ShopPlugin {
    @Override
    public void register(ShopRegistry registry) {
        registry.registerSoldPrice(Items.agave, 1);
        registry.registerSoldPrice(Items.glowberries, 1);
    }
}
