package net.oilcake.mitelros.event.listener;

import moddedmite.rustedironcore.api.event.events.PlayerLoggedInEvent;
import moddedmite.rustedironcore.api.event.listener.IPlayerEventListener;
import net.minecraft.Curse;
import net.minecraft.ItemStack;
import net.minecraft.ServerPlayer;
import net.minecraft.WorldServer;
import net.oilcake.mitelros.item.ItemGuideBook;
import net.oilcake.mitelros.mixin.interfaces.ITFEntityPlayer;
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
        loadPlayerCurses(player);
    }
    
    private void loadPlayerCurses(ServerPlayer player) {
        if (!(player.worldObj instanceof WorldServer worldServer)) return;
	    List<Curse> wCurses = worldServer.getWorldInfo().getCurses();
        if (wCurses.isEmpty()) return;
        List<Curse> pCurses = new ArrayList<>();
        for (Curse curse : wCurses) {
            if (curse.cursed_player_username.equals(player.getEntityName())) {
                pCurses.add(curse);
            }
        }
        if (!pCurses.isEmpty()) {
            ((ITFEntityPlayer) player).itf$SetActiveCurses(pCurses);
        }
    }
}
