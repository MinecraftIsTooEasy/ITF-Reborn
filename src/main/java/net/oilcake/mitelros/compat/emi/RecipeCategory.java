package net.oilcake.mitelros.compat.emi;

import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.registry.block.Blocks;

public class RecipeCategory {
    public static final EmiRecipeCategory EnchantReserverIn = new EmiRecipeCategory(new ResourceLocation("itf:enchant_reserver.in"), EmiStack.of(Blocks.blockEnchantReserver), null, EnchantReserverInRecipe.comparatorIn);
    public static final EmiRecipeCategory EnchantReserverOut = new EmiRecipeCategory(new ResourceLocation("itf:enchant_reserver.out"), EmiStack.of(Blocks.blockEnchantReserver), null, EnchantReserverOutRecipe.comparatorOut);
    public static final EmiRecipeCategory MetalRecycle = new EmiRecipeCategory(new ResourceLocation("itf:metal_recycle"), EmiStack.of(Blocks.blastFurnaceStoneIdle), null);
}
