package net.oilcake.mitelros.mixins.item.food;

import net.minecraft.*;
import net.oilcake.mitelros.feat.FoodWater;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemFood.class)
public class ItemFoodMixin extends Item {
    @Inject(method = "onItemUseFinish", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;addFoodValue(Lnet/minecraft/Item;)V"))
    private void itfFood(ItemStack item_stack, World world, EntityPlayer player, CallbackInfo ci) {
        FoodWater.onFoodEaten(item_stack, player);
    }
}
