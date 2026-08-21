package net.oilcake.mitelros.mixins.block;

import net.minecraft.Block;
import net.minecraft.BlockFluid;
import net.minecraft.IBlockAccess;
import net.minecraft.Icon;
import net.minecraft.IconRegister;
import net.oilcake.mitelros.ITFStart;
import net.oilcake.mitelros.client.render.PureWaterIconProvider;
import net.oilcake.mitelros.util.WaterHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFluid.class)
public abstract class BlockFluidMixin implements PureWaterIconProvider {

    @Unique
    private final Icon[] itf$pureWaterStillIcons = new Icon[WaterHelper.PURE_WATER_BLEND_LEVELS];

    @Unique
    private final Icon[] itf$pureWaterFlowIcons = new Icon[WaterHelper.PURE_WATER_BLEND_LEVELS];

    @Inject(method = "colorMultiplier", at = @At("RETURN"), cancellable = true)
    private void itf$colorPureWater(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, CallbackInfoReturnable<Integer> cir) {
        if (WaterHelper.isWater((Block) (Object) this) && WaterHelper.getPureWaterBlendLevel(par1IBlockAccess, par2, par4) > 0) {
            cir.setReturnValue(0xFFFFFF);
        }
    }

    @Inject(method = "registerIcons", at = @At("RETURN"))
    private void itf$registerPureWaterIcons(IconRegister par1IconRegister, CallbackInfo ci) {
        if (!WaterHelper.isWater((Block) (Object) this)) return;

        for (int level = 1; level <= WaterHelper.PURE_WATER_BLEND_LEVELS; level++) {
            this.itf$pureWaterStillIcons[level - 1] = par1IconRegister.registerIcon(
                    ITFStart.ResourceDomainColon + "water/pure_water_still_" + level
            );
            this.itf$pureWaterFlowIcons[level - 1] = par1IconRegister.registerIcon(
                    ITFStart.ResourceDomainColon + "water/pure_water_flow_" + level
            );
        }
    }

    @Override
    @Unique
    public Icon itf$getPureWaterIcon(int side, int blendLevel) {
        if (blendLevel < 1 || blendLevel > WaterHelper.PURE_WATER_BLEND_LEVELS) return null;
        return side == 0 || side == 1 ? this.itf$pureWaterStillIcons[blendLevel - 1] : this.itf$pureWaterFlowIcons[blendLevel - 1];
    }
}
