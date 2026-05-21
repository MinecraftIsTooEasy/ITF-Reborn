package net.oilcake.mitelros.mixins.client.gui;

import moddedmite.rustedironcore.api.interfaces.IPotion;
import net.minecraft.*;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(value = InventoryEffectRenderer.class, priority = 2000)
public abstract class InventoryEffectRendererMixin extends GuiContainer {
    @Unique
    private static final ResourceLocation itf$MITE_ICONS = new ResourceLocation("textures/gui/MITE_icons.png");
    @Unique
    private static final ResourceLocation itf$SUGAR_ICON = new ResourceLocation("textures/items/sugar.png");
    @Unique
    private boolean itf$curseTooltipDrawnThisFrame;
    @Unique
    private boolean itf$statusEffectsDrawnThisFrame;

    public InventoryEffectRendererMixin(Container par1Container) {
        super(par1Container);
    }

    @Inject(method = "drawScreen", at = @At("HEAD"), cancellable = true)
    private void itf$beforeDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.itf$curseTooltipDrawnThisFrame = false;
        this.itf$statusEffectsDrawnThisFrame = false;
        if (this.itf$shouldForceDrawStatusEffects()) {
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.itf$drawStatusEffects();
            this.itf$statusEffectsDrawnThisFrame = true;
            this.itf$drawCurseTooltipAt(mouseX, mouseY);
            ci.cancel();
        }
    }

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/InventoryEffectRenderer;drawMalnourishedBoxTooltip(II)V", shift = At.Shift.BEFORE))
    private void itf$drawStatusEffectsBeforeTooltips(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.itf$shouldForceDrawStatusEffects()) {
            this.itf$drawStatusEffects();
            this.itf$statusEffectsDrawnThisFrame = true;
        }
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void itf$afterDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (!this.itf$statusEffectsDrawnThisFrame && this.itf$shouldForceDrawStatusEffects()) {
            this.itf$drawStatusEffects();
        }
        if (!this.itf$curseTooltipDrawnThisFrame) {
            this.itf$drawCurseTooltipAt(mouseX, mouseY);
        }
    }

    @Inject(method = "displayDebuffEffects", at = @At("HEAD"), cancellable = true)
    private void itf$displayDebuffEffects(CallbackInfo ci) {
        ci.cancel();
        this.itf$drawStatusEffects();
    }

    @Unique
    private boolean itf$shouldForceDrawStatusEffects() {
        return ((ITFEntityPlayer) this.mc.thePlayer).itf$GetActiveCurses().size() > 1;
    }

    @Unique
    private boolean itf$drawStatusEffects() {
        EntityClientPlayerMP player = this.mc.thePlayer;
        List<Curse> curses = ((ITFEntityPlayer) player).itf$GetActiveCurses();
        Collection<?> potionEffects = player.getActivePotionEffects();
        int effectCount = this.itf$getStatusCount(player, curses, potionEffects);
        if (effectCount <= 0) {
            return false;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);

        int left = this.guiLeft - 128;
        int top = this.guiTop;
        int spacing = this.itf$getStatusSpacing(effectCount);

        if (player.isMalnourished()) {
            this.itf$drawMalnourishedStatus(left, top);
            top += spacing;
        }

        if (player.isInsulinResistant()) {
            this.itf$drawInsulinResistantStatus(player, left, top);
            top += spacing;
        }

        for (int i = 0; i < curses.size(); i++) {
            this.itf$drawCurseStatus(curses.get(i), i, curses.size(), left, top);
            top += spacing;
        }

        for (Object potionEffectObject : potionEffects) {
            this.itf$drawPotionStatus((PotionEffect) potionEffectObject, left, top);
            top += spacing;
        }
        return true;
    }

    @Inject(method = "drawCurseBoxTooltip", at = @At("HEAD"), cancellable = true)
    private void itf$drawCurseBoxTooltip(int mouseX, int mouseY, CallbackInfo ci) {
        ci.cancel();
        this.itf$drawCurseTooltipAt(mouseX, mouseY);
    }

    @Unique
    private boolean itf$drawCurseTooltipAt(int mouseX, int mouseY) {
        EntityClientPlayerMP player = this.mc.thePlayer;
        List<Curse> curses = ((ITFEntityPlayer) player).itf$GetActiveCurses();
        if (curses.isEmpty()) {
            return false;
        }

        int effectCount = this.itf$getStatusCount(player, curses, player.getActivePotionEffects());
        int spacing = this.itf$getStatusSpacing(effectCount);
        int left = this.guiLeft - 128;
        int top = this.guiTop;

        if (player.isMalnourished()) {
            top += spacing;
        }
        if (player.isInsulinResistant()) {
            top += spacing;
        }

        int hitHeight = this.itf$getStatusHitHeight(spacing);
        for (Curse curse : curses) {
            if (this.itf$isMouseOverStatus(mouseX, mouseY, left, top, hitHeight)) {
                this.itf$drawCurseTooltip(curse, mouseX, mouseY);
                this.itf$curseTooltipDrawnThisFrame = true;
                return true;
            }
            top += spacing;
        }
        return false;
    }

    @Unique
    private int itf$getStatusCount(EntityClientPlayerMP player, List<Curse> curses, Collection<?> potionEffects) {
        int count = potionEffects.size() + curses.size();
        if (player.isMalnourished()) {
            count++;
        }
        if (player.isInsulinResistant()) {
            count++;
        }
        return count;
    }

    @Unique
    private int itf$getStatusSpacing(int effectCount) {
        return effectCount > 5 ? 132 / (effectCount - 1) : 33;
    }

    @Unique
    private int itf$getStatusHitHeight(int spacing) {
        return Math.min(31, spacing);
    }

    @Unique
    private boolean itf$isMouseOverStatus(int mouseX, int mouseY, int left, int top, int hitHeight) {
        return mouseX >= left && mouseX <= left + 123 && mouseY >= top && mouseY <= top + hitHeight;
    }

    @Unique
    private void itf$drawStatusBackground(int left, int top) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_110408_a);
        this.drawTexturedModalRect(left, top, 0, 166, 140, 32);
    }

    @Unique
    private void itf$drawStatusText(String title, String detail, int left, int top) {
        this.fontRenderer.drawStringWithShadow(title, left + 27, top + 7, 16777215);
        this.fontRenderer.drawStringWithShadow(detail, left + 27, top + 17, 8355711);
    }

    @Unique
    private void itf$drawMalnourishedStatus(int left, int top) {
        this.itf$drawStatusBackground(left, top);
        this.mc.getTextureManager().bindTexture(itf$MITE_ICONS);
        this.drawTexturedModalRect(left + 6, top + 7, 18, 198, 18, 18);
        String title = I18n.getString("effect.malnourished");
        String detail = this.mc.theWorld.getTotalWorldTime() / 100L % 2L == 0L
                ? I18n.getString("effect.malnourished.slowHealing")
                : I18n.getString("effect.malnourished.plus50PercentHunger");
        this.itf$drawStatusText(title, detail, left, top);
    }

    @Unique
    private void itf$drawInsulinResistantStatus(EntityClientPlayerMP player, int left, int top) {
        this.itf$drawStatusBackground(left, top);
        this.mc.getTextureManager().bindTexture(itf$SUGAR_ICON);
        this.drawTexturedModalRect2(left + 7, top + 8, 16, 16);
        EnumInsulinResistanceLevel level = player.getInsulinResistanceLevel();
        if (level != null) {
            EnumChatFormatting levelColor = level.getColor();
            GL11.glColor4f(levelColor.getRedAsFloat(), levelColor.getGreenAsFloat(), levelColor.getBlueAsFloat(), 1.0F);
        }
        this.mc.getTextureManager().bindTexture(itf$MITE_ICONS);
        this.drawTexturedModalRect(left + 6, top + 7, 54, 198, 18, 18);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.itf$drawStatusText(
                I18n.getString("effect.insulinResistance"),
                StringUtils.ticksToElapsedTime(player.getInsulinResistance()),
                left,
                top
        );
    }

    @Unique
    private void itf$drawCurseStatus(Curse activeCurse, int index, int total, int left, int top) {
        Curse curse = Curse.cursesList[activeCurse.id];
        if (curse == null) {
            return;
        }
        this.itf$drawStatusBackground(left, top);
        this.mc.getTextureManager().bindTexture(itf$MITE_ICONS);
        this.drawTexturedModalRect(left + 6, top + 7, 0, 198, 18, 18);
        String title = I18n.getString("effect.cursed");
        if (total > 1) {
            title += " " + (index + 1) + "/" + total;
        }
        String detail = activeCurse.effect_known
                ? EnumChatFormatting.DARK_PURPLE + curse.getTitle()
                : Translator.get("curse.unknown");
        this.itf$drawStatusText(title, detail, left, top);
    }

    @Unique
    private void itf$drawPotionStatus(PotionEffect potionEffect, int left, int top) {
        Potion potion = Potion.potionTypes[potionEffect.getPotionID()];
        if (potion == null) {
            return;
        }
        this.itf$drawStatusBackground(left, top);
        if (potion instanceof IPotion ricPotion && ricPotion.ric$UsesIndividualTexture()) {
            this.mc.getTextureManager().bindTexture(ricPotion.ric$GetTexture());
            this.drawTexturedModalRect2(left + 6, top + 7, 18, 18);
        } else if (potion.hasStatusIcon()) {
            int iconIndex = potion.getStatusIconIndex();
            this.drawTexturedModalRect(left + 6, top + 7, iconIndex % 8 * 18, 198 + iconIndex / 8 * 18, 18, 18);
        }
        this.itf$drawStatusText(
                this.itf$getPotionDisplayName(potion, potionEffect),
                Potion.getDurationString(potionEffect),
                left,
                top
        );
    }

    @Unique
    private String itf$getPotionDisplayName(Potion potion, PotionEffect potionEffect) {
        return I18n.getString(potion.getName()) + " " + I18n.getString("enchantment.level." + (potionEffect.getAmplifier() + 1));
    }

    @Unique
    private void itf$drawCurseTooltip(Curse activeCurse, int mouseX, int mouseY) {
        Curse curse = Curse.cursesList[activeCurse.id];
        if (curse == null) {
            return;
        }
        ArrayList<String> tooltip = new ArrayList<>();
        if (activeCurse.effect_known) {
            tooltip.add(EnumChatFormatting.DARK_PURPLE + curse.getTitle());
            for (String line : curse.getTooltip()) {
                tooltip.add(EnumChatFormatting.GRAY + line);
            }
        } else {
            tooltip.add(EnumChatFormatting.DARK_GRAY + Translator.get("curse.unknown"));
        }
        this.func_102021_a(tooltip, mouseX, mouseY, false);
    }
}
