package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.listener.IFurnaceUpdateListener;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.TileEntityFurnace;
import net.oilcake.mitelros.block.BlockBlastFurnace;
import net.oilcake.mitelros.block.BlockSmoker;
import net.oilcake.mitelros.mixin.interfaces.ITFFurnace;
import net.oilcake.mitelros.registry.item.Items;
import net.oilcake.mitelros.registry.property.ITFProperties;

public class FurnaceListener implements IFurnaceUpdateListener {
    @Override
    public void onFurnaceUpdatePre(TileEntityFurnace tileEntityFurnace) {
        ITFFurnace itfFurnace = (ITFFurnace) tileEntityFurnace;
        if (!tileEntityFurnace.getWorldObj().isRemote && !tileEntityFurnace.isBurning() && itfFurnace.itf$IsActive() && tileEntityFurnace.getStackInSlot(1) == null) {
            itfFurnace.itf$setActive(false);
        }
    }

    @Override
    public int onFurnaceBurnTimeDecreaseModify(TileEntityFurnace tileEntityFurnace, int original) {
        if (tileEntityFurnace.getFurnaceBlock() instanceof BlockBlastFurnace || tileEntityFurnace.getFurnaceBlock() instanceof BlockSmoker) {
            return original * 2;
        }
        return original;
    }

    @Override
    public boolean onFurnaceBeginToBurn(TileEntityFurnace tileEntityFurnace, boolean original) {
        return original && (((ITFFurnace) tileEntityFurnace).itf$CanNormallyWork() || ((ITFFurnace) tileEntityFurnace).itf$canBurnByItself());
    }

    @Override
    public void onFurnaceFuelConsumed(TileEntityFurnace tileEntityFurnace) {
        if (((ITFFurnace) tileEntityFurnace).itf$IsBlastFurnace())
            tileEntityFurnace.getWorldObj().playSoundEffect((tileEntityFurnace.xCoord + 0.5F), (tileEntityFurnace.yCoord + 0.5F), (tileEntityFurnace.zCoord + 0.5F), "imported.random.melting");
    }

    @Override
    public void onFurnaceCookTimeAdd(TileEntityFurnace tileEntityFurnace) {
        ((ITFFurnace) tileEntityFurnace).itf$setActive(true);
    }

    @Override
    public int onFurnaceCookTimeIncreaseModify(TileEntityFurnace tileEntityFurnace, int original) {
        float speed_bonus = ITFProperties.SMELTING_SPEED_MODIFIER.getOrDefault(tileEntityFurnace.getInputItemStack().getItem());
        original = (int) (original * speed_bonus);
        if (tileEntityFurnace.getFurnaceBlock() instanceof BlockBlastFurnace) {
            original *= 2;
        } else if (tileEntityFurnace.getFurnaceBlock() instanceof BlockSmoker) {
            original *= 2;
        }
        return original;
    }

    @Override
    public void onFurnaceCookSuccess(TileEntityFurnace tileEntityFurnace) {
        ItemStack inputItemStack = tileEntityFurnace.getInputItemStack();
        ItemStack outputItemStack = tileEntityFurnace.getOutputItemStack();
        if (inputItemStack != null && outputItemStack.getItem() instanceof net.minecraft.ItemMeat)
            tileEntityFurnace.getWorldObj().playSoundEffect((tileEntityFurnace.xCoord + 0.5F), (tileEntityFurnace.yCoord + 0.5F), (tileEntityFurnace.zCoord + 0.5F), "imported.random.sizzle");
        if ((inputItemStack != null && outputItemStack.getItem() == Item.bowlWater) || outputItemStack.getItem() == Items.clayBowlWater)
            tileEntityFurnace.getWorldObj().playSoundEffect((tileEntityFurnace.xCoord + 0.5F), (tileEntityFurnace.yCoord + 0.5F), (tileEntityFurnace.zCoord + 0.5F), "imported.random.boil");
    }
}
