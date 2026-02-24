package net.oilcake.mitelros.mixins.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import fi.dy.masa.malilib.util.StringUtils;
import net.minecraft.*;
import net.oilcake.mitelros.api.ITFApi;
import net.oilcake.mitelros.feat.ExtraInfo;
import net.oilcake.mitelros.localization.TooltipKeys;
import net.oilcake.mitelros.registry.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.PrintStream;
import java.util.List;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow
    private float reach_bonus;
    @Shadow
    @Final
    public int itemID;

    @WrapOperation(method = "<init>(ILjava/lang/String;I)V", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    public void removePrint(PrintStream instance, String x, Operation<Void> original, @Local(argsOnly = true) String texture) {
//        System.out.println(texture);
//        System.out.println(Item.itemsList[256 + Integer.parseInt(messsage.substring(11))].getItemDisplayName());
//        System.out.println("-----------------");
    }// TODO why null item call, is that fml?

    @WrapOperation(method = "doesSubtypeMatterForProduct(Lnet/minecraft/ItemStack;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;setErrorMessage(Ljava/lang/String;)V"))
    public void removeErrorInfo(String text, Operation<Void> original) {// TODO fix flowerextend recipes
    }

    @Inject(method = "getReachBonus(Lnet/minecraft/Block;I)F", at = @At("HEAD"), cancellable = true)
    private void alwaysBonus(Block block, int metadata, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(this.reach_bonus);
    }

    @Inject(method = "addInformation", at = @At(value = "INVOKE", target = "Lnet/minecraft/Item;getNutrition()I"))
    private void itfFoodInfo(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot, CallbackInfo ci) {
        if (extended_info) {
            Item item = item_stack.getItem();
            int water = ITFApi.getItemWater(item);
            if (water > 0) {
                info.add(EnumChatFormatting.AQUA + TooltipKeys.WATER_ADD.translate(water));
            } else if (water < 0) {
                info.add(EnumChatFormatting.YELLOW + TooltipKeys.WATER_MINUS.translate(water));
            }
            float chance = ITFApi.getItemWaterChance(item);
            if (chance > 0) {
                info.add(EnumChatFormatting.AQUA + TooltipKeys.WATER_CHANCE.translate(Math.round(100.0f * chance)));
            }
        }
    }

    @Inject(method = "addInformation", at = @At("TAIL"))
    private void extraInfo(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot, CallbackInfo ci) {
        if (extended_info) {
            ExtraInfo.getExtraInfo(item_stack.getItem())
                    .ifPresent(string -> info.add(EnumChatFormatting.LIGHT_GRAY + StringUtils.getTranslatedOrFallback(string, string)));
        }
    }

    @WrapOperation(method = "getExclusiveMaterial", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;setErrorMessage(Ljava/lang/String;)V", ordinal = 1))
    private void noError(String text, Operation<Void> original) {
    }// TODO why call to book?

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void setExtraAttributes(CallbackInfo ci) {
        Item.blazeRod.setReachBonus(0.5F);
        Item.shardDiamond.setXPReward(4);
        Item.shardEmerald.setXPReward(3);
        Item.shardNetherQuartz.setXPReward(2);
        Item.bowlEmpty.setMaxStackSize(4);
    }

    @ModifyReturnValue(method = "preventsHandDamage", at = @At("RETURN"))
    private boolean reachBonus(boolean original) {
        return original || this.itemID == Item.blazeRod.itemID || this.itemID == Items.enderRod.itemID || this.itemID == Items.frostRod.itemID;
    }

}
