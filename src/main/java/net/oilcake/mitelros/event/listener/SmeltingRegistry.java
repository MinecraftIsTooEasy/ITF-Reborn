package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.SmeltingRecipeRegisterEvent;
import moddedmite.rustedironcore.api.event.handler.SmeltingHandler;
import net.minecraft.ItemFood;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.feat.MetalRecycle;
import net.oilcake.mitelros.item.ItemKettle;
import net.oilcake.mitelros.registry.item.Items;

import java.util.function.Consumer;

import static net.oilcake.mitelros.registry.block.Blocks.*;
import static net.oilcake.mitelros.registry.item.Items.*;

public class SmeltingRegistry implements Consumer<SmeltingRecipeRegisterEvent> {
    @Override
    public void accept(SmeltingRecipeRegisterEvent event) {
        event.register(pieceAdamantium.itemID, new ItemStack(adamantiumNugget));
        event.register(pieceCopper.itemID, new ItemStack(copperNugget));
        event.register(pieceGold.itemID, new ItemStack(goldNugget));
        event.register(pieceGoldNether.itemID, new ItemStack(goldNugget));
        event.register(pieceSilver.itemID, new ItemStack(silverNugget));
        event.register(pieceIron.itemID, new ItemStack(ironNugget));
        event.register(pieceNickel.itemID, new ItemStack(nickelNugget));
        event.register(pieceMithril.itemID, new ItemStack(mithrilNugget));
        event.register(pieceTungsten.itemID, new ItemStack(tungstenNugget));
        event.register(pieceUru.itemID, new ItemStack(uruNugget));
        event.register(ancientMetalArmorPiece.itemID, new ItemStack(ancientMetalNugget));

        event.register(clayBowlWater.itemID, new ItemStack(clayBowlWaterPure));
        event.register(bowlWater.itemID, new ItemStack(bowlWaterPure));
        event.register(suspiciousPotion.itemID, new ItemStack(potion, 1, 0));
        event.register(horse_meat.itemID, new ItemStack(horse_meat_cooked));
        event.register(clayBowlRaw.itemID, new ItemStack(clayBowlEmpty));// TODO remove when ric 1.3.7
        event.register(leatherKettle.itemID, new ItemStack(leatherKettlePure));//
        event.register(hardenedClayJug.itemID, new ItemStack(hardenedClayJugPure));//
        event.register(clayJug.itemID, new ItemStack(hardenedClayJug).setItemDamage(hardenedClayJug.maxDamage - 1));

        ItemFood.setCookingResult((ItemFood) horse_meat, (ItemFood) horse_meat_cooked, 6);

        event.register(oreTungsten.blockID, new ItemStack(Items.tungstenIngot));
        event.register(oreNickel.blockID, new ItemStack(Items.nickelIngot));
        event.register(oreUru.blockID, new ItemStack(Items.uruIngot));

        this.registerSpecialRecipes(event);
    }

    private void registerSpecialRecipes(SmeltingRecipeRegisterEvent event) {
        event.registerSpecial((itemStack, heatLevel) -> {
            if (MetalRecycle.canApply(itemStack)) {
                return SmeltingHandler.result(MetalRecycle.recycleMetal(itemStack));
            }
            return null;
        });

        event.registerSpecial((itemStack, heatLevel) -> itemStack.getItem() == Items.clayBowlRaw && itemStack.stackSize >= 4 ? SmeltingHandler.result(4, new ItemStack(Items.clayBowlEmpty, 4)) : null);
        event.registerSpecial((itemStack, heatLevel) -> {
            if (itemStack.getItem() instanceof ItemKettle itemKettle) {
                if (itemKettle.canBoil()) {
                    return SmeltingHandler.result(itemKettle.onBoil(itemStack));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        });
    }

}
