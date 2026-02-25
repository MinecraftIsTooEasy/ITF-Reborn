package net.oilcake.mitelros.feat;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.material.IWateryMaterial;
import net.oilcake.mitelros.potion.PotionExtend;
import net.oilcake.mitelros.registry.property.ITFProperties;

import java.util.Random;

public class FoodWater {
    @SuppressWarnings("RedundantIfStatement")
    public static int getWater(Item item) {
        int water = ITFProperties.WATER.getOrDefault(item);
        if (water != 0) return water;

        Material material = null;
        if (item instanceof ItemBowl bowl) {
            material = bowl.getContents();
        }
        if (item instanceof ItemFood food) {
            material = food.getMaterial(0);
        }

        if (material != null) {
            water = getMaterialWater(material);
            if (water != 0) return water;
        }

        return 0;
    }

    public static float getWaterChance(Item item) {
        Float v = ITFProperties.WATER_CHANCE.get(item);
        if (v != null) return v;
        return 0.0F;
    }

    private static int getMaterialWater(Material material) {
        if (material instanceof IWateryMaterial iWateryMaterial) {
            return iWateryMaterial.getWater();
        }
        return ITFProperties.MATERIAL_WATER.getOrDefault(material);
    }

    public static void onWaterDrunk(Item item, EntityPlayer player) {
        if (item.hasMaterial(Material.water)) {
            float randomFloat = player.rand.nextFloat();
            if (randomFloat > 0.8D)
                player.addPotionEffect(new PotionEffect(Potion.poison.id, 450, 0));
            player.addPotionEffect(new PotionEffect(PotionExtend.dehydration.id, (int) (160.0D * (1.0D + randomFloat)), 0));
        }
    }

    public static void onFoodEaten(ItemStack item_stack, EntityPlayer player) {
        Random rand = player.rand;
        Item item = item_stack.getItem();
        if (item == Item.rottenFlesh)
            player.addPotionEffect(new PotionEffect(Potion.confusion.id, 600, 0));
        if (item_stack.hasMaterial(Material.bread) || item_stack.hasMaterial(Material.desert))
            player.addPotionEffect(new PotionEffect(PotionExtend.thirsty.id, 1280, 0));
        if (item instanceof ItemMeat meat) {
            if (meat.is_cooked) {
                player.addPotionEffect(new PotionEffect(PotionExtend.thirsty.id, 1280, 0));
            } else {
                if (ITFConfig.TagDigest.getBooleanValue()) return;
                if (rand.nextInt(4) == 0) {
                    player.addPotionEffect(new PotionEffect(PotionExtend.dehydration.id, (int) (120.0D * (1.0D + rand.nextDouble())), 0));
                }
            }
        }
    }
}
