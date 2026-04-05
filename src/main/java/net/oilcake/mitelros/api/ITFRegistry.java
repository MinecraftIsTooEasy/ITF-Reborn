package net.oilcake.mitelros.api;

import net.minecraft.*;

public interface ITFRegistry {
    void registerItemWater(Item item, int water);

    void registerItemWaterChance(Item item, float chance);

    /**
     * If you enable the config {@code Apocalypse}, the animals registered here won't spawn naturally.
     */
    void registerMeatAnimal(Class<? extends Entity> clazz);


    /**
     * If registered, the block will drop 4 to 7 pieces.
     */
    void registerOrePiece(Block blockOre, int dropItemID);

    void registerOreMelting(Block blockOre, int dropItemID);

    void registerOreMeltingSilkTouch(Block blockOre, int dropItemID);

    /**
     * @param output used to look up the exp equivalent.
     */
    void registerOreAbsorbing(Block blockOre, ItemStack output);

    /**
     * For bowl or other food containers
     */
    void registerMaterialWater(Material material, int water);

    /**
     * for workbench; 1 for unit
     */
    void registerMaterialCraftingSpeedModifier(Material material, float modifier);

    void registerMaterialBowPullTicks(Material material, int ticks);

    /**
     * 0.75 for default
     */
    void registerMaterialBowDamageModifier(Material material, float modifier);

    void registerSmeltingSpeedModifier(Item item, float modifier);
}
