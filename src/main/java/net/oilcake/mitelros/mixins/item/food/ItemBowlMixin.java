package net.oilcake.mitelros.mixins.item.food;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.feat.FoodWater;
import net.oilcake.mitelros.util.WaterHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBowl.class)
public abstract class ItemBowlMixin extends ItemVessel {
    public ItemBowlMixin(int id, Material vessel_material, Material contents_material, int standard_volume, int max_stack_size_empty, int max_stack_size_full, String texture) {
        super(id, vessel_material, contents_material, standard_volume, max_stack_size_empty, max_stack_size_full, texture);
    }

    @Inject(method = "onItemUseFinish", at = @At("HEAD"))
    private void itfDrink(ItemStack item_stack, World world, EntityPlayer player, CallbackInfo ci) {
        if (player.onServer()) {
            FoodWater.onWaterDrunk(item_stack.getItem(), player);
            if (!contains(Material.water) && !contains(Material.milk)) {
                player.itf$GetFeastManager().update(this);
            }
        }
    }

    @ModifyArg(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;convertOneOfHeldItem(Lnet/minecraft/ItemStack;)V", ordinal = 0))
    private ItemStack itfWaterBowl(ItemStack created_item_stack, @Local RaycastCollision rc) {
        Material material = WaterHelper.getWaterMaterial(rc.world, rc.block_hit_x, rc.block_hit_z);
        return new ItemStack(this.getPeerForContents(material));
    }
}
