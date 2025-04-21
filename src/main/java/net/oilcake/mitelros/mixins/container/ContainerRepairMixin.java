package net.oilcake.mitelros.mixins.container;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.api.AnvilStatus;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.feat.AnvilSystem;
import net.oilcake.mitelros.mixin.interfaces.ITFContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerRepair.class)
public abstract class ContainerRepairMixin extends Container implements ITFContainerRepair {
    @Shadow
    private IInventory inputSlots;
    @Unique
    private int xpDifference;
    @Unique
    private AnvilStatus anvilStatus = AnvilStatus.EnchantmentConflict;

    @Override
    public int itf$GetXPDifference() {
        return this.xpDifference;
    }

    @Override
    public AnvilStatus itf$GetAnvilStatus() {
        return this.anvilStatus;
    }

    public ContainerRepairMixin(EntityPlayer player) {
        super(player);
    }

    @WrapOperation(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;isEnchantable()Z"))
    private boolean allowEnchantedItem(ItemStack instance, Operation<Boolean> original) {
        return original.call(instance) || (ITFConfig.ITFAnvilSystem.getBooleanValue() && instance.isItemEnchanted());
    }

    @WrapOperation(method = "updateRepairOutput", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/ItemStack;isItemEnchanted()Z"))
    public boolean allowEnchantedItem1(ItemStack instance, Operation<Boolean> original) {
        if (ITFConfig.ITFAnvilSystem.getBooleanValue()) {
            return false;
        }
        return original.call(instance);
    }

    @ModifyExpressionValue(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;hasValidEnchantmentForItem(Lnet/minecraft/NBTTagList;Lnet/minecraft/Item;)Z"))
    private boolean itfValidEnchantment(boolean original, @Local NBTTagList enchantmentsOfBook) {
        if (!ITFConfig.ITFAnvilSystem.getBooleanValue()) return original;
        if (!original) {
            this.anvilStatus = AnvilStatus.NoAvailableEnchantment;
            return false;
        }
        int xpDifference = AnvilSystem.calcXPDiffOnEnchanting(this.inputSlots.getStackInSlot(0), enchantmentsOfBook);
        this.xpDifference = xpDifference;// TODO this always >0?
        boolean needsXP = xpDifference < 0;
        boolean rewardsXP = xpDifference > 0;
        if (rewardsXP) {
            this.anvilStatus = AnvilStatus.Satisfied;
            return true;
        } else if (needsXP) {
            boolean enoughXP = this.player.experience + xpDifference >= 0;
            this.anvilStatus = enoughXP ? AnvilStatus.Satisfied : AnvilStatus.LackExp;
            return enoughXP;
        } else {
            this.anvilStatus = AnvilStatus.EnchantmentConflict;
            return false;
        }
    }

    @ModifyExpressionValue(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/Enchantment;canEnchantItem(Lnet/minecraft/Item;)Z"))
    private boolean checkConflict(boolean original, @Local Enchantment enchantment) {
        if (ITFConfig.ITFAnvilSystem.getBooleanValue()) {
            return original && AnvilSystem.canApplyTogether(EnchantmentHelper.getEnchantmentsMap(this.inputSlots.getStackInSlot(0)), enchantment);
        }
        return original;
    }

    @Inject(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;clearEnchantTagList()V"))
    private void rewardXP(CallbackInfo ci) {
        if (ITFConfig.ITFAnvilSystem.getBooleanValue()) {
            this.xpDifference = AnvilSystem.calcXPDiffOnDisenchanting(this.inputSlots.getStackInSlot(0));
        }
    }

}
