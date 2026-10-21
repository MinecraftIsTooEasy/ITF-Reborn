package net.oilcake.mitelros.mixins.container;

import net.minecraft.*;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.feat.EnchantPrediction;
import net.oilcake.mitelros.item.ItemGoldenAppleLegend;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.S2CEnchantmentPrediction;
import net.oilcake.mitelros.registry.block.Blocks;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.util.AchievementExtend;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Random;

@Mixin(ContainerEnchantment.class)
public abstract class ContainerEnchantmentMixin extends Container {
    @Unique
    private final Random itf$predictionRandom = new Random();

    @Shadow
    public IInventory tableInventory;
    @Shadow
    private int posX;

    @Shadow
    private int posY;

    @Shadow
    private int posZ;

    public ContainerEnchantmentMixin(EntityPlayer player) {
        super(player);
    }

    @Shadow
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return false;
    }

    @Shadow
    private Random rand;

    @Shadow
    public int[] enchantLevels;

    @Inject(method = "onCraftMatrixChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/ContainerEnchantment;detectAndSendChanges()V", shift = At.Shift.AFTER))
    private void itf$updateEnchantmentPrediction(IInventory par1IInventory, CallbackInfo ci) {
        if (this.world.isRemote || ModReference.hasMod(ModReference.ENCHANT_DIVINE)) return;
        ItemStack itemStack = this.tableInventory.getStackInSlot(0);
        if (itemStack == null || ItemPotion.isBottleOfWater(itemStack) || ItemAppleGold.isUnenchantedGoldenApple(itemStack)) {
            this.itf$sendEnchantmentPrediction(null);
            return;
        }
        boolean predicated = (this.world.getBlock(this.posX, this.posY - 1, this.posZ) == Blocks.blockEnchantPredicator) || this.world.getBlock(this.posX, this.posY, this.posZ) == Blocks.magicTable;
        if (!predicated) {
            this.itf$sendEnchantmentPrediction(null);
            return;
        }
        long seed = this.itf$predictionRandom.nextLong();
        boolean extended = itemStack.getMaterialForRepairs() == Materials.uru;
        this.itf$sendEnchantmentPrediction(EnchantPrediction.predict(seed, itemStack, this.enchantLevels, extended));
        this.rand.setSeed(seed);
    }

    @Unique
    private void itf$sendEnchantmentPrediction(int[] info) {
        ITFNetwork.sendToClient((ServerPlayer) this.player, new S2CEnchantmentPrediction(this.windowId, info));
    }

    @Inject(method = "enchantItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemAppleGold;isUnenchantedGoldenApple(Lnet/minecraft/ItemStack;)Z"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void itfApple(EntityPlayer par1EntityPlayer, int par2, CallbackInfoReturnable<Boolean> cir, ItemStack var3, int experience_cost) {
        if (ItemGoldenAppleLegend.isUnenchantedGoldenApple(var3)) {
            par1EntityPlayer.addExperience(-experience_cost);
            this.tableInventory.setInventorySlotContents(0, new ItemStack(Items.goldenAppleLegend, 1, 1));
            par1EntityPlayer.triggerAchievement(AchievementExtend.decimator);
            cir.setReturnValue(true);
        }
    }

    @ModifyArg(method = "calcEnchantmentLevelsForSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;getEnchantmentLevelsAlteredByItemEnchantability(ILnet/minecraft/Item;)I"))
    private int enhance(int enchantment_levels) {
        boolean enhanced = (this.world.getBlock(this.posX, this.posY - 1, this.posZ) == Blocks.blockEnchantEnhancer || this.world.getBlock(this.posX, this.posY, this.posZ) == Blocks.magicTable);
        return enchantment_levels * (enhanced ? 2 : 1);
    }

    @Inject(method = "calcEnchantmentLevelsForSlot", at = @At("HEAD"), cancellable = true)
    private void addITFApple(Random random, int slot_index, int num_accessible_bookshelves, ItemStack item_stack, CallbackInfoReturnable<Integer> cir) {
        if (ItemGoldenAppleLegend.isUnenchantedGoldenApple(item_stack)) {
            cir.setReturnValue(25);
        }
    }
}
