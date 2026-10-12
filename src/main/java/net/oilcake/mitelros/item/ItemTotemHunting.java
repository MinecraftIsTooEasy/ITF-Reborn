package net.oilcake.mitelros.item;

import net.minecraft.EntityPlayer;
import net.minecraft.EnumEntityFX;
import net.minecraft.Potion;
import net.minecraft.PotionEffect;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.status.HuntManager;

public class ItemTotemHunting extends ItemTotem {
    public ItemTotemHunting(int id) {
        super(id, Materials.nickel, "totem");
    }

    @Override
    public void specifiedEffect(EntityPlayer player) {
        for (int i = 0; i < 8; i++) {
            player.entityFX(EnumEntityFX.summoned);
        }
        HuntManager huntManager = player.itf$getPlayerExtension().getHuntManager();
        huntManager.setHunt_counter(400);
        huntManager.hunt_cap = true;
        int duration = ITFConfig.TagTotemBlessing.getBooleanValue() ? 800 : 400;
        player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, duration, (int) ((1.0F - player.getHealthFraction()) * 2.0F)));
    }
}
