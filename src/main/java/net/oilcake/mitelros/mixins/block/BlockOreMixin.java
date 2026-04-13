package net.oilcake.mitelros.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.*;
import net.oilcake.mitelros.enchantment.Enchantments;
import net.oilcake.mitelros.feat.OreMiningHooks;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(BlockOre.class)
public abstract class BlockOreMixin extends Block {
    @Unique
    private final BlockOre instance = (BlockOre) (Object) this;

    protected BlockOreMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @WrapOperation(method = "dropBlockAsEntityItem", at = @At(value = "INVOKE", target = "Ljava/util/Random;nextInt(I)I"))
    private int preventLapisLazuiDroppingIfAbsorbed(Random instance, int i, Operation<Integer> original, @Local(argsOnly = true) BlockBreakInfo info) {
        if (EnchantmentHelper.hasEnchantment(info.responsible_item_stack, Enchantments.enchantmentAbsorb)) {
            return -3;
        }
        return original.call(instance, i);
    }

    @Inject(method = "dropBlockAsEntityItem", at = @At(value = "FIELD", target = "Lnet/minecraft/BlockOre;blockID:I", ordinal = 2))
    private void changeDrop1(BlockBreakInfo info, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) LocalIntRef id_dropped, @Local(ordinal = 2) LocalIntRef quantity_dropped) {
        OreMiningHooks.setIdMeta(info, instance, id_dropped, quantity_dropped);
    }

    @ModifyExpressionValue(method = "dropBlockAsEntityItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/BlockBreakInfo;getHarvesterFortune()I"))
    private int moreFortune(int original) {
        return original * 2;
    }

    @WrapOperation(method = "dropBlockAsEntityItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;IIIF)I"))
    private int modifyFinal(BlockOre instance,
                            BlockBreakInfo info,
                            int id,
                            int metadata,
                            int quantity,
                            float chance,
                            Operation<Integer> original,
                            @Local boolean suppress_fortune) {
        MutableInt mutableId = new MutableInt(id);
        MutableInt mutableMetadata = new MutableInt(metadata);
        OreMiningHooks.modifyFinalDropId(instance, mutableId, mutableMetadata, info, suppress_fortune);
        return original.call(instance, info, mutableId.intValue(), mutableMetadata.intValue(), quantity, chance);
    }

}
