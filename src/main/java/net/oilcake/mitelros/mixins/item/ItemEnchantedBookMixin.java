package net.oilcake.mitelros.mixins.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.feat.MultiEnchantedBook;
import net.oilcake.mitelros.feat.anvil.AnvilSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Random;

@Mixin(ItemEnchantedBook.class)
public abstract class ItemEnchantedBookMixin extends Item {
    @Shadow
    public abstract void addEnchantment(ItemStack par1ItemStack, EnchantmentData par2EnchantmentData);

    @WrapOperation(method = "func_92112_a", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemEnchantedBook;addEnchantment(Lnet/minecraft/ItemStack;Lnet/minecraft/EnchantmentData;)V"))
    private void addMoreEnchantments(ItemEnchantedBook instance, ItemStack var5, EnchantmentData var7, Operation<Void> original, @Local(argsOnly = true) Random par1Random) {
        if (AnvilSystem.isActive()) {
            List<EnchantmentData> var4 = MultiEnchantedBook.buildEnchantmentList(par1Random, 75);
            if (var4 != null) {
                for (EnchantmentData var8 : var4) {
                    this.addEnchantment(var5, var8);
                }
            }
        } else {
            original.call(instance, var5, var7);
        }
    }

    @ModifyReturnValue(method = "func_92112_a", at = @At("RETURN"))
    private WeightedRandomChestContent modifyWeight(WeightedRandomChestContent original) {
        if (AnvilSystem.isActive()) {
            original.itemWeight *= 2;
        }
        return original;
    }

}
