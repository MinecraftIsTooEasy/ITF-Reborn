package net.oilcake.mitelros.block;

import net.minecraft.Block;

import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

public record FlowerCollection<T extends Block>(
        T luminescent_herb,
        T azure_bluet,
        T cornflower,
        T lily_of_the_valley,
        T pink_tulip,
        T white_tulip,
        T red_tulip,
        T agave
) {

    public static final int TYPE_NUMBER = 8;

    public static boolean isValidType(int type) {
        return type >= 0 && type < TYPE_NUMBER;
    }

    public static FlowerCollection<BlockFlowerExtend> create(IntSupplier idSupplier) {
        return new FlowerCollection<>(
                new BlockFlowerExtend(idSupplier.getAsInt(), "luminescent_herb"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "azure_bluet"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "cornflower"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "lily_of_the_valley"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "pink_tulip"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "white_tulip"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "red_tulip"),
                new BlockFlowerExtend(idSupplier.getAsInt(), "agave")
        );
    }

    public Stream<T> stream() {
        return Stream.of(
                this.luminescent_herb,
                this.azure_bluet,
                this.cornflower,
                this.lily_of_the_valley,
                this.pink_tulip,
                this.white_tulip,
                this.red_tulip,
                this.agave
        );
    }

    public void forEach(Consumer<T> consumer) {
        consumer.accept(this.luminescent_herb);
        consumer.accept(this.azure_bluet);
        consumer.accept(this.cornflower);
        consumer.accept(this.lily_of_the_valley);
        consumer.accept(this.pink_tulip);
        consumer.accept(this.white_tulip);
        consumer.accept(this.red_tulip);
        consumer.accept(this.agave);
    }

    public T pick(int type) {
        return switch (type) {
            case 0 -> (this.luminescent_herb);
            case 1 -> (this.azure_bluet);
            case 2 -> (this.cornflower);
            case 3 -> (this.lily_of_the_valley);
            case 4 -> (this.pink_tulip);
            case 5 -> (this.white_tulip);
            case 6 -> (this.red_tulip);
            case 7 -> (this.agave);
            default -> throw new AssertionError();
        };
    }
}
