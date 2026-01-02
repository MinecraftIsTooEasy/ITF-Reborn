package net.oilcake.mitelros.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.BlockConstants;
import net.minecraft.BlockDirectionalWithTileEntity;
import net.minecraft.BlockFurnace;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFurnace.class)
public abstract class BlockFurnaceMixin extends BlockDirectionalWithTileEntity {
    protected BlockFurnaceMixin(int id, Material material, BlockConstants constants) {
        super(id, material, constants);
    }

    @ModifyReturnValue(method = "getCraftingDifficultyAsComponent", at = @At("RETURN"))
    private float setDifficulty(float original) {
        return 1920.0F;
    }
}
