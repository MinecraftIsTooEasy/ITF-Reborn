package net.oilcake.mitelros.compat;

import com.inf1nlty.newshop.api.ShopPlugin;
import com.inf1nlty.newshop.api.ShopRegistry;
import net.oilcake.mitelros.registry.item.Items;

public class ShopPluginImpl implements ShopPlugin {
    @Override
    public void register(ShopRegistry registry) {
        registry.addItem(Items.agave, 0.0, 1.0);
        registry.addItem(Items.glowberries, 0.0, 1.0);
    }
}
