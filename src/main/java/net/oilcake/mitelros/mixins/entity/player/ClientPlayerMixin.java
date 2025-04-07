package net.oilcake.mitelros.mixins.entity.player;

import net.minecraft.*;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.api.CraftingSpeedTweaker;
import net.oilcake.mitelros.inventory.EnchantReserverInventory;
import net.oilcake.mitelros.client.gui.GuiEnchantReserver;
import net.oilcake.mitelros.client.gui.GuiMinePocketInventory;
import net.oilcake.mitelros.mixin.interfaces.ITFPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientPlayer.class)
public abstract class ClientPlayerMixin extends AbstractClientPlayer implements ITFPlayer {
    @Shadow
    protected Minecraft mc;

    public ClientPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Override
    public void itf$DisplayGUIEnchantReserver(int x, int y, int z, EnchantReserverInventory slots) {
        this.mc.displayGuiScreen(new GuiEnchantReserver(this, x, y, z, slots));
    }

    @Override
    public void itf$DisplayGuiMinePocket(IInventory minePocketInventory) {
        this.mc.displayGuiScreen(new GuiMinePocketInventory(this, minePocketInventory));
    }

    @Inject(method = "getBenchAndToolsModifier", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/BlockWorkbench;getToolMaterial(I)Lnet/minecraft/Material;", shift = At.Shift.AFTER), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectModifyWorkbenchModifier(Container container, CallbackInfoReturnable<Float> cir, ContainerWorkbench workbench, SlotCrafting slot_crafting, ItemStack item_stack, Item item, IRecipe recipe, Material material_to_check_tool_bench_hardness_against, Material benchMaterial) {
        if (benchMaterial.min_harvest_level < material_to_check_tool_bench_hardness_against.min_harvest_level) {
            cir.setReturnValue(0.0F);
        } else {
            float modifier = CraftingSpeedTweaker.get(benchMaterial);
            if (modifier != 0.0f) cir.setReturnValue(modifier);
        }
    }
}
