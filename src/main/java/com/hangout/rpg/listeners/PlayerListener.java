package com.hangout.rpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.HangoutAPI;
import com.hangout.core.events.PlayerJoinCompleteEvent;
import com.hangout.core.events.PlayerPostLoadEvent;
import com.hangout.core.events.PlayerPreSaveEvent;
import com.hangout.core.events.PlayerQuitCompleteEvent;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerRaces;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinCompleteEvent e){
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitCompleteEvent e){
		RpgPlayerManager.removePlayer(e.getUUID());
	}
	
	@EventHandler
	public void onPlayerPostLoad(final PlayerPostLoadEvent e){
		e.getPlayer().setLoadingState("rpg", false);
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){

			@Override
			public void run() {
				
				RpgPlayer p = new RpgPlayer(e.getPlayer());
				RpgPlayerManager.addPlayer(p);
				
				HangoutAPI.addCommonPlayer(e.getPlayer().getUUID(), "RPG", p);
				
				String race = (String)e.getProperty("race");
				if(race.equals("Unknown")){
					p.setRace(PlayerRaces.HUMAN);
				}else{
					p.setRace(PlayerRaces.valueOf((String)e.getProperty("race")));
				}
				
				
				RpgPlayerManager.loadOccupation(p);
				RpgPlayerManager.loadExperience(p);
				
				e.getPlayer().setLoadingState("rpg", true);
				
				HangoutAPI.sendDebugMessage("Loaded RPG player: " + e.getPlayer().getName());
			}
			
		});
	}
	
	@EventHandler
	public void onPlayerPreSave(PlayerPreSaveEvent e){
		RpgPlayer rp = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
		e.saveSecondaryProperty("race", rp.getRace().toString());
	}
}
