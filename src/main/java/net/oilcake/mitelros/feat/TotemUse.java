package net.oilcake.mitelros.feat;

import com.google.common.base.Predicates;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.item.ItemTotem;
import net.oilcake.mitelros.unsafe.BaublesAccess;
import net.oilcake.mitelros.unsafe.ExtremeAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public class TotemUse {
    private static final List<Source> SOURCES = new ArrayList<>();

    private static final Source MAIN_HAND = register(new Source(
            Predicates.alwaysTrue(),
            player -> new InventorySection(player.inventory, x -> x == player.inventory.currentItem)
    ));

    private static final Source OFF_HAND = register(new Source(
            player -> ModReference.hasMod(ModReference.OFFHAND),
            player -> new InventorySection(player.inventory, x -> x == 36)
    ));

    private static final Source BAUBLES = register(new Source(
            player -> ModReference.hasMod(ModReference.BAUBLES),
            player -> new InventorySection(BaublesAccess.getInventory(player), BaublesAccess::isAmuletIndex)
    ));

    private static final Source EXTREME_JEWELRY = register(new Source(
            player -> ModReference.hasMod(ModReference.EXTREME),
            player -> new InventorySection(ExtremeAccess.getInventory(player), x -> true)
    ));

    private static Source register(Source source) {
        SOURCES.add(source);
        return source;
    }

    public static boolean skipDeath(EntityPlayer player) {
        for (Source source : SOURCES) {
            if (!source.predicate.test(player)) continue;

            InventorySection inventorySection = source.sectionAccess.apply(player);

            IInventory inventory = inventorySection.inventory;
            IntPredicate slotPredicate = inventorySection.slotPredicate;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                if (!slotPredicate.test(i)) continue;

                ItemStack itemStack = inventory.getStackInSlot(i);
                if (itemStack != null && itemStack.getItem() instanceof ItemTotem totem) {
                    totem.trigger(player, false);
                    inventory.setInventorySlotContents(i, null);
                    return true;
                }
            }
        }

        return false;
    }

    public record Source(
            Predicate<EntityPlayer> predicate,
            Function<EntityPlayer, InventorySection> sectionAccess
    ) {
    }

    public record InventorySection(IInventory inventory, IntPredicate slotPredicate) {
    }
}
