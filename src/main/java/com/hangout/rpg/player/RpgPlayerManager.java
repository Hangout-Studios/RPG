package com.hangout.rpg.player;

import java.sql.PreparedStatement;
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
}
