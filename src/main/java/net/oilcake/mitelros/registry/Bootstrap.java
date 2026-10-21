package net.oilcake.mitelros.registry;

import fi.dy.masa.malilib.config.ConfigManager;
import moddedmite.rustedironcore.api.RicRegistry;
import net.minecraft.Entity;
import net.minecraft.ResourceLocation;
import net.oilcake.mitelros.ITFStart;
import net.oilcake.mitelros.block.entity.TileEntityEnchantReserver;
import net.oilcake.mitelros.block.entity.TileEntityObserver;
import net.oilcake.mitelros.block.entity.TileEntityReceiver;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.enchantment.Enchantments;
import net.oilcake.mitelros.entity.boss.EntityLich;
import net.oilcake.mitelros.entity.misc.*;
import net.oilcake.mitelros.entity.mob.*;
import net.oilcake.mitelros.event.ITFEventFML;
import net.oilcake.mitelros.event.ITFEventRIC;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.registry.block.BlockRegistry;
import net.oilcake.mitelros.registry.item.ItemRegistry;
import net.oilcake.mitelros.util.AchievementExtend;
import net.xiaoyu233.fml.ModResourceManager;
import net.xiaoyu233.fml.reload.event.MITEEvents;

public class Bootstrap {
    @SuppressWarnings("UnstableApiUsage")
    public static void bootstrap() {
        MITEEvents.MITE_EVENT_BUS.register(new ITFEventFML());
        ITFEventRIC.register();
        ConfigManager.getInstance().registerConfig(ITFConfig.getInstance());
        ITFNetwork.init();
        ModResourceManager.addResourcePackDomain(ITFStart.ResourceDomain);

        BlockRegistry.run();
        ItemRegistry.run();

        AchievementExtend.registerAchievements();

        Enchantments.registerEnchantments();

        RicRegistry registry = RicRegistry.getInstance();

        registerEntityTypes(registry);

        registerBlockEntityTypes(registry);
    }

    private static void registerEntityTypes(RicRegistry registry) {
        registerEntity(EntityWitherBoneLord.class, ITFStart.identifier("wither_bone_lord"), 541, 1314564, 13003008);
        registerEntity(EntityClusterSpider.class, ITFStart.identifier("cluster_spider"), 542, 15474675, 5051227);
        registerEntity(EntityWitherBodyguard.class, ITFStart.identifier("wither_bodyguard"), 543, 1314564, 7039851);
        registerEntity(EntitySpiderKing.class, ITFStart.identifier("spider_king"), 544, 3419431, 15790120);
        registerEntity(EntityStray.class, ITFStart.identifier("stray"), 545, 10862020, 871004);
        registerEntity(EntityHusk.class, ITFStart.identifier("husk"), 546, 9798412, 3940871);
        registerEntity(EntityPigmanLord.class, ITFStart.identifier("pig_manlord"), 547, 15373203, 5066061);
        registerEntity(EntityLich.class, ITFStart.identifier("lich"), 548, 13422277, 14008320);
        registerEntity(EntityLichShadow.class, ITFStart.identifier("lich_shadow"), 549, 13422277, 7699821);
        registerEntity(EntityStalkerCreeper.class, ITFStart.identifier("stalker_creeper"), 550, 10921638, 0);
        registry.registerEntity(EntityWandFireBall.class, ITFStart.identifier("wand_fire_ball"), 551);
        registry.registerEntity(EntityWandIceBall.class, ITFStart.identifier("wand_ice_ball"), 552);
        registry.registerEntity(EntityWandShockWave.class, ITFStart.identifier("wand_shock_wave"), 553);
        registerEntity(EntityZombieLord.class, ITFStart.identifier("zombie_lord?"), 554, 44975, 7969893);
        registerEntity(EntityRetinueZombie.class, ITFStart.identifier("zombie_retinue"), 555, 44975, 7969893);
        registerEntity(EntityBoneBodyguard.class, ITFStart.identifier("bone_bodyguard"), 556, 12698049, 4802889);
        registerEntity(EntityGhost.class, ITFStart.identifier("ghost"), 557, 9539985, 6629376);
        registerEntity(EntityEvil.class, ITFStart.identifier("evil"), 558, 9539985, 14008320);
        registerEntity(EntityUndeadGuard.class, ITFStart.identifier("undead_guard"), 559, 12698049, 4802889);
        registerEntity(EntityPigmanGuard.class, ITFStart.identifier("pig_man_guard"), 560, 15373203, 5066061);
        registerEntity(EntityCastleGuard.class, ITFStart.identifier("castle_guard"), 561, 0x565656, 0x999999);
        registerEntity(EntityLongdeadSentry.class, ITFStart.identifier("longdead_sentry"), 563, 13422277, 7699821);
        registry.registerEntity(EntityWandSlimeBall.class, ITFStart.identifier("wand_slime_ball"), 564);
        registry.registerEntity(EntityWandPearl.class, ITFStart.identifier("wand_pearl"), 565);
        registry.registerEntity(EntityUnknown.class, ITFStart.identifier("null"), 1895);
    }

    private static void registerEntity(Class<? extends Entity> clazz, ResourceLocation identifier, int numberId, int color1, int color2) {
        RicRegistry registry = RicRegistry.getInstance();
        registry.registerEntity(clazz, identifier, numberId);
        registry.registerEntityEgg(clazz, color1, color2);
    }

    private static void registerBlockEntityTypes(RicRegistry registry) {
        registry.registerTileEntity(TileEntityEnchantReserver.class, ITFStart.identifier("enchant_reserver"));
        registry.registerTileEntity(TileEntityObserver.class, ITFStart.identifier("observer"));
        registry.registerTileEntity(TileEntityReceiver.class, ITFStart.identifier("receiver"));
    }
}
