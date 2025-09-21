package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.BiomeDecorationRegisterEvent;
import moddedmite.rustedironcore.api.event.handler.BiomeDecorationHandler;
import moddedmite.rustedironcore.api.world.Dimension;
import net.minecraft.BiomeGenBase;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.world.ITFBiomes;
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
    }

    private static int getFlowerFrequency(BiomeGenBase biome) {
        if (biome == ITFBiomes.BIOME_SAVANNA) return 1;
        if (biome == ITFBiomes.BIOME_SAVANNA_PLEATU) return 1;
        if (biome == ITFBiomes.BIOME_WINDSWEPT_PLEATU) return 0;
        return 2;
    }
}
