package net.oilcake.mitelros.mixins.client.render;

import net.minecraft.RenderGlobal;
import net.minecraft.Vec3;
import net.minecraft.ICamera;
import net.oilcake.mitelros.client.render.TotemFlatteningRangeRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public abstract class RenderGlobalMixin {

    @Inject(method = "renderEntities", at = @At("TAIL"))
    private void itf$renderTotemFlatteningRange(Vec3 par1Vec3, ICamera par2ICamera, float par3, CallbackInfo ci) {
        TotemFlatteningRangeRenderer.render();
    }
}
