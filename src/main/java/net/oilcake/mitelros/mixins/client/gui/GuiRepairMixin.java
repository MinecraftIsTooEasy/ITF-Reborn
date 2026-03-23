package net.oilcake.mitelros.mixins.client.gui;

import net.minecraft.*;
import net.oilcake.mitelros.feat.anvil.AnvilStatus;
import net.oilcake.mitelros.feat.anvil.AnvilSystem;
import net.oilcake.mitelros.mixin.interfaces.ITFContainerRepair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRepair.class)
public abstract class GuiRepairMixin extends GuiContainer {
    @Shadow
    private ContainerRepair repairContainer;

    public GuiRepairMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawScreen", at = @At("TAIL"))
    private void itfRepair(int mouse_x, int mouse_y, float par3, CallbackInfo ci) {
        if (!AnvilSystem.isActive()) return;
        ItemStack itemStack = this.repairContainer.getSlot(1).getStack();
        if (itemStack == null) return;
        if (itemStack.itemID != Item.enchantedBook.itemID && itemStack.itemID != Item.bottleOfDisenchanting.itemID)
            return;
        if (this.isMouseOverSlot(this.repairContainer.getSlot(2), mouse_x, mouse_y)) {
            AnvilStatus anvilStatus = ((ITFContainerRepair) this.repairContainer).itf$getAnvilStatus();
            String text = switch (anvilStatus) {
                case NoAvailableEnchantment -> I18n.getString("gui.repair.no_available_enchantment");
                case EnchantmentConflict -> I18n.getString("gui.repair.enchantment_conflict");
                case Satisfied ->
                        AnvilSystem.getString(this.repairContainer.player, ((ITFContainerRepair) this.repairContainer).itf$getXPDifference());
                case LackExp ->
                        I18n.getStringParams("gui.repair.lack_xp", this.repairContainer.player.getExperienceLevel(-((ITFContainerRepair) this.repairContainer).itf$getXPDifference()));
            };
            this.drawCreativeTabHoveringText(text, mouse_x, mouse_y);
        }
    }
}
