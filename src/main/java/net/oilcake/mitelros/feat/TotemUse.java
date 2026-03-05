package net.oilcake.mitelros.feat;

import com.google.common.base.Predicates;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.oilcake.mitelros.ModReference;
import net.oilcake.mitelros.item.ItemTotem;
import net.oilcake.mitelros.unsafe.BaublesAccess;
import net.oilcake.mitelros.unsafe.ExtremeAccess;
import net.oilcake.mitelros.unsafe.OffhandAccess;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class TotemUse {
    public static final List<Source> SOURCES = new ArrayList<>();

    public static final Source MAIN_HAND = register(new DefaultSource(
            Predicates.alwaysTrue(),
            EntityPlayer::getHeldItemStack,
            (player, itemStack) -> player.setHeldItemStack(null)
    ));

    public static final Source OFF_HAND = register(new DefaultSource(
            player -> ModReference.hasMod(ModReference.OFFHAND),
            OffhandAccess::getOffhandItem,
            (player, itemStack) -> OffhandAccess.setOffhandItem(player, null)
    ));

    public static final Source BAUBLES = register(new DefaultSource(
            player -> ModReference.hasMod(ModReference.BAUBLES),
            BaublesAccess::getStackInAmulet,
            (player, itemStack) -> BaublesAccess.clearAmulet(player)
    ));

    public static final Source EXTREME_JEWELRY = register(new DefaultSource(
            player -> ModReference.hasMod(ModReference.EXTREME),
            ExtremeAccess::findTotem,
            ExtremeAccess::consumeTotem
    ));


    public static Source register(Source source) {
        SOURCES.add(source);
        return source;
    }

    public static boolean skipDeath(EntityPlayer player) {
        for (Source source : SOURCES) {
            if (!source.canUse(player)) continue;
            ItemStack itemStack = source.get(player);
            if (itemStack != null && itemStack.getItem() instanceof ItemTotem totem) {
                totem.trigger(player, false);
                source.consume(player, itemStack);
                return true;
            }
        }

        return false;
    }

    public record DefaultSource(
            Predicate<EntityPlayer> predicate,
            Function<EntityPlayer, ItemStack> itemAccess,
            BiConsumer<EntityPlayer, ItemStack> consumer
    ) implements Source {

        @Override
        public boolean canUse(EntityPlayer player) {
            return predicate.test(player);
        }

        @Override
        public @Nullable ItemStack get(EntityPlayer player) {
            return itemAccess.apply(player);
        }

        @Override
        public void consume(EntityPlayer player, ItemStack itemStack) {
            consumer.accept(player, itemStack);
        }
    }


    public interface Source {
        boolean canUse(EntityPlayer player);

        @Nullable
        ItemStack get(EntityPlayer player);

        void consume(EntityPlayer player, ItemStack itemStack);
    }
}
