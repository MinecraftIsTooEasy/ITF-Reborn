package net.oilcake.mitelros.mixins.container;

import net.minecraft.*;
import net.oilcake.mitelros.mixin.interfaces.ITFContainerRepair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerRepairINNER2.class)
public abstract class ContainerRepairINNER2Mixin extends Slot {
    @Shadow
    @Final
    ContainerRepair repairContainer;

    public ContainerRepairINNER2Mixin(IInventory inventory, int slot_index, int display_x, int display_y) {
        super(inventory, slot_index, display_x, display_y);
    }

    @Inject(method = "onPickupFromSlot", at = @At("HEAD"))
    private void modifyXP(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        ((ITFContainerRepair) this.repairContainer).itf$onTakeOutput(player, stack);
    }
}
