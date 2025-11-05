package net.oilcake.mitelros.feat;

import net.minecraft.*;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.entity.mob.EntityHusk;
import net.oilcake.mitelros.entity.mob.EntityStray;

import java.util.List;

public class SpawnEntryControl {
    public static void postBiomeInit() {
        modifyMobSpawn();
        if (ITFConfig.TagCreaturesV2.getBooleanValue()) {
            animalSpawnOverride();
        }
    }

    private static void modifyMobSpawn() {
        for (BiomeGenBase biome : List.of(BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.taiga, BiomeGenBase.taigaHills)) {
            biome.removeEntityFromSpawnableLists(EntitySkeleton.class);
            biome.getSpawnableList(EnumCreatureType.monster).add(new SpawnListEntry(EntityStray.class, 100, 1, 4));
        }

        for (BiomeGenBase biome : List.of(BiomeGenBase.desert, BiomeGenBase.desertHills)) {
            biome.removeEntityFromSpawnableLists(EntityZombie.class);
            biome.getSpawnableList(EnumCreatureType.monster).add(new SpawnListEntry(EntityHusk.class, 100, 1, 4));
        }
    }

    private static void animalSpawnOverride() {
        for (BiomeGenBase biome : List.of(BiomeGenBase.forest, BiomeGenBase.forestHills)) {
            biome.removeEntityFromSpawnableLists(EntityWolf.class);
            biome.getSpawnableList(EnumCreatureType.animal).add(new SpawnListEntry(EntityWolf.class, 5, 4, 8));
        }

        for (BiomeGenBase biome : List.of(BiomeGenBase.icePlains, BiomeGenBase.iceMountains)) {
            biome.removeEntityFromSpawnableLists(EntityWolf.class);
            biome.removeEntityFromSpawnableLists(EntityDireWolf.class);
            List list = biome.getSpawnableList(EnumCreatureType.animal);
            list.add(new SpawnListEntry(EntityWolf.class, 8, 4, 8));
            list.add(new SpawnListEntry(EntityDireWolf.class, 2, 4, 6));
        }

        for (BiomeGenBase biome : List.of(BiomeGenBase.taiga, BiomeGenBase.taigaHills)) {
            biome.removeEntityFromSpawnableLists(EntityWolf.class);
            biome.removeEntityFromSpawnableLists(EntityDireWolf.class);
            List list = biome.getSpawnableList(EnumCreatureType.animal);
            list.add(new SpawnListEntry(EntityWolf.class, 10, 4, 8));
            list.add(new SpawnListEntry(EntityDireWolf.class, 5, 4, 6));
        }

    }

}
