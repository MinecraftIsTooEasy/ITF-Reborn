package net.oilcake.mitelros.mixins.entity.misc;

import net.minecraft.*;
import net.oilcake.mitelros.registry.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends EntityLivingBase {
    public EntityLivingMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "tryDisableNearbyLightSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/MathHelper;floor_double(D)I", ordinal = 0), cancellable = true)
    private void itfTorches(CallbackInfoReturnable<Boolean> cir) {
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.posY);
        int z = MathHelper.floor_double(this.posZ);
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1 + (int) this.height; ++dy) {
                for (int dz = -1; dz <= 1; ++dz) {
                    int block_id = this.worldObj.getBlockId(x + dx, y + dy, z + dz);

                    Block toReplace = null;
                    if (block_id == Block.torchWood.blockID || block_id == Blocks.torchWoodIdle.blockID) {
                        toReplace = Blocks.torchWoodExtinguished;
                    } else if (block_id == Block.torchRedstoneActive.blockID) {
                        toReplace = Block.torchRedstoneIdle;
                    }

                    if (toReplace != null && this.worldObj.setBlock(x + dx, y + dy, z + dz, toReplace.blockID, this.worldObj.getBlockMetadata(x + dx, y + dy, z + dz), 2)) {
                        this.playSound("random.fizz", 0.5F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F));
                        cir.setReturnValue(true);
                        return;
                    }
                }
            }
        }
    }
}
