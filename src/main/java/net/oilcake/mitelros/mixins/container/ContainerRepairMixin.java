package net.oilcake.mitelros.mixins.container;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.oilcake.mitelros.feat.anvil.AnvilStatus;
import net.oilcake.mitelros.feat.anvil.AnvilSystem;
import net.oilcake.mitelros.mixin.interfaces.ITFContainerRepair;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.S2CAnvilData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerRepair.class)
public abstract class ContainerRepairMixin extends Container implements ITFContainerRepair {
    @Shadow
    private IInventory inputSlots;
    @Unique
    private int itf$xpDifference;
    @Unique
    private AnvilStatus itf$anvilStatus = AnvilStatus.EnchantmentConflict;
    @Unique
    private boolean itf$hasSyncedAnvilData;
    @Unique
    private boolean itf$syncedAnvilSystemActive;
    @Unique
    private int itf$syncedXPDifference;
    @Unique
    private AnvilStatus itf$syncedAnvilStatus = AnvilStatus.EnchantmentConflict;

    @Override
    public boolean itf$isAnvilSystemActive() {
        return this.world.isRemote && this.itf$hasSyncedAnvilData ? this.itf$syncedAnvilSystemActive : AnvilSystem.isActive();
    }

    @Override
    public int itf$getXPDifference() {
        return this.world.isRemote && this.itf$hasSyncedAnvilData ? this.itf$syncedXPDifference : this.itf$xpDifference;
    }

    @Override
    public AnvilStatus itf$getAnvilStatus() {
        return this.world.isRemote && this.itf$hasSyncedAnvilData ? this.itf$syncedAnvilStatus : this.itf$anvilStatus;
    }

    @Override
    public void itf$setAnvilData(boolean active, int xpDifference, AnvilStatus anvilStatus) {
        this.itf$hasSyncedAnvilData = true;
        this.itf$syncedAnvilSystemActive = active;
        this.itf$syncedXPDifference = xpDifference;
        this.itf$syncedAnvilStatus = anvilStatus;
    }

    @Override
    public void itf$onTakeOutput(EntityPlayer player) {
        if (!AnvilSystem.isActive()) return;
        if (this.world.isRemote) return;
        int xpDifference = this.itf$xpDifference;
        if (xpDifference == 0) return;
        player.addExperience(xpDifference);
        this.itf$xpDifference = 0;
    }

    @Inject(method = "transferStackInSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/Slot;onSlotChange(Lnet/minecraft/ItemStack;Lnet/minecraft/ItemStack;)V"))
    private void itf$onShiftClickOutput(EntityPlayer par1EntityPlayer, int par2, CallbackInfoReturnable<ItemStack> cir) {
        if (par2 == ContainerRepair.SLOT_INDEX_OUTPUT) {
            this.itf$onTakeOutput(par1EntityPlayer);
        }
    }

    public ContainerRepairMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(method = "updateRepairOutput", at = @At("HEAD"))
    private void resetAnvilStatus(CallbackInfo ci) {
        if (AnvilSystem.isActive()) {
            this.itf$xpDifference = 0;
            this.itf$anvilStatus = AnvilStatus.EnchantmentConflict;
        }
    }

    @Inject(method = "updateRepairOutput", at = @At("RETURN"))
    private void itf$syncAnvilData(CallbackInfo ci) {
        if (this.world.isRemote) return;
        boolean active = AnvilSystem.isActive();
        if (this.itf$hasSyncedAnvilData
                && this.itf$syncedAnvilSystemActive == active
                && this.itf$syncedXPDifference == this.itf$xpDifference
                && this.itf$syncedAnvilStatus == this.itf$anvilStatus) {
            return;
        }
        ITFNetwork.sendToClient((ServerPlayer) this.player,
                new S2CAnvilData(this.windowId, active, this.itf$xpDifference, this.itf$anvilStatus));
        this.itf$hasSyncedAnvilData = true;
        this.itf$syncedAnvilSystemActive = active;
        this.itf$syncedXPDifference = this.itf$xpDifference;
        this.itf$syncedAnvilStatus = this.itf$anvilStatus;
    }

    @WrapOperation(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;isEnchantable()Z"))
    private boolean allowEnchantedItem(ItemStack instance, Operation<Boolean> original) {
        return original.call(instance) || (AnvilSystem.isActive() && instance.isItemEnchanted());
    }

    @WrapOperation(method = "updateRepairOutput", at = @At(ordinal = 0, value = "INVOKE", target = "Lnet/minecraft/ItemStack;isItemEnchanted()Z"))
    public boolean allowEnchantedItem1(ItemStack instance, Operation<Boolean> original) {
        if (AnvilSystem.isActive()) {
            return false;
        }
        return original.call(instance);
    }

    @ModifyExpressionValue(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;hasValidEnchantmentForItem(Lnet/minecraft/NBTTagList;Lnet/minecraft/Item;)Z"))
    private boolean itfValidEnchantment(boolean original, @Local NBTTagList enchantmentsOfBook) {
        if (!AnvilSystem.isActive()) return original;
        if (!original) {
            this.itf$anvilStatus = AnvilStatus.NoAvailableEnchantment;
            return false;
        }
        int xpDifference = AnvilSystem.calcXPDiffOnEnchanting(this.inputSlots.getStackInSlot(0), enchantmentsOfBook);
        this.itf$xpDifference = xpDifference;
        boolean needsXP = xpDifference < 0;
        boolean rewardsXP = xpDifference > 0;
        if (rewardsXP) {
            this.itf$anvilStatus = AnvilStatus.Satisfied;
            return true;
        } else if (needsXP) {
            boolean enoughXP = this.player.experience + xpDifference >= 0;
            this.itf$anvilStatus = enoughXP ? AnvilStatus.Satisfied : AnvilStatus.LackExp;
            return enoughXP;
        } else {
            this.itf$anvilStatus = AnvilStatus.EnchantmentConflict;
            return false;
        }
    }

    @ModifyExpressionValue(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/Enchantment;canEnchantItem(Lnet/minecraft/Item;)Z"))
    private boolean checkConflict(boolean original, @Local Enchantment enchantment) {
        if (AnvilSystem.isActive()) {
            return original && AnvilSystem.canApplyTogether(EnchantmentHelper.getEnchantmentsMap(this.inputSlots.getStackInSlot(0)), enchantment);
        }
        return original;
    }

    @Inject(method = "updateRepairOutput", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;clearEnchantTagList()V"))
    private void rewardXP(CallbackInfo ci) {
        if (AnvilSystem.isActive()) {
            this.itf$xpDifference = AnvilSystem.calcXPDiffOnDisenchanting(this.inputSlots.getStackInSlot(0));
            this.itf$anvilStatus = AnvilStatus.Satisfied;
        }
    }

}
