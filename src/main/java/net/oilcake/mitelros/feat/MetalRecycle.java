package net.oilcake.mitelros.feat;

import net.minecraft.*;
import net.oilcake.mitelros.item.api.ItemMorningStar;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiConsumer;

public class MetalRecycle {
    public static final List<Class<? extends IDamageableItem>> tools = List.of(
            ItemSword.class, ItemAxe.class, ItemPickaxe.class, ItemHoe.class, ItemShovel.class,
            ItemWarHammer.class, ItemBattleAxe.class, ItemScythe.class, ItemDagger.class, ItemKnife.class,
            ItemMorningStar.class, ItemHatchet.class, ItemShears.class, ItemMattock.class);
    public static final List<Class<? extends IDamageableItem>> armors = List.of(
            ItemHelmet.class, ItemCuirass.class, ItemLeggings.class, ItemBoots.class
    );

    public static boolean canApply(ItemStack itemStack) {
        return canApply(itemStack.getItem());
    }

    public static boolean canApply(Item item) {
        Class<? extends Item> clazz = item.getClass();
        return (item instanceof ItemHorseArmor || item == Item.cauldron) && item.getRepairItem() != null
                || MetalRecycle.tools.contains(clazz) || MetalRecycle.armors.contains(clazz);
    }

    private static void registerRecipeSafe(BiConsumer<Item, ItemStack> registry, Item item, ItemStack itemStack) {
        if (item != null && itemStack != null) {
            registry.accept(item, itemStack);
        }
    }

    @Nullable
    public static ItemStack recycleMetal(Item item) {
        return recycleMetal(new ItemStack(item));
    }

    @Nullable
    public static ItemStack recycleMetal(ItemStack itemStack) {
        Item item = itemStack.getItem();
        if (item instanceof ItemHorseArmor || item == Item.cauldron) {
            Item repairItem = item.getRepairItem();
            return repairItem == null ? null : new ItemStack(repairItem, item == Item.cauldron ? 21 : 15);
        }
        return recycleMetal(itemStack, (Item & IDamageableItem) item);
    }

    @Nullable
    private static <T extends Item & IDamageableItem> ItemStack recycleMetal(ItemStack input_item_stack, T metal_product) {
        Item repairItem = metal_product.getRepairItem();
        if (repairItem == null) return null;
        float ingotToNugget = input_item_stack.getItem().isChainMail() ? 4.0F : 9.0F;
        float durabilityRatio = 1 - (float) input_item_stack.getItemDamage() / input_item_stack.getMaxDamage();
        float component = metal_product.getNumComponentsForDurability();
        int quantity = (int) (durabilityRatio * component * ingotToNugget / 3.0F);
        Material hardestMaterial = metal_product.getHardestMetalMaterial();
        if (hardestMaterial == Material.rusted_iron) {
            quantity /= 3;
        }
        quantity = Math.max(1, quantity);
        return new ItemStack(repairItem, quantity);
    }

    public static void registerToEmi(BiConsumer<Item, ItemStack> registry) {
        for (Item item : Item.itemsList) {
            if (item == null) continue;
            if (canApply(item)) registerRecipeSafe(registry, item, recycleMetal(item));
        }
    }
}
