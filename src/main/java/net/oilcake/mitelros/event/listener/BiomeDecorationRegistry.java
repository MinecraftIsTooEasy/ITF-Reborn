package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.BiomeDecorationRegisterEvent;
import moddedmite.rustedironcore.api.event.handler.BiomeDecorationHandler;
import moddedmite.rustedironcore.api.world.Dimension;
import net.minecraft.BiomeGenBase;
import net.minecraft.BiomeGenDesert;
import net.minecraft.BiomeGenSnow;
import net.minecraft.BiomeGenTaiga;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.world.ITFBiomes;
import net.oilcake.mitelros.world.WorldGenEmerald;
import net.oilcake.mitelros.world.WorldGenFlowersExtend;
import net.oilcake.mitelros.world.WorldGenSulphur;

import java.util.function.Consumer;

public class BiomeDecorationRegistry implements Consumer<BiomeDecorationRegisterEvent> {
    @Override
    public void accept(BiomeDecorationRegisterEvent event) {
        event.register(Dimension.NETHER, new WorldGenSulphur())
                .setChance(256)
                .setHeightSupplier(BiomeDecorationHandler.HeightSupplier.SURFACE);

        event.register(Dimension.OVERWORLD, new WorldGenFlowersExtend(Blocks.flowerextend.blockID))
                .setFrequency(context -> getFlowerFrequency(context.biome()));

        event.register(Dimension.OVERWORLD, new WorldGenEmerald())
                .setFrequency(BiomeDecorationRegistry::getEmeraldFrequency)
                .setHeightSupplier((context, x1, z1) -> 4 + context.rand().nextInt(28));

        event.register(Dimension.OVERWORLD, new WorldGenSulphur())
                .setFrequency(BiomeDecorationRegistry::getSulphurFrequency)
//                .setChance(144)
//                .requiresBiome(biome -> biome instanceof BiomeGenDesert)
                .setHeightSupplier((ctx, x, z) -> ctx.world().getHeightValue(x, z) + 1);
    }

    private static int getEmeraldFrequency(BiomeDecorationHandler.Context context) {
        BiomeGenBase biome = context.biome();
        if (biome instanceof BiomeGenSnow || biome instanceof BiomeGenTaiga) {
            return 3 + context.rand().nextInt(6);
        }
        return 0;
    }

    private static int getFlowerFrequency(BiomeGenBase biome) {
        if (biome == ITFBiomes.BIOME_SAVANNA) return 1;
        if (biome == ITFBiomes.BIOME_SAVANNA_PLEATU) return 1;
        if (biome == ITFBiomes.BIOME_WINDSWEPT_PLEATU) return 0;
        return 2;
    }

    private static int getSulphurFrequency(BiomeDecorationHandler.Context context) {
        if (context.biome() instanceof BiomeGenDesert) {
            return context.rand().nextInt(144) == 0 ? 1 : 0;
        }
        return 0;
    }
}
