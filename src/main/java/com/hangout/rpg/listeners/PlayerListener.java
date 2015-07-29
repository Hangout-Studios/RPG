package com.hangout.rpg.listeners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.Config;
import com.hangout.core.HangoutAPI;
import com.hangout.core.events.PlayerJoinCompleteEvent;
import com.hangout.core.events.PlayerPostLoadEvent;
import com.hangout.core.events.PlayerPreSaveEvent;
import com.hangout.core.events.PlayerQuitCompleteEvent;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.OccupationAction;
import com.hangout.rpg.utils.PlayerOccupations;
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
				
				
				loadOccupation(p);
				loadExperience(p);
				
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
	
	public static void loadOccupation(final RpgPlayer p){
		try (PreparedStatement pst = HangoutAPI.getDatabase().prepareStatement(
                "SELECT occupation FROM " + Config.databaseName + ".occupation_action WHERE player_id = ? AND action = 'SWITCH' "
                		+ "ORDER BY action_id DESC LIMIT 1;")) {
			pst.setString(1, p.getHangoutPlayer().getUUID().toString());
			ResultSet rs = pst.executeQuery();
			
			PlayerOccupations occupation = PlayerOccupations.ADVENTURER;
			if(rs.first()){
				occupation = PlayerOccupations.valueOf(rs.getString("occupation"));
			}else{
				RpgPlayerManager.commitOccupationAction(p, OccupationAction.ADD, occupation);
				RpgPlayerManager.commitOccupationAction(p, OccupationAction.SWITCH, occupation);
			}
			
			pst.close();
			
			p.setOccupation(occupation, false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadExperience(final RpgPlayer p){
		try (PreparedStatement pst = HangoutAPI.getDatabase().prepareStatement(
                "SELECT sum(experience) as 'exp_sum' FROM " + Config.databaseName + ".experience_action WHERE player_id = ?;")) {
			pst.setString(1, p.getHangoutPlayer().getUUID().toString());
			ResultSet rs = pst.executeQuery();
			
			int experience = 0;
			if(rs.first()){
				experience = rs.getInt("exp_sum");
			}
			
			pst.close();
			
			p.addExperience(experience, false, "DATABASE");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
