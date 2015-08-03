package com.hangout.rpg.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class FishingListener implements Listener {
	
	@EventHandler
	public void onPlayerFishEvent(PlayerFishEvent e){
		Entity caught = e.getCaught();
		if(caught == null) return;
		
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		EntityType type = caught.getType();
			 
		if(type == EntityType.DROPPED_ITEM){
			ItemStack item = (ItemStack)caught;
			
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.FISHING_SPECIAL_CATCH))){
				//replace item with something else!
			}
			
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.FISHING_CATCH_INCREASE))){
				item.setAmount(item.getAmount() * p.getStats().getStat(PlayerStat.FISHING_CATCH_AMOUNT));
			}
		}
	}
}
