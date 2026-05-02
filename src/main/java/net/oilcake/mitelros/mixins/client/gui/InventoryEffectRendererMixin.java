package net.oilcake.mitelros.mixins.client.gui;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(InventoryEffectRenderer.class)
public abstract class InventoryEffectRendererMixin extends GuiContainer {
	
	@Unique private int currentCurseIndex = 0;
	
	public InventoryEffectRendererMixin(Container par1Container) {
		super(par1Container);
	}

	@Redirect(method = "displayDebuffEffects", at = @At(value = "FIELD", target = "Lnet/minecraft/EntityClientPlayerMP;is_cursed:Z", opcode = Opcodes.GETFIELD))
	private boolean redirectIsCursed(EntityClientPlayerMP player) {
		return !((ITFEntityPlayer) player).itf$GetActiveCurses().isEmpty();
	}
	
	@Redirect(method = "displayDebuffEffects",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityClientPlayerMP;getCurse()Lnet/minecraft/Curse;"))
	private Curse redirectGetCurse(EntityClientPlayerMP player) {
		List<Curse> curses = ((ITFEntityPlayer) player).itf$GetActiveCurses();
		if (curses.isEmpty()) return null;
		if (currentCurseIndex >= curses.size()) currentCurseIndex = 0;
		return curses.get(currentCurseIndex);
	}
	
	@Redirect(method = "displayDebuffEffects",
			at = @At(value = "FIELD", target = "Lnet/minecraft/EntityClientPlayerMP;curse_effect_known:Z", opcode = Opcodes.GETFIELD))
	private boolean redirectCurseEffectKnown(EntityClientPlayerMP player) {
		List<Curse> curses = ((ITFEntityPlayer) player).itf$GetActiveCurses();
		if (curses.isEmpty()) return false;
		if (currentCurseIndex >= curses.size()) currentCurseIndex = 0;
		return curses.get(currentCurseIndex).effect_known;
	}

	@Inject(method = "drawCurseBoxTooltip", at = @At("HEAD"), cancellable = true)
	private void onDrawCurseBoxTooltip(int mouse_x, int mouse_y, CallbackInfo ci) {
		if (((ITFEntityPlayer) this.mc.thePlayer).itf$GetActiveCurses().isEmpty()) {
			ci.cancel();
			return;
		}
		int baseLeft = this.guiLeft - 128;
		int baseTop = this.guiTop;
		if (this.mc.thePlayer.isMalnourished()) baseTop += 33;
		if (this.mc.thePlayer.isInsulinResistant()) baseTop += 33;
		int boxWidth = 123;
		int boxHeight = 32;
		int boxRight = baseLeft + boxWidth;
		int boxTop = baseTop;
		int boxBottom = boxTop + boxHeight;
		if (mouse_x >= baseLeft && mouse_x <= boxRight && mouse_y >= boxTop && mouse_y <= boxBottom) {
			ArrayList<String> tooltip = new ArrayList<>();
			tooltip.add(EnumChatFormatting.DARK_PURPLE + Translator.get("effect.cursed") + ":");
			for (Curse temp : ((ITFEntityPlayer) this.mc.thePlayer).itf$GetActiveCurses()) {
				Curse curse = Curse.cursesList[temp.id];
//				if (curse.effect_known) {
					for (String line : curse.getTooltip()) {
						tooltip.add(EnumChatFormatting.GRAY + line);
					}
//				} else {
//					tooltip.add(EnumChatFormatting.DARK_GRAY + Translator.get("curse.unknown"));
//				}
//				if (!Minecraft.inDevMode()) continue;
//				for (String line : curse.getTooltip()) {
//					tooltip.add(EnumChatFormatting.RED + "(" + line + ")");
//				}
			}
			if (!tooltip.isEmpty() && tooltip.get(tooltip.size() - 1).isEmpty()) {
				tooltip.remove(tooltip.size() - 1);
			}
			this.func_102021_a(tooltip, mouse_x, mouse_y, false);
			ci.cancel();
		}
	}
	
	@ModifyArg(method = "displayDebuffEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 5))
	private String modify(String par1Str) {
		if (ITFConfig.TagRejection.getIntegerValue() <= 2)  return par1Str;
		return EnumChatFormatting.DARK_PURPLE + Translator.getFormatted("effect.curses", ((ITFEntityPlayer) this.mc.thePlayer).itf$GetActiveCurses().size());
	}
}