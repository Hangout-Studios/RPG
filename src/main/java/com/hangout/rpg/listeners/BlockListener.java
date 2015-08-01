package com.hangout.rpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		p.addExperience(1, true, "BLOCK_BREAK_" + e.getBlock().getType().toString());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		p.addExperience(1, true, "BLOCK_PLACE_" + e.getBlock().getType().toString());
	}
}
