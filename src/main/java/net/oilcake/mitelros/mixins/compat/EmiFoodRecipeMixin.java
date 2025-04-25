package net.oilcake.mitelros.mixins.compat;

import dev.emi.emi.api.widget.WidgetHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import moddedmite.emi.recipe.EmiFoodRecipe;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.Reference;
import net.oilcake.mitelros.client.texture.Textures;
import net.oilcake.mitelros.mixin.interfaces.ITFItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Restriction(require = @Condition(Reference.EMI))
@Mixin(EmiFoodRecipe.class)
public abstract class EmiFoodRecipeMixin {
    @Shadow
    private int y;

    @Shadow
    public abstract void checkY(int value);

    @Unique
    private int water;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectInit(ItemStack foodStack, CallbackInfo ci) {
        this.water = ((ITFItem) foodStack.getItem()).itf$GetFoodWater();
    }

    @Inject(method = "addWidgets", at = @At(value = "INVOKE", target = "Lmoddedmite/emi/recipe/EmiFoodRecipe;checkY(I)V", ordinal = 4, shift = At.Shift.AFTER))
    private void addWater(WidgetHolder widgets, CallbackInfo ci) {
        ResourceLocation iconsItf = Textures.icons_itf;
        int i, haunchXCoord;
        for (i = 0; i < this.water / 2; ++i) {
            widgets.addTexture(iconsItf, 10 * i + 25, this.y, 9, 9, 16, 54);
            widgets.addTexture(iconsItf, 10 * i + 25, this.y, 9, 9, 16 + 9, 54);
        }

        if (this.water % 2 != 0) {
            haunchXCoord = 10 * i + 25;
            widgets.addTexture(iconsItf, haunchXCoord, this.y, 9, 9, 16, 54);
            widgets.addTexture(iconsItf, haunchXCoord, this.y, 9, 9, 16 + 18, 54);
        }

        this.checkY(this.water);
    }
}
