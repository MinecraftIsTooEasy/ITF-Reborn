package net.oilcake.mitelros.mixins.compat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.emi.api.widget.WidgetHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.emi.recipe.EmiFoodRecipe;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.api.ITFApi;
import net.oilcake.mitelros.client.texture.Textures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(value = ModReference.EMI, versionPredicates = ">=1.1.24"))
@Mixin(EmiFoodRecipe.class)
public abstract class EmiFoodRecipeMixin {
    @Shadow
    protected abstract void drawFoodValueBar(WidgetHolder widgets, int amount, int fullU1, int fullU2, int halfU, int v, ResourceLocation texture);

    @Unique
    private int water;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectInit(ItemStack foodStack, CallbackInfo ci) {
        this.water = ITFApi.getItemWater(foodStack.getItem());
    }

    @Inject(
            method = "addWidgets",
            at = @At(
                    value = "INVOKE",
                    target = "Lmoddedmite/emi/recipe/EmiFoodRecipe;drawFoodValueBar(Ldev/emi/emi/api/widget/WidgetHolder;IIIIILnet/minecraft/ResourceLocation;)V",
                    ordinal = 3,
                    shift = At.Shift.AFTER
            )
    )
    private void addWater(WidgetHolder widgets, CallbackInfo ci) {
        this.drawFoodValueBar(widgets, this.water, 16, 16 + 9, 16 + 18, 54, Textures.icons_itf);
    }

    @ModifyReturnValue(method = "getVisibleRowCount", at = @At("RETURN"))
    private int addWater(int original) {
        if (this.water > 0) original++;
        return original;
    }
}
