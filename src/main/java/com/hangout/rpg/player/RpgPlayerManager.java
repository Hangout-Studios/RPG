package com.hangout.rpg.player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.hangout.core.Config;
import com.hangout.core.HangoutAPI;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.utils.OccupationAction;
import com.hangout.rpg.utils.PlayerOccupations;

public class RpgPlayerManager {
	
	private static HashMap<UUID, RpgPlayer> rpgPlayers = new HashMap<UUID, RpgPlayer>();
	
	public static void addPlayer(RpgPlayer p){
		rpgPlayers.put(p.getHangoutPlayer().getUUID(), p);
	}
	
	public static RpgPlayer getPlayer(UUID id){
		if(rpgPlayers.containsKey(id)){
			return rpgPlayers.get(id);
		}
		return null;
	}
	
	public static void removePlayer(UUID id){
		if(rpgPlayers.containsKey(id)){
			rpgPlayers.remove(id);
		}
	}
	
	public static void commitOccupationAction(final RpgPlayer player, final OccupationAction action, final PlayerOccupations occupation){
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
			@Override
			public void run() {
				try(PreparedStatement pst = HangoutAPI.getDatabase().prepareStatement(
		    			"INSERT INTO " + Config.databaseName + ".occupation_action (player_id, occupation, action) VALUES (?, ?, ?)")){
					pst.setString(1, player.getHangoutPlayer().getUUID().toString());
					pst.setString(2, occupation.toString());
					pst.setString(3, action.toString());
		    		pst.execute();
		    		pst.close();
		    	} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void commitExperienceAction(final RpgPlayer player, final String source, final int experience){
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
			@Override
			public void run() {
				try(PreparedStatement pst = HangoutAPI.getDatabase().prepareStatement(
		    			"INSERT INTO " + Config.databaseName + ".experience_action (player_id, experience, source) VALUES (?, ?, ?)")){
					pst.setString(1, player.getHangoutPlayer().getUUID().toString());
					pst.setInt(2, experience);
					pst.setString(3, source);
		    		pst.execute();
		    		pst.close();
		    	} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
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
