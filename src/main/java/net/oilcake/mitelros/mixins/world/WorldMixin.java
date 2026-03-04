package net.oilcake.mitelros.mixins.world;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.BiomeGenBase;
import net.minecraft.Entity;
import net.minecraft.Explosion;
import net.minecraft.World;
import net.oilcake.mitelros.feat.EnumSeason;
import net.oilcake.mitelros.feat.EternalRaining;
import net.oilcake.mitelros.mixin.interfaces.ITFWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements ITFWorld {
    @Shadow
    public abstract World getWorld();

    public Explosion itf$ExplosionC(Entity exploder, double posX, double posY, double posZ, float explosion_size_vs_blocks, float explosion_size_vs_living_entities, boolean b) {
        Explosion explosion = new Explosion(this.getWorld(), exploder, posX, posY, posZ, explosion_size_vs_blocks, explosion_size_vs_living_entities);
        explosion.doExplosionA();
        explosion.affectedBlockPositions.clear();
        explosion.doExplosionB(false);
        return explosion;
    }

    @ModifyExpressionValue(method = "generateWeatherEvents(I)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I", ordinal = 2))
    private int itfRain(int original) {
        return EternalRaining.modifyRain((World) (Object) this, original);
    }

    @Shadow
    @Final
    public abstract int getDayOfWorld();

    @Unique
    public EnumSeason itf$GetWorldSeason() {
        return EnumSeason.getForCode((this.getDayOfWorld() % 128) / 32);
    }

    @ModifyConstant(method = "canSnowAt", constant = @Constant(floatValue = 0.15F))
    private float itfSnow(float constant) {
        return (this.itf$GetWorldSeason() == EnumSeason.WINTER) ? 1.0F : 0.15F;
    }

    @Inject(method = "isFreezing", at = @At("HEAD"), cancellable = true)
    private void itfFreezing(int x, int z, CallbackInfoReturnable<Boolean> cir) {
        if (getBiomeGenForCoords(x, z).temperature <= ((itf$GetWorldSeason() == EnumSeason.WINTER) ? 1.0F : 0.15F))
            cir.setReturnValue(true);
    }

    @Shadow
    public BiomeGenBase getBiomeGenForCoords(int par1, int par3) {
        return null;
    }
}
