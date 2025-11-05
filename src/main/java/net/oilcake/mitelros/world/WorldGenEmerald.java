package net.oilcake.mitelros.world;

import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldGenerator;

import java.util.Random;

public class WorldGenEmerald extends WorldGenerator {
    @Override
    public boolean generate(World world, Random random, int i, int j, int k) {
        int var10 = world.getBlockId(i, j, k);
        if (var10 == Block.stone.blockID) {
            world.setBlock(i, j, k, Block.oreEmerald.blockID, 0, 2);
        }
        return true;
    }
}
