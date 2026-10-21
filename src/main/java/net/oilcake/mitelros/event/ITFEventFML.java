package net.oilcake.mitelros.event;

import com.google.common.eventbus.Subscribe;
import net.minecraft.*;
import net.oilcake.mitelros.block.entity.TileEntityEnchantReserver;
import net.oilcake.mitelros.block.entity.TileEntityObserver;
import net.oilcake.mitelros.block.entity.TileEntityReceiver;
import net.oilcake.mitelros.client.render.*;
import net.oilcake.mitelros.command.CommandGenerate;
import net.oilcake.mitelros.command.CommandHunger;
import net.oilcake.mitelros.command.CommandSetStatus;
import net.oilcake.mitelros.command.CommandStatus;
import net.oilcake.mitelros.enchantment.Enchantments;
import net.oilcake.mitelros.entity.boss.EntityLich;
import net.oilcake.mitelros.entity.misc.*;
import net.oilcake.mitelros.entity.mob.*;
import net.oilcake.mitelros.feat.Difficulty;
import net.oilcake.mitelros.registry.block.BlockRegistry;
import net.oilcake.mitelros.registry.item.ItemRegistry;
import net.oilcake.mitelros.sound.Sounds;
import net.oilcake.mitelros.status.MiscManager;
import net.oilcake.mitelros.util.AchievementExtend;
import net.xiaoyu233.fml.reload.event.*;

import static net.oilcake.mitelros.ITFStart.NameSpace;

public class ITFEventFML {
    @Subscribe
    public void handleChatCommand(HandleChatCommandEvent event) {
        String par2Str = event.getCommand();
        EntityPlayer player = event.getPlayer();
        if (par2Str.startsWith("Brain Power")) {
            if (player.rand.nextFloat() <= 0.1F)
                player.makeSound(Sounds.brainPower.toString(), 10.0F, 1.0F);
            player.sendChatToPlayer(ChatMessageComponent.createFromText("O-oooooooooo AAAAE-A-A-I-A-U- JO-oooooooooooo AAE-O-A-A-U-U-A- E-eee-ee-eee AAAAE-A-E-I-E-A- JO-ooo-ooo-oooo EEEEO-A-AAA-AAA- O----------\n"));
            event.setExecuteSuccess(true);
        }
        if (par2Str.startsWith("yay")) {
            World world = player.getWorld();
            ItemStack itemStack = new ItemStack(Item.fireworkCharge);
            ItemStack itemStack2 = new ItemStack(Item.firework);
            NBTTagList var25 = new NBTTagList("Explosions");
            NBTTagCompound var15 = new NBTTagCompound();
            NBTTagCompound var18 = new NBTTagCompound("Explosion");
            var18.setBoolean("Flicker", true);
            var18.setBoolean("Trail", true);
            byte var23 = (byte) (player.rand.nextInt(4) + 1);
            var18.setIntArray("Colors", ItemDye.dyeColors);
            var18.setIntArray("FadeColors", ItemDye.dyeColors);
            var18.setByte("Type", (byte) (player.rand.nextInt(4) + 1));
            var15.setTag("Explosion", var18);
            itemStack.setTagCompound(var15);
            var15 = new NBTTagCompound();
            var18 = new NBTTagCompound("Fireworks");
            var25.appendTag(itemStack.getTagCompound().getCompoundTag("Explosion"));
            var18.setTag("Explosions", var25);
            var18.setByte("Flight", (byte) (player.rand.nextInt(3) + 1));
            var15.setTag("Fireworks", var18);
            itemStack2.setTagCompound(var15);
            world.spawnEntityInWorld(new EntityFireworkRocket(world, player.posX, player.posY + 5.0D, player.posZ, itemStack2));
            event.setExecuteSuccess(true);
        }
        if (par2Str.startsWith("difficulty")) {
            int difficulty = Difficulty.calculateCurrentDifficulty();
            player.sendChatToPlayer(MiscManager.getDifficultyMessage(difficulty));
            event.setExecuteSuccess(true);
        }
    }

    @Subscribe
    public void onCommandRegister(CommandRegisterEvent event) {
        event.register(new CommandHunger());
        event.register(new CommandSetStatus());
        event.register(new CommandStatus());
        event.register(new CommandGenerate());
    }

    @Subscribe
    public void onEntityRendererRegister(EntityRendererRegistryEvent event) {
        event.register(EntityWitherBoneLord.class, new RenderWitherBoneLord());
        event.register(EntityClusterSpider.class, new RenderClusterSpider(0.4F));
        event.register(EntityWitherBodyguard.class, new RenderWitherBodyguard());
        event.register(EntitySpiderKing.class, new RenderSpiderKing(1.45F));
        event.register(EntityPigmanLord.class, new RenderPigmanLord());
        event.register(EntityStray.class, new RenderStray());
        event.register(EntityHusk.class, new RenderHusk());
        event.register(EntityLich.class, new RenderLich());
        event.register(EntityLichShadow.class, new RenderLichShadow());
        event.register(EntityStalkerCreeper.class, new RenderStalkerCreeper());
        event.register(EntityWandFireBall.class, new RenderSnowball(Item.fireballCharge));
        event.register(EntityWandIceBall.class, new RenderSnowball(Item.snowball));
        event.register(EntityWandShockWave.class, new RenderSnowball(Item.eyeOfEnder));
        event.register(EntityCastleGuard.class, new RenderCastleGuard());
        event.register(EntityWandSlimeBall.class, new RenderSnowball(Item.slimeBall));
        event.register(EntityUnknown.class, new RenderUnknown());
        event.register(EntityWandPearl.class, new RenderSnowball(Item.enderPearl));
    }

    @Subscribe
    public void onSoundsRegister(SoundsRegisterEvent event) {
        event.registerSound(Sounds.totemUse);
        event.registerSound(Sounds.spiderkingSay, 3);
        event.registerSound(Sounds.spiderkingHit, 4);
        event.registerSound(Sounds.spiderkingDeath);
        event.registerSound(Sounds.brainPower);

        event.registerStreaming(Sounds.damnation);
        event.registerStreaming(Sounds.connected);
    }
}
