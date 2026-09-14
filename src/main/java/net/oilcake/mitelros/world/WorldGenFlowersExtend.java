package net.oilcake.mitelros.world;

import net.minecraft.World;
import net.minecraft.WorldGenerator;
import net.oilcake.mitelros.block.BlockFlowerExtend;
import net.oilcake.mitelros.block.FlowerCollection;

import java.util.Random;

public class WorldGenFlowersExtend extends WorldGenerator {
    private final FlowerCollection<BlockFlowerExtend> flowers;

    public WorldGenFlowersExtend(FlowerCollection<BlockFlowerExtend> flowers) {
        this.flowers = flowers;
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        BlockFlowerExtend flower = this.flowers.pick((par2Random.nextInt(8)));
        int attempts = 64;
        for (int var6 = 0; var6 < attempts; var6++) {
            int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
            if (var8 >= 0 && var8 <= 255) {
                int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
                int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
                if (par1World.isAirBlock(var7, var8, var9) && (!par1World.provider.hasNoSky || var8 < 127) && flower.canOccurAt(par1World, var7, var8, var9, 0))
                    par1World.setBlock(var7, var8, var9, flower.blockID, 0, 2);
            }
        }
        return true;
    }
}
