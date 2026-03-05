package net.oilcake.mitelros.item;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;

public class ItemTotemFecund extends ItemTotem {
    public ItemTotemFecund(int id) {
        super(id, Material.gold, "totem");
    }

    @Override
    public void specifiedEffect(EntityPlayer player) {
        for (int i = 0; i < 8; i++) {
            player.entityFX(EnumEntityFX.heal);
        }
        player.setHealth(player.getMaxHealth(), true, player.getHealFX());
        if (ITFConfig.TagTotemBlessing.getBooleanValue()) {
            player.addPotionEffect(new PotionEffect(Potion.regeneration.id, 1200, 1));
        }
        this.growCrops(player.worldObj, player.getBlockPosX(), player.getBlockPosY(), player.getBlockPosZ(), ITFConfig.TagTotemBlessing.getBooleanValue() ? 14 : 7);
    }

    private void growCrops(World world, int startX, int startY, int startZ, int range) {
        for (int x = startX - range; x <= startX + range; x++) {
            for (int y = startY - 1; y <= startX + 1; y++)
                for (int z = startZ - range; z <= startZ + range; z++) {
                    Block block = world.getBlock(x, y, z);
                    if (block instanceof BlockCrops blockCrops) {
                        int metadata = world.getBlockMetadata(x, y, z);
                        world.playAuxSFX(2005, x, y, z, 0);
                        world.setBlockMetadataWithNotify(x, y, z, blockCrops.incrementGrowth(metadata), 2);
                    }
                }
        }
    }
}
