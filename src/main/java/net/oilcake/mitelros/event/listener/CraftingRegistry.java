package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.CraftingRecipeRegisterEvent;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.registry.recipe.RecipeRegistry;

import java.util.function.Consumer;

public class CraftingRegistry implements Consumer<CraftingRecipeRegisterEvent> {
    @Override
    public void accept(CraftingRecipeRegisterEvent event) {
        event.registerArmorRepairRecipe(Items.wolf_fur, Materials.wolf_fur);
        event.registerArmorRepairRecipe(Items.iceChunk, Materials.ice_chunk);

        RecipeRegistry.registerRecipes(event);

        Blocks.registerRecipes(event);
    }
}
