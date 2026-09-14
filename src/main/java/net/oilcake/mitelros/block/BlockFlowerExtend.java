package net.oilcake.mitelros.block;

import moddedmite.rustedironcore.api.util.FabricUtil;
import net.minecraft.*;
import net.oilcake.mitelros.registry.block.Blocks;

import java.util.Random;

public class BlockFlowerExtend extends BlockFlower {
    private final String registryName;

    private static final int[] candidates = new int[FlowerCollection.TYPE_NUMBER];

    public BlockFlowerExtend(int id, String name) {
        super(id, Material.plants);
        this.registryName = name;
        setHardness(0.0F);
        setStepSound(soundGrassFootstep);
        setMaxStackSize(FabricUtil.isModLoaded("vanilla_stack") ? 64 : 32);
    }

    public String getRegistryName() {
        return this.registryName;
    }

    @Override
    public boolean canBeReplacedBy(int metadata, Block other_block, int other_block_metadata) {
        return (other_block == this) ? ((other_block_metadata != metadata)) : super.canBeReplacedBy(metadata, other_block, other_block_metadata);
    }

    @Override
    public void setBlockBoundsBasedOnStateAndNeighbors(IBlockAccess block_access, int x, int y, int z) {
        float width = 0.2F;
        setBlockBoundsForCurrentThread((0.5F - width), 0.0D, (0.5F - width), (0.5F + width), 0.75D, (0.5F + width));
    }

    public static int getRandomSubtypeForBiome(Random random, BiomeGenBase biome) {
        if (random.nextInt(2) == 0) {
            return 7;
        }
        int num_candidates = 0;
        for (int i = 0; i < FlowerCollection.TYPE_NUMBER; i++) {
            if (Blocks.flowers.pick(i).isBiomeSuitable(biome, i)) {
                candidates[num_candidates++] = i;
            }
        }
        return (num_candidates == 0) ? -1 : candidates[random.nextInt(num_candidates)];
    }

    public static int getRandomSubtypeThatCanOccurAt(World world, int x, int y, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        int subtype = getRandomSubtypeForBiome(world.rand, biome);
        if (subtype < 0) {
            return -1;
        }
        while (!Blocks.flowers.pick(subtype).canOccurAt(world, x, y, z, subtype)) {
            subtype = getRandomSubtypeForBiome(world.rand, biome);
            if (subtype < 0) {
                return -1;
            }
        }
        return subtype;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean isBiomeSuitable(BiomeGenBase biome, int metadata) {
        if (biome.isSwampBiome()) return false;
        if (this == Blocks.flowers.luminescent_herb() && biome.temperature < BiomeGenBase.plains.temperature) {
            return false;
        } else if (this == Blocks.flowers.agave() && biome.temperature < BiomeGenBase.icePlains.temperature) {
            return false;
        } else if (biome.temperature < BiomeGenBase.forestHills.temperature) {
            return false;
        }
        if (biome.isJungleBiome()) {
            return false;
        }
        return true;
    }

    @Override
    public boolean canOccurAt(World world, int x, int y, int z, int metadata) {
        return (isBiomeSuitable(world.getBiomeGenForCoords(x, z), metadata) && super.canOccurAt(world, x, y, z, metadata));
    }

    @Override
    public boolean isLegalAt(World world, int x, int y, int z, int metadata) {
        return (isBiomeSuitable(world.getBiomeGenForCoords(x, z), metadata) && super.isLegalAt(world, x, y, z, metadata));
    }

}
