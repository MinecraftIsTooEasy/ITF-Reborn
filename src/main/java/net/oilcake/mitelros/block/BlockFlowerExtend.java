package net.oilcake.mitelros.block;

import moddedmite.rustedironcore.api.util.FabricUtil;
import net.minecraft.*;
import net.oilcake.mitelros.registry.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class BlockFlowerExtend extends BlockFlower {
    private final String registryName;

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

    @Nullable
    public static Block getRandomTypeForBiome(Random random, BiomeGenBase biome) {
        if (random.nextInt(2) == 0) {
            return Blocks.flowers.agave();
        }

        List<BlockFlowerExtend> candidates = Blocks.flowers.stream().filter(
                x -> x.isBiomeSuitable(biome, 0)
        ).toList();

        return (candidates.isEmpty()) ? null : candidates.get(random.nextInt(candidates.size()));
    }

    @Nullable
    public static Block getRandomTypeThatCanOccurAt(World world, int x, int y, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        Block block = getRandomTypeForBiome(world.rand, biome);
        if (block == null) {
            return null;
        }
        while (!block.canOccurAt(world, x, y, z, 0)) {
            block = getRandomTypeForBiome(world.rand, biome);
            if (block == null) {
                return null;
            }
        }
        return block;
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
