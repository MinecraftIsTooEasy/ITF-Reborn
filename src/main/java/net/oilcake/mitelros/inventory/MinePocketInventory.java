package net.oilcake.mitelros.inventory;

import net.minecraft.InventoryBasic;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.util.InventoryUtil;

public class MinePocketInventory extends InventoryBasic {
    public static final int SIZE = 5;

    private final ItemStack itemStack;

    public MinePocketInventory(String par1Str, boolean par2, ItemStack itemStack) {
        super(par1Str, par2, SIZE);
        if (itemStack.getItem() != Items.minePocket) {
            throw new AssertionError("why create inventory for not mine pocket");
        }
        this.itemStack = itemStack;
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        InventoryUtil.load(this, tagCompound);
    }

    @Override
    public void closeChest() {
        NBTTagCompound tagCompound = this.itemStack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
        }
        InventoryUtil.save(this, tagCompound);
        this.itemStack.setTagCompound(tagCompound);
    }
}
