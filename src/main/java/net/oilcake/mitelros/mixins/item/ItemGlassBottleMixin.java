package net.oilcake.mitelros.mixins.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.util.WaterHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemGlassBottle.class)
public class ItemGlassBottleMixin extends Item {
    @ModifyArg(method = "onItemRightClick", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;convertOneOfHeldItem(Lnet/minecraft/ItemStack;)V"))
    private ItemStack suspiciousPotion(ItemStack created_item_stack, @Local RaycastCollision rc) {
        if (WaterHelper.isPureWater(rc.world, rc.block_hit_x, rc.block_hit_z)) {
            return new ItemStack(potion, 1, 0);
        }
        return new ItemStack(Items.suspiciousPotion, 1, 0);
    }
}
