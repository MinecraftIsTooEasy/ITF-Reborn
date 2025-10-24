package net.oilcake.mitelros.item;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.minecraft.*;
import net.oilcake.mitelros.localization.TooltipKeys;
import net.oilcake.mitelros.material.Materials;
import net.oilcake.mitelros.mixin.interfaces.ITFFoodStats;
import net.oilcake.mitelros.util.FoodDataList;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemKettle extends Item implements IDamageableItem {
    private static final int drinkDamageUnit = 3;
    private static final int douseDamageUnit = 1;
    private static final int waterOnDrink = 2;
    private final Material vessel_material;
    private final Material contents;
    private boolean purify = false;

    public ItemKettle(int id, int volume, @Nonnull Material contents, @Nonnull Material vessel_material) {
        super(id, Material.silk, "kettle");
        this.setAlwaysEdible();
        this.addMaterial(vessel_material, contents);
        this.setMaxDamage(volume);
        this.vessel_material = vessel_material;
        this.contents = contents;
        this.setCraftingDifficultyAsComponent(100.0F);
        this.setMaxStackSize(1);
        this.register();
    }

    // vessel, contents
    private static final Table<Material, Material, ItemKettle> KettleTable = HashBasedTable.create();

    private void register() {
        KettleTable.put(this.vessel_material, this.contents, this);
    }

    public ItemKettle setPurify() {
        this.purify = true;
        return this;
    }

    public ItemKettle getContentsPeer(Material contents) {
        return getPeer(this.vessel_material, contents);
    }

    public ItemKettle getVesselPeer(Material vessel_material) {
        return getPeer(vessel_material, this.contents);
    }

    public static ItemKettle getPeer(Material vessel_material, Material contents) {
        return KettleTable.get(vessel_material, contents);
    }

    public boolean canBoil() {
        return !this.purify;
    }

    public @Nonnull ItemStack onBoil(ItemStack input) {
        ItemKettle item = (ItemKettle) input.getItem();
        ItemKettle boiled = item.getContentsPeer(Materials.pure_water);
        return new ItemStack(boiled).setItemDamage(input.getItemDamage());
    }

    @Override
    public int getNumComponentsForDurability() {
        return 8;
    }

    @Override
    public int getRepairCost() {
        return 16;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 32;
    }

    @Override
    public boolean isDrinkable(int item_subtype) {
        return true;
    }

    @Override
    public float getCompostingValue() {
        return 0.0F;
    }

    public boolean contains(Material material) {
        return this.hasMaterial(material);
    }

    @Override
    public void onItemUseFinish(ItemStack item_stack, World world, EntityPlayer player) {
        if (player.onServer()) {
            player.itf$AddWater(waterOnDrink);
            FoodDataList.onWaterDrunk(item_stack.getItem(), player);
            player.getHeldItemStack().tryDamageItem(world, drinkDamageUnit, true);
        }
    }

    @Override
    public EnumItemInUseAction getItemInUseAction(ItemStack item_stack, EntityPlayer player) {
        if (!canDrink(item_stack)) return null;

        ITFFoodStats foodStats = (ITFFoodStats) player.getFoodStats();
        if (foodStats.itf$GetWater() >= foodStats.itf$GetWaterLimit()) return null;

        return EnumItemInUseAction.DRINK;
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float partial_tick, boolean ctrl_is_down) {
        RaycastCollision rc = player.getSelectedObject(partial_tick, true);
        if (rc == null || !rc.isBlock()) {
            return false;
        }
        ItemStack item_stack = player.getHeldItemStack();

        if (item_stack.getItemDamage() > 0) {

            if (rc.getBlockHitMaterial() == Material.water || rc.getNeighborOfBlockHitMaterial() == Material.water) {

                if (player.onServer()) {
                    Material result;
                    if (this.purify) {
                        result = Materials.pure_water;
                    } else {
                        BiomeGenBase biome = rc.world.getBiomeGenForCoords(rc.block_hit_x, rc.block_hit_z);
                        if (biome == BiomeGenBase.river || biome == BiomeGenBase.desertRiver) {
                            result = Materials.pure_water;
                        } else {
                            result = Materials.water;
                        }
                    }
                    player.convertOneOfHeldItem(new ItemStack(this.getContentsPeer(result)));

                }
                return true;
            }
        } else {
            if (rc.getNeighborOfBlockHit() == Block.fire && item_stack.getItemDamage() + douseDamageUnit < item_stack.getMaxDamage()) {
                if (player.onServer()) {
                    rc.world.douseFire(rc.neighbor_block_x, rc.neighbor_block_y, rc.neighbor_block_z, (Entity) null);
                    player.getHeldItemStack().tryDamageItem(player.worldObj, douseDamageUnit, true);
                }

                return true;
            }

            if (this.contains(Material.water) || this.contains(Materials.pure_water)) {
                Block block = rc.getBlockHit();
                int x = rc.block_hit_x;
                int y = rc.block_hit_y;
                int z = rc.block_hit_z;
                EnumFace face_hit = rc.face_hit;
                if (block instanceof BlockCrops || block instanceof BlockStem || block == Block.mushroomBrown) {
                    --y;
                    block = rc.world.getBlock(x, y, z);
                    face_hit = EnumFace.TOP;
                }
                if (block == Block.tilledField && face_hit == EnumFace.TOP && BlockFarmland.fertilize(rc.world, x, y, z, player.getHeldItemStack(), player)) {
                    if (player.onServer() && !player.inCreativeMode()) {
                        player.getHeldItemStack().tryDamageItem(player.worldObj, douseDamageUnit, true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack item_stack, EntityPlayer player, List info, boolean extended_info, Slot slot) {
        super.addInformation(item_stack, player, info, extended_info, slot);
        if (extended_info) {
            if (canDrink(item_stack)) {
                info.add(EnumChatFormatting.AQUA + TooltipKeys.WATER_ADD.translate(waterOnDrink));
            }
        }
    }

    private static boolean canDrink(ItemStack itemStack) {
        return itemStack.getItemDamage() + drinkDamageUnit < itemStack.getMaxDamage();
    }
}