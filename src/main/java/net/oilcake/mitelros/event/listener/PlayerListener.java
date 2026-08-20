package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.PlayerLoggedInEvent;
import moddedmite.rustedironcore.api.event.listener.IPlayerEventListener;
import net.minecraft.Curse;
import net.minecraft.ItemStack;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import net.oilcake.mitelros.config.ITFConfig;
import net.oilcake.mitelros.item.ItemGuideBook;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
import net.oilcake.mitelros.network.ITFNetwork;
import net.oilcake.mitelros.network.packets.S2CUpdateCurses;
import net.oilcake.mitelros.registry.item.Items;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements IPlayerEventListener {
    @Override
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        ServerPlayer player = event.player();
        player.setHealth(player.getHealth());
        if (event.firstLogin()) {
            ItemStack guide = new ItemStack(Items.guide);
            guide.setTagCompound(ItemGuideBook.generateBookContents());
            player.inventory.addItemStackToInventoryOrDropIt(guide);
        }
        if (event.firstLogin() && ITFConfig.TagRejection.isEnable()) {
            ((WorldServer) player.worldObj).removeCursesFromPlayer(player);
        }
        loadPlayerCurses(player);
    }
    
    private void loadPlayerCurses(ServerPlayer player) {
        WorldServer worldServer = (WorldServer) player.worldObj;
	    List<Curse> wCurses = worldServer.getWorldInfo().getCurses();
        List<Curse> pCurses = new ArrayList<>();
        for (Curse curse : wCurses) {
            if (curse.cursed_player_username.equals(player.getEntityName()) && curse.has_been_realized) {
                pCurses.add(curse);
            }
        }
        if (ITFConfig.TagRejection.isEnable()) {
            for (Curse curse : pCurses) {
                curse.effect_known = true;
                curse.effect_has_already_been_learned = true;
            }
        }
        ((ITFEntityPlayer) player).itf$SetActiveCurses(pCurses);
        ITFNetwork.sendToClient(player, new S2CUpdateCurses(((ITFEntityPlayer) player).itf$GetActiveCurses()));
    }
}
