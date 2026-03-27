package net.oilcake.mitelros.mixins.container;

import net.minecraft.DamageSource;
import net.minecraft.EntityPlayer;
import net.minecraft.InventoryPlayer;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.inventory.MinePocketInventory;
import net.oilcake.mitelros.item.ItemMinePocket;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.util.InventoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryPlayer.class)
public abstract class InventoryPlayerMixin {
    @Shadow
    public EntityPlayer player;

    @Shadow
    public abstract void addItemStackToInventoryOrDropIt(ItemStack item_stack);

    @Inject(method = "takeDamage(Lnet/minecraft/ItemStack;Lnet/minecraft/DamageSource;F)Z", at = @At(value = "RETURN", ordinal = 7))
    private void breakContainer(ItemStack item_stack, DamageSource damage_source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (item_stack.getItem() == Items.minePocket) {
            if (item_stack.stackSize <= 0) {// in fact, you can assert this true
                MinePocketInventory inventory = ItemMinePocket.createInventory(item_stack);
                InventoryUtil.stream(inventory, this::addItemStackToInventoryOrDropIt);
            }
        }
    }
}
