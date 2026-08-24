package net.oilcake.mitelros.mixins.block;

import net.minecraft.BiomeGenBase;
import net.minecraft.BlockPlant;
import net.minecraft.BlockSapling;
import net.oilcake.mitelros.world.ITFBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockSapling.class)
public abstract class BlockSaplingMixin extends BlockPlant {

    protected BlockSaplingMixin(int blockId) {
        super(blockId);
    }

    @Inject(method = "canGrowInBiome", at = @At("HEAD"), cancellable = true)
    private static void allowDryITFBiomes(int subtype, BiomeGenBase biome, CallbackInfoReturnable<Boolean> cir) {
        if (biome != ITFBiomes.BIOME_SAVANNA && biome != ITFBiomes.BIOME_SAVANNA_PLEATU) return;

        if (subtype == BlockSapling.OAK_TREE) {
            cir.setReturnValue(biome.temperature >= 0.4F);
        } else if (subtype == BlockSapling.BIRCH_TREE) {
            cir.setReturnValue(biome.temperature >= 0.5F);
        } else if (subtype == BlockSapling.JUNGLE_TREE) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(true);
        }
    }
}
