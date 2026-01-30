package net.oilcake.mitelros.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.registry.block.Blocks;

import java.util.Comparator;

public class RecipeCategories {
    public static final EmiRecipeCategory EnchantReserverIn = of(new ResourceLocation("itf:enchant_reserver.in"), EmiStack.of(Blocks.blockEnchantReserver), EnchantReserverInRecipe.comparatorIn);
    public static final EmiRecipeCategory EnchantReserverOut = of(new ResourceLocation("itf:enchant_reserver.out"), EmiStack.of(Blocks.blockEnchantReserver), EnchantReserverOutRecipe.comparatorOut);
    public static final EmiRecipeCategory MetalRecycle = of(new ResourceLocation("itf:metal_recycle"), EmiStack.of(Blocks.blastFurnaceStoneIdle));

    private static EmiRecipeCategory of(ResourceLocation identifier, EmiRenderable icon) {
        return new EmiRecipeCategory(identifier, icon);
    }

    @SuppressWarnings("UnstableApiUsage")
    private static EmiRecipeCategory of(ResourceLocation identifier, EmiRenderable icon, Comparator<EmiRecipe> sorter) {
        return new EmiRecipeCategory(identifier, icon, icon, sorter);
    }
}
