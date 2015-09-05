package com.hangout.rpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.EnchantingInventory;

import com.hangout.rpg.player.RpgPlayerManager;

public class InventoryListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInventoryOpen(InventoryOpenEvent e){
		if(e.isCancelled()) return;
		
		if(e.getInventory() instanceof EnchantingInventory){
			RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId()).swapExperienceBar(false);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onInventoryClose(InventoryCloseEvent e){		
		if(e.getInventory() instanceof EnchantingInventory){
			RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId()).swapExperienceBar(true);
		}
	}
}
