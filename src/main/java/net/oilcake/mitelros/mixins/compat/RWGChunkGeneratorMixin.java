package net.oilcake.mitelros.mixins.compat;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.IChunkProvider;
import net.minecraft.World;
import net.minecraft.WorldGenerator;
import net.oilcake.mitelros.Reference;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.world.WorldGenFlowersExtend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import rwg.world.ChunkGeneratorRealistic;

import java.util.Random;

@Restriction(require = @Condition(Reference.RWG))
@Mixin(ChunkGeneratorRealistic.class)
public class RWGChunkGeneratorMixin {
    @Shadow
    private Random rand;
    @Shadow
    private World worldObj;
    @Unique
    private final WorldGenerator flowerExtend = new WorldGenFlowersExtend(Blocks.flowerextend.blockID);

    @Inject(method = "populate", at = @At(value = "FIELD", target = "Lrwg/config/ConfigRWG;generateEmeralds:Z"))
    private void addFlowerGen(IChunkProvider ichunkprovider, int i, int j, CallbackInfo ci) {
        Random random = this.rand;
        int blockX = i * 16;
        int blockZ = j * 16;
        for (int i1 = 0; i1 < 2; i1++) {
            int randomX = blockX + random.nextInt(16) + 8;
            int randomZ = blockZ + random.nextInt(16) + 8;
            int y = random.nextInt(128);
            this.flowerExtend.generate(this.worldObj, random, randomX, y, randomZ);
        }
    }
}
