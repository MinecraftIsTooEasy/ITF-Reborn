package net.oilcake.mitelros.mixins.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.block.BlockUruBeacon;
import net.oilcake.mitelros.client.render.PureWaterIconProvider;
import net.oilcake.mitelros.util.WaterHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow
    public IBlockAccess blockAccess;

    @Unique
    private int itf$pureWaterBlendLevel;

    @ModifyArg(method = "renderBlockAsItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderBlocks;getBlockIcon(Lnet/minecraft/Block;)Lnet/minecraft/Icon;", ordinal = 0))
    private Block uruBeacon(Block par1Block, @Local(argsOnly = true) Block blockInArg) {
        return blockInArg instanceof BlockUruBeacon ? Block.blockMithril : par1Block;
    }

    @ModifyArg(method = "renderBlockBeacon", at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderBlocks;getBlockIcon(Lnet/minecraft/Block;)Lnet/minecraft/Icon;", ordinal = 1))
    private Block uruBeacon_1(Block par1Block, @Local(argsOnly = true) BlockBeacon par1BlockBeacon) {
        return par1BlockBeacon instanceof BlockUruBeacon ? Block.blockMithril : par1Block;
    }

    @Inject(method = "renderBlockFluids(Lnet/minecraft/Block;III)Z", at = @At("HEAD"))
    private void itf$selectPureWaterTexture(Block par1Block, int par2, int par3, int par4, CallbackInfoReturnable<Boolean> cir) {
        this.itf$pureWaterBlendLevel = WaterHelper.isWater(par1Block) ? WaterHelper.getPureWaterBlendLevel(this.blockAccess, par2, par4) : 0;
    }

    @Redirect(method = "renderBlockFluids(Lnet/minecraft/Block;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderBlocks;getBlockIconFromSideAndMetadata(Lnet/minecraft/Block;II)Lnet/minecraft/Icon;"))
    private Icon itf$usePureWaterIconWithMetadata(RenderBlocks renderer, Block par1Block, int par2, int par3) {
        Icon icon = this.itf$getPureWaterIcon(par1Block, par2);
        return icon == null ? renderer.getBlockIconFromSideAndMetadata(par1Block, par2, par3) : icon;
    }

    @Redirect(method = "renderBlockFluids(Lnet/minecraft/Block;III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/RenderBlocks;getBlockIconFromSide(Lnet/minecraft/Block;I)Lnet/minecraft/Icon;"))
    private Icon itf$usePureWaterIcon(RenderBlocks renderer, Block par1Block, int par2) {
        Icon icon = this.itf$getPureWaterIcon(par1Block, par2);
        return icon == null ? renderer.getBlockIconFromSide(par1Block, par2) : icon;
    }

    @Unique
    private Icon itf$getPureWaterIcon(Block block, int side) {
        if (this.itf$pureWaterBlendLevel == 0 || !(block instanceof PureWaterIconProvider provider)) return null;
        return provider.itf$getPureWaterIcon(side, this.itf$pureWaterBlendLevel);
    }
}
