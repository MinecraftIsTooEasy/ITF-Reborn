package net.oilcake.mitelros.registry;

import net.minecraft.*;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.api.ITFPlugin;
import net.oilcake.mitelros.api.ITFRegistry;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.unsafe.ExtremeAccess;
import net.oilcake.mitelros.unsafe.ITEAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InternalPlugin implements ITFPlugin {
    private static final Logger LOGGER = LogManager.getLogger(InternalPlugin.class);

    @Override
    public void register(ITFRegistry registry) {
        boolean dryDilemma = ITFConfig.TagDryDilemma.getBooleanValue();

        registry.registerItemWater(Item.carrot, dryDilemma ? 1 : 2);
        registry.registerItemWater(Items.ice_sucker, dryDilemma ? 1 : 2);
        registry.registerItemWater(Items.melon_ice, dryDilemma ? 1 : 2);
        registry.registerItemWater(Items.chocolate_smoothie, dryDilemma ? 1 : 2);
        registry.registerItemWater(Items.peeledSugarcane, 1);
        registry.registerItemWater(Items.mashedCactus, 1);
        registry.registerItemWater(Item.bread, -1);
        registry.registerItemWater(Item.cheese, -1);

        registry.registerItemWaterChance(Items.agave, dryDilemma ? 0.2F : 0.4F);
        registry.registerItemWaterChance(Items.glowberries, dryDilemma ? 0.5F : 1.0F);

        registry.registerMaterialWater(Material.water, 1);
        registry.registerMaterialWater(Material.cereal, 2);
        registry.registerMaterialWater(Material.ice_cream, 2);
        registry.registerMaterialWater(Material.milk, 2);

        registry.registerMaterialWater(Material.fruit, dryDilemma ? 1 : 2);
        registry.registerMaterialWater(Material.desert, -1);

        // originally 4 for any else but null/mashed_potato/salad, but I'm not sure if these are all
        registry.registerMaterialWater(Materials.mushroom_stew, 4);
        registry.registerMaterialWater(Materials.beef_stew, 4);
        registry.registerMaterialWater(Materials.chicken_soup, 4);
        registry.registerMaterialWater(Materials.vegetable_soup, 4);
        registry.registerMaterialWater(Materials.cream_of_mushroom_soup, 4);
        registry.registerMaterialWater(Materials.cream_of_vegetable_soup, 4);
        registry.registerMaterialWater(Materials.pumpkin_soup, 4);
        registry.registerMaterialWater(Materials.sorbet, 4);
        registry.registerMaterialWater(Materials.porridge, 4);
        registry.registerMaterialWater(Materials.porkchop_stew, 4);
        registry.registerMaterialWater(Materials.lampchop_stew, 4);
        registry.registerMaterialWater(Materials.pure_water, 4);
        registry.registerMaterialWater(Materials.lemonade, 4);
        registry.registerMaterialWater(Materials.fish_soup, 4);
        registry.registerMaterialWater(Materials.beetroot_soup, 4);


        registry.registerMeatAnimal(EntityCow.class);
        registry.registerMeatAnimal(EntityChicken.class);
        registry.registerMeatAnimal(EntitySheep.class);
        registry.registerMeatAnimal(EntityPig.class);
        registry.registerMeatAnimal(EntityHorse.class);


        registry.registerOrePiece(Block.oreCopper, Items.pieceCopper.itemID);
        registry.registerOrePiece(Block.oreSilver, Items.pieceSilver.itemID);
        registry.registerOrePiece(Block.oreIron, Items.pieceIron.itemID);
        registry.registerOrePiece(Block.oreMithril, Items.pieceMithril.itemID);
        registry.registerOrePiece(Block.oreAdamantium, Items.pieceAdamantium.itemID);
        registry.registerOrePiece(Block.oreNetherQuartz, Item.shardNetherQuartz.itemID);
        registry.registerOrePiece(Block.oreDiamond, Item.shardDiamond.itemID);
        registry.registerOrePiece(Block.oreEmerald, Item.shardEmerald.itemID);
        registry.registerOrePiece(Blocks.oreNickel, Items.pieceNickel.itemID);
        registry.registerOrePiece(Blocks.oreTungsten, Items.pieceTungsten.itemID);
        registry.registerOrePiece(Blocks.oreUru, Items.pieceUru.itemID);
        registry.registerOrePiece(Blocks.blockSulphur, Items.sulphur.itemID);
        registry.registerOrePiece(Blocks.blockAzurite, Items.shardAzurite.itemID);


        registry.registerOreMelting(Block.oreCopper, Item.copperNugget.itemID);
        registry.registerOreMelting(Block.oreSilver, Item.silverNugget.itemID);
        registry.registerOreMelting(Block.oreGold, Item.goldNugget.itemID);
        registry.registerOreMelting(Block.oreIron, Item.ironNugget.itemID);
        registry.registerOreMelting(Block.oreMithril, Item.mithrilNugget.itemID);
        registry.registerOreMelting(Block.oreAdamantium, Item.adamantiumNugget.itemID);
        registry.registerOreMelting(Blocks.oreNickel, Items.nickelNugget.itemID);
        registry.registerOreMelting(Blocks.oreTungsten, Items.tungstenNugget.itemID);
        registry.registerOreMelting(Blocks.oreUru, Items.uruNugget.itemID);


        registry.registerOreMeltingSilkTouch(Block.oreCopper, Item.ingotCopper.itemID);
        registry.registerOreMeltingSilkTouch(Block.oreSilver, Item.ingotSilver.itemID);
        registry.registerOreMeltingSilkTouch(Block.oreGold, Item.ingotGold.itemID);
        registry.registerOreMeltingSilkTouch(Block.oreIron, Item.ingotIron.itemID);
        registry.registerOreMeltingSilkTouch(Block.oreMithril, Item.ingotMithril.itemID);
        registry.registerOreMeltingSilkTouch(Block.oreAdamantium, Item.ingotAdamantium.itemID);
        registry.registerOreMeltingSilkTouch(Blocks.oreNickel, Items.nickelIngot.itemID);
        registry.registerOreMeltingSilkTouch(Blocks.oreTungsten, Items.tungstenIngot.itemID);
        registry.registerOreMeltingSilkTouch(Blocks.oreUru, Items.uruIngot.itemID);


        registry.registerOreAbsorbing(Block.oreDiamond, new ItemStack(Item.diamond));
        registry.registerOreAbsorbing(Block.oreEmerald, new ItemStack(Item.emerald));
        registry.registerOreAbsorbing(Block.oreNetherQuartz, new ItemStack(Item.netherQuartz));
        registry.registerOreAbsorbing(Block.oreLapis, new ItemStack(Item.dyePowder, 1, 4));


        registry.registerMaterialCraftingSpeedModifier(Material.flint, 0.25F);
        registry.registerMaterialCraftingSpeedModifier(Material.obsidian, 0.25F);
        registry.registerMaterialCraftingSpeedModifier(Material.copper, 0.4F);
        registry.registerMaterialCraftingSpeedModifier(Material.silver, 0.4F);
        registry.registerMaterialCraftingSpeedModifier(Material.gold, 0.4F);
        registry.registerMaterialCraftingSpeedModifier(Material.iron, 0.5F);
        registry.registerMaterialCraftingSpeedModifier(Materials.nickel, 0.5F);
        registry.registerMaterialCraftingSpeedModifier(Materials.ancient_metal, 0.75F);
        registry.registerMaterialCraftingSpeedModifier(Materials.mithril, 1.0F);
        registry.registerMaterialCraftingSpeedModifier(Materials.tungsten, 1.5F);
        registry.registerMaterialCraftingSpeedModifier(Materials.adamantium, 2.5F);


        registry.registerMaterialBowPullTicks(Materials.tungsten, 30);
        registry.registerMaterialBowPullTicks(Materials.uru, 18);
        registry.registerMaterialBowPullTicks(Materials.mithril, 27);
        registry.registerMaterialBowPullTicks(Materials.ancient_metal, 24);


        registry.registerMaterialBowDamageModifier(Materials.tungsten, 1.15F);
        registry.registerMaterialBowDamageModifier(Materials.uru, 0.9F);
        registry.registerMaterialBowDamageModifier(Materials.mithril, 1.1F);
        registry.registerMaterialBowDamageModifier(Materials.ancient_metal, 1.05F);


        registry.registerSmeltingSpeedModifier(Items.pieceCopper, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceSilver, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceGold, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceGoldNether, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceIron, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceNickel, 4);
        registry.registerSmeltingSpeedModifier(Items.pieceMithril, 2);
        registry.registerSmeltingSpeedModifier(Items.pieceTungsten, 2);
        registry.registerSmeltingSpeedModifier(Items.pieceAdamantium, 2);


        if (ITFConfig.OreCompat.getBooleanValue()) {
            try {
                if (ModReference.hasMod(ModReference.ITE)) ITEAccess.register(registry);
                if (ModReference.hasMod(ModReference.EXTREME)) ExtremeAccess.register(registry);
            } catch (RuntimeException e) {
                LOGGER.warn("exception while compat registry", e);
            }
        }

    }
}
