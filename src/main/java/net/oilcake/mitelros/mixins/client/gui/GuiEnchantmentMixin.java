package net.oilcake.mitelros.mixins.client.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.Container;
import net.minecraft.ContainerEnchantment;
import net.minecraft.EnumChatFormatting;
import net.minecraft.FontRenderer;
import net.minecraft.GuiContainer;
import net.minecraft.GuiEnchantment;
import net.minecraft.ItemStack;
import net.minecraft.ScaledResolution;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.feat.EnchantPrediction;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.mixin.interfaces.ITFGuiEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(GuiEnchantment.class)
public abstract class GuiEnchantmentMixin extends GuiContainer implements ITFGuiEnchantment {
    @Shadow
    private ContainerEnchantment containerEnchantment;

    @Shadow
    ItemStack theItemStack;

    @Unique
    private int[] info;

    public GuiEnchantmentMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 1, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void itf$drawPrediction(float par1, int par2, int par3, CallbackInfo ci, int left, int top, ScaledResolution scaledResolution, float var7, float var8, float var9, float var10, float var11, List tooltips, int line, String tooltip, int var14, int experienceCost, String var15, FontRenderer fontRenderer, int var17, int var18, int var19) {
        if (ModReference.hasMod(ModReference.ENCHANT_DIVINE) || this.info == null || line >= 3) return;
        String prediction = EnchantPrediction.readInfo(this.info, line);
        if (prediction == null) return;
        fontRenderer.drawStringWithShadow(EnumChatFormatting.AQUA + prediction, left + 62, top + 16 + 19 * line, EnumChatFormatting.AQUA.rgb);
    }

    @WrapWithCondition(method = "drawGuiContainerBackgroundLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawSplitString(Ljava/lang/String;IIII)V", ordinal = 1))
    private boolean itf$hideGalacticText(FontRenderer instance, String par1Str, int par2, int par3, int par4, int par5) {
        return ModReference.hasMod(ModReference.ENCHANT_DIVINE) || this.info == null;
    }

    @Override
    public void itf$SetEnchantmentSeed(long seed) {
        if (this.theItemStack == null) {
            this.info = null;
            return;
        }
        boolean extended = this.theItemStack.getMaterialForRepairs() == Materials.uru;
        this.info = EnchantPrediction.predict(seed, this.theItemStack, this.containerEnchantment.enchantLevels, extended);
    }

    @Override
    public boolean itf$HasEnchantmentInfo() {
        if (this.info == null || !this.inventorySlots.getSlot(0).getHasStack()) return false;
        for (int level : this.containerEnchantment.enchantLevels) {
            if (level > 0) return true;
        }
        return false;
    }
}
