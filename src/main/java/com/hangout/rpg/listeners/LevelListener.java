package com.hangout.rpg.listeners;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.rpg.events.PlayerLevelUpEvent;

public class LevelListener implements Listener {
	
	@EventHandler
	public void onPlayerLevelUp(PlayerLevelUpEvent e){
		if(e.getPlayer() == null) return;
		
		Player p = e.getPlayer().getHangoutPlayer().getPlayer();
		if(p == null) return;
		
		p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_DEATH, 1, 2);
		p.sendMessage("You have leveled up to level " + e.getLevel() + "!");
	}
}
