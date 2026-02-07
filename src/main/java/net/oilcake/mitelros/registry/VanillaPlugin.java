package net.oilcake.mitelros.registry;

import net.minecraft.*;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.api.ITFPlugin;
import net.oilcake.mitelros.api.ITFRegistry;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.unsafe.ExtremeAccess;
import net.oilcake.mitelros.unsafe.ITEAccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VanillaPlugin implements ITFPlugin {
    private static final Logger LOGGER = LogManager.getLogger(VanillaPlugin.class);

    @Override
    public void register(ITFRegistry registry) {
        registry.registerItemWater(Item.carrot, ITFConfig.TagDryDilemma.getBooleanValue() ? 1 : 2);
        registry.registerItemWater(Items.ice_sucker, ITFConfig.TagDryDilemma.getBooleanValue() ? 1 : 2);
        registry.registerItemWater(Items.melon_ice, ITFConfig.TagDryDilemma.getBooleanValue() ? 1 : 2);
        registry.registerItemWater(Items.chocolate_smoothie, ITFConfig.TagDryDilemma.getBooleanValue() ? 1 : 2);
        registry.registerItemWater(Items.peeledSugarcane, 1);
        registry.registerItemWater(Items.mashedCactus, 1);
        registry.registerItemWater(Item.bread, -1);
        registry.registerItemWater(Item.cheese, -1);

        registry.registerItemWaterChance(Items.agave, ITFConfig.TagDryDilemma.getBooleanValue() ? 0.2F : 0.4F);
        registry.registerItemWaterChance(Items.glowberries, ITFConfig.TagDryDilemma.getBooleanValue() ? 0.5F : 1.0F);

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


        registry.registerOreAbsorbing(Block.oreDiamond, new ItemStack(Item.diamond));
        registry.registerOreAbsorbing(Block.oreEmerald, new ItemStack(Item.emerald));
        registry.registerOreAbsorbing(Block.oreNetherQuartz, new ItemStack(Item.netherQuartz));
        registry.registerOreAbsorbing(Block.oreLapis, new ItemStack(Item.dyePowder, 1, 4));

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
