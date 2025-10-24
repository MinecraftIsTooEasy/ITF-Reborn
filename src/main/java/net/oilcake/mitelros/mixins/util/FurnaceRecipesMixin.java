package net.oilcake.mitelros.mixins.util;

import net.minecraft.FurnaceRecipes;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.feat.MetalRecycle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FurnaceRecipes.class)
public class FurnaceRecipesMixin {
    @Inject(method = "doesSmeltingRecipeExistFor", at = @At("HEAD"), cancellable = true)
    private void cheat(ItemStack input_item_stack, CallbackInfoReturnable<Boolean> cir) {
        if (MetalRecycle.canApply(input_item_stack)) cir.setReturnValue(true);
    }
}
