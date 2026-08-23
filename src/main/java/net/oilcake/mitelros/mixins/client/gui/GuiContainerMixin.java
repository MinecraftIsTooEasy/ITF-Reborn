package net.oilcake.mitelros.mixins.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.GuiContainer;
import net.minecraft.GuiEnchantment;
import net.minecraft.GuiScreen;
import net.minecraft.ItemStack;
import net.minecraft.Slot;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.mixin.interfaces.ITFGuiEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiContainer.class)
public abstract class GuiContainerMixin extends GuiScreen {

    @WrapWithCondition(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiContainer;drawItemStackTooltip(Lnet/minecraft/ItemStack;IILnet/minecraft/Slot;)V"))
    private boolean itf$allowItemTooltip(GuiContainer instance, ItemStack par1ItemStack, int par2, int par3, Slot slot) {
        return !(instance instanceof GuiEnchantment)
                || !(instance instanceof ITFGuiEnchantment enchantmentGui)
                || instance.inventorySlots.getSlot(0) != slot
                || (!ModReference.hasMod(ModReference.ENCHANT_DIVINE) && !enchantmentGui.itf$HasEnchantmentInfo())
                || GuiScreen.isShiftKeyDown();
    }
}
