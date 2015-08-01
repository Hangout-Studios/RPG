package com.hangout.rpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerOccupations;

public class BattleListener implements Listener {
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		Player p = e.getEntity().getKiller();
		if(p == null || e.getEntity() instanceof Player) return;
		
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUniqueId());
		if(rpgP.getOccupation() == PlayerOccupations.WARRIOR){
			rpgP.addExperience(10, true, "KILL_MOB_" + e.getEntity().getType().toString(), PlayerOccupations.WARRIOR);
		}
	}
}
