package net.oilcake.mitelros.mixins.client.gui;

import net.minecraft.Container;
import net.minecraft.EnumChatFormatting;
import net.minecraft.GuiContainer;
import net.minecraft.GuiEnchantment;
import net.oilcake.mitelros.feat.EnchantPrediction;
import net.oilcake.mitelros.mixin.interfaces.ITFGuiEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiEnchantment.class)
public abstract class GuiEnchantmentMixin extends GuiContainer implements ITFGuiEnchantment {
    @Unique
    private int[] info = null;

    public GuiEnchantmentMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawGuiContainerBackgroundLayer", at = @At("TAIL"))
    private void addInfo(float par1, int par2, int par3, CallbackInfo ci) {
        if (this.info == null) return;
        if (!this.inventorySlots.getSlot(0).getHasStack()) return;
        for (int line = 0; line < 3; line++) {
            String var13 = EnchantPrediction.readInfo(this.info, line);
            if (var13 == null) continue;
            var13 += "...?";
            int var4 = (this.width - this.xSize) / 2;
            int var5 = (this.height - this.ySize) / 2;
            int var18 = par2 - (var4 + 60);
            int var19 = par3 - (var5 + 14 + 19 * line);
            if (var18 >= 0 && var19 >= 0 && var18 < 108 && var19 < 19) {
                this.drawCreativeTabHoveringText(EnumChatFormatting.AQUA + var13, par2, par3);
            }
        }
    }

    @Override
    public void itf$SetEnchantmentInfo(int[] info) {
        this.info = info;
    }
}
