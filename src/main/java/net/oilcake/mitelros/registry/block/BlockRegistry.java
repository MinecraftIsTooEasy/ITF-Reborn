package net.oilcake.mitelros.registry.block;

import net.minecraft.*;
import net.oilcake.mitelros.ITFStart;
import net.oilcake.mitelros.registry.item.ItemRegistry;
import net.xiaoyu233.fml.api.block.IBlock;
import net.xiaoyu233.fml.api.item.IItem;

import java.util.function.Function;

public class BlockRegistry extends Blocks {
    public static void run() {
        registerAnvil("nickel_anvil", anvilNickel);
        registerAnvil("tungsten_anvil", anvilTungsten);
        register("blastfurnace_stone_idle", blastFurnaceStoneIdle);
        register("blastfurnace_obsidian_idle", blastFurnaceObsidianIdle);
        register("blastfurnace_netherrack_idle", blastFurnaceNetherrackIdle);
        register("blastfurnace_stone_burning", blastFurnaceStoneBurning, false);
        register("blastfurnace_obsidian_burning", blastFurnaceObsidianBurning, false);
        register("blastfurnace_netherrack_burning", blastFurnaceNetherrackBurning, false);
        register("block_enchant_reserver", blockEnchantReserver);
        register("ore/nickel_ore", oreNickel);
        register("block/nickel_block", blockNickel);
        register("bars/nickel_bars", fenceNickel);
        register("door/door_nickel", doorNickel, false);
        register("block_smoker_idle", blockSmokerIdle);
        register("block_smoker_burning", blockSmokerBurning, false);
        register("ore/tungsten_ore", oreTungsten);
        register("block/tungsten_block", blockTungsten);
        register("bars/tungsten_bars", fenceTungsten);
        register("door/door_tungsten", doorTungsten, false);
        flowers.forEach(x -> register("flowers/" + x.getRegistryName(), x));
        register("block_enchant_enhancer", blockEnchantEnhancer);
        register("ore/uru_ore", oreUru);
        register("beetroot", beetroots, false, false);
        register("beetroot", beetrootsDead, false, false);
        register("flower_pot", flowerPotExtend, false, false);
        register("azurite_block", blockAzurite);
        register("azurite_cluster", azuriteCluster);
        register("torch_idle", torchWoodIdle);
        register("torch_off", torchWoodExtinguished);
        register("sulphur", blockSulphur);
        register("block_observer", blockObserver);
        register("block_receiver", blockReceiver);
        register("block_enchant_predicator", blockEnchantPredicator);
        register("magic_table", magicTable);
        register("crafting_table", nickelWorkBench);
        register("crafting_table", tungstenWorkBench);
        register("beacon", uruBeacon, false, true);
        register("obsidian", tungstenRuneStone, false, true);

        uruBeacon.setUnlocalizedName("uru_beacon");
        tungstenRuneStone.setUnlocalizedName("runestone");
    }

    private static void register(String texture, Block block) {
        register(texture, block, true);
    }

    private static void register(String texture, Block block, boolean show) {
        register(texture, block, true, show);
    }

    private static void register(String texture, Block block, boolean withDomain, boolean show) {
        ResourceLocation texturePath;
        if (withDomain) {
            texturePath = ITFStart.identifier(texture);
        } else {
            texturePath = new ResourceLocation(texture);
        }
        ResourceLocation identifier = ITFStart.identifier(texture);
        registerBlock(block, identifier, texturePath);
        if (show) {
            block.setCreativeTab(ItemRegistry.ITFBlock);
        }
    }

    private static void registerAnvil(String texture, BlockAnvil block) {
        registerBlock(block, ITFStart.identifier(texture), ITFStart.identifier(texture), ItemAnvilBlock::new);
        block.setCreativeTab(ItemRegistry.ITFBlock);
    }

    private static void registerBlock(Block block, ResourceLocation id, ResourceLocation texture) {
        registerBlock(block, id, texture, ItemBlock::new);
    }

    private static <T extends Block> void registerBlock(T block, ResourceLocation id, ResourceLocation texture, Function<T, Item> factory) {
        block.setUnlocalizedName(id.getResourcePath());
//        block.setUnlocalizedName(id.getResourceDomain() + "." + id.getResourcePath());// TODO
        ((IBlock) block).setNamespace(id.getResourceDomain());
        ((IBlock) block).setBlockTextureName(texture.toString());
        Item item = factory.apply(block).setUnlocalizedName(id.toString());
        ((IItem) item).setNamespace(id.getResourceDomain());
        item.setMaxStackSize(block.getItemStackLimit());
    }
}
