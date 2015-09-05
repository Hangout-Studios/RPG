package com.hangout.rpg.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import com.hangout.core.events.PlayerDataReleaseEvent;
import com.hangout.core.events.PlayerJoinCompleteEvent;
import com.hangout.core.events.PlayerPostLoadEvent;
import com.hangout.core.events.PlayerPreSaveEvent;
import com.hangout.core.events.PlayerQuitCompleteEvent;
import com.hangout.core.player.CommonPlayerManager;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.core.utils.scoreboard.DisplayboardManager;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerRaces;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinCompleteEvent e){
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(e.getUUID());
		
		rpgP.updateStats();
		rpgP.swapExperienceBar(true);
		
		DisplayboardManager.setPrefix(DisplayboardManager.getScoreboard(e.getUUID()));
		rpgP.updatePrefix();
		rpgP.updateSidebar();
	}
	
	@EventHandler
	public void onPlayerDataRelease(PlayerDataReleaseEvent e){
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
				
				CommonPlayerManager.addPlayer(e.getPlayer().getUUID(), "RPG", p);
				
				String race = (String)e.getProperty("race");
				if(race.equals("Unknown")){
					p.setRace(PlayerRaces.HUMAN);
				}else{
					p.setRace(PlayerRaces.valueOf((String)e.getProperty("race")));
				}
				
				RpgPlayerManager.loadOccupation(p);
				RpgPlayerManager.loadExperience(p);
				
				e.getPlayer().setLoadingState("rpg", true);
				
				DebugUtils.sendDebugMessage("Loaded RPG player: " + e.getPlayer().getName(), DebugMode.INFO);
			}
			
		});
	}
	
	@EventHandler
	public void onPlayerPreSave(PlayerPreSaveEvent e){
		RpgPlayer rp = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
		e.saveSecondaryProperty("race", rp.getRace().toString());
	}
	
	@EventHandler
	public void onPlayerQuitComplete(PlayerQuitCompleteEvent e){
		RpgPlayerManager.getPlayer(e.getUUID()).swapExperienceBar(false);
	}
	
	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent e) {
	    int xp = e.getAmount();
	    RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
	    
	    e.getPlayer().getPlayer().sendMessage("Giving " + xp + " xp");
	    p.addBaseExperience(xp);
	    e.setAmount(0);
	}
}
