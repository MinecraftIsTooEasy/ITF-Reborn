package net.oilcake.mitelros.mixins.block;

import net.minecraft.*;
import net.oilcake.mitelros.registry.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockCauldron.class)
public class BlockCauldronMixin extends Block {
    protected BlockCauldronMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, EnumFace face, float offset_x, float offset_y, float offset_z) {
        ItemArmor armor;
        int volume_in_cauldron;
        if (world.isBlockFaceFlatAndSolid(x, y + 1, z, EnumFace.BOTTOM)) {
            return false;
        }
        ItemStack held_item = player.getHeldItemStack();
        if (held_item == null) {
            return false;
        }
        int volume_in_cauldron_before = volume_in_cauldron = BlockCauldron.func_111045_h_(world.getBlockMetadata(x, y, z));
        int cauldron_max_volume = 3;
        boolean action_performed = false;
        Item item = held_item.getItem();
        if (item instanceof ItemVessel) {
            ItemVessel vessel = (ItemVessel)item;
            int vessel_volume = vessel.getStandardVolume();
            if (vessel_volume > 3) {
                vessel_volume = 3;
            }
            if (vessel.isEmpty()) {
                if (volume_in_cauldron >= vessel_volume) {
                    if (player.onClient()) {
                        return true;
                    }
                    if (!player.inCreativeMode()) {
                        player.inventory.convertOneOfCurrentItem(new ItemStack(vessel.getPeerForContents(Material.water)));
                    }
                    volume_in_cauldron -= vessel_volume;
                    action_performed = true;
                }
            } else if (vessel.contains(Material.water) && volume_in_cauldron < 3) {
                if (player.onClient()) {
                    return true;
                }
                if (!player.inCreativeMode()) {
                    player.inventory.convertOneOfCurrentItem(new ItemStack(vessel.getEmptyVessel()));
                }
                volume_in_cauldron = MathHelper.clamp_int(volume_in_cauldron + vessel_volume, 0, 3);
                action_performed = true;
            }
        } else if (item == Item.glassBottle) {
            if (volume_in_cauldron > 0) {
                if (player.onClient()) {
                    return true;
                }
                if (!player.inCreativeMode()) {
                    player.inventory.convertOneOfCurrentItem(new ItemStack(Items.suspiciousPotion));
                }
                --volume_in_cauldron;
                action_performed = true;
            }
        } else if (item == Item.potion && held_item.getItemSubtype() == 0) {
            if (volume_in_cauldron < 3) {
                if (player.onClient()) {
                    return true;
                }
                if (!player.inCreativeMode()) {
                    player.inventory.convertOneOfCurrentItem(new ItemStack(Items.glassBottle));
                }
                ++volume_in_cauldron;
                action_performed = true;
            }
        } else if (item instanceof ItemArmor && (armor = (ItemArmor)item).hasColor(held_item) && volume_in_cauldron > 0) {
            if (player.onClient()) {
                return true;
            }
            armor.removeColor(held_item);
            --volume_in_cauldron;
            action_performed = true;
        }
        if (player.onServer() && volume_in_cauldron != volume_in_cauldron_before) {
            world.setBlockMetadataWithNotify(x, y, z, volume_in_cauldron, 2);
            world.func_96440_m(x, y, z, this.blockID);
        }
        return action_performed;
    }
}
