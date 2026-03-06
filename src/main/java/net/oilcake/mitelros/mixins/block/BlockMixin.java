package net.oilcake.mitelros.mixins.block;

import net.minecraft.*;
import net.oilcake.mitelros.block.BlockFlowerExtend;
import net.oilcake.mitelros.feat.OreMiningHooks;
import net.oilcake.mitelros.registry.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Inject(method = "<clinit>()V", at = @At("TAIL"))
    private static void injectClinit(CallbackInfo callback) {
        Item.itemsList[Blocks.flowerextend.blockID] = (new ItemMultiTextureTile(Blocks.flowerextend, BlockFlowerExtend.types)).setUnlocalizedName("flowers");
        Item.itemsList[Blocks.tungstenRuneStone.blockID] = new ItemRunestone(Blocks.tungstenRuneStone).setUnlocalizedName("runestone");
        Block.pumpkinLantern.setLightValue(0.9375f);
    }

    @Inject(
            method = "dropBlockAsItself",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/Block;dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;Lnet/minecraft/ItemStack;)I"
            ),
            cancellable = true
    )
    private void melting(BlockBreakInfo info, CallbackInfoReturnable<Integer> cir) {
        if (!(info.block instanceof BlockOre ore)) return;
        int i = OreMiningHooks.onSilkTouch(info, ore);
        if (i != -1) {
            cir.setReturnValue(i);
        }
    }
}
