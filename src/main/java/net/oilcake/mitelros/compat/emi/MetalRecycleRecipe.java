package net.oilcake.mitelros.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.recipe.EmiCookingRecipe;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;

public class MetalRecycleRecipe extends EmiCookingRecipe {
    public MetalRecycleRecipe(ResourceLocation id, ItemStack input, ItemStack output, EmiRecipeCategory category, int fuelMultiplier, int xp) {
        super(id, input, output, category, fuelMultiplier, xp);
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return RecipeCategories.MetalRecycle;
    }
}
