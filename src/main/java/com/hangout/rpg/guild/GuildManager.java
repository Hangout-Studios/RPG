package com.hangout.rpg.guild;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.joda.time.DateTime;

import com.hangout.core.Config;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.database.Database;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class GuildManager {
	
	public static HashMap<Integer, Guild> guilds = new HashMap<Integer, Guild>();
	private static int nextID = 0;
	
	public static Guild getGuild(int id){
		if(guilds.containsKey(id)){
			return guilds.get(id);
		}
		return null;
	}
	
	public static Guild getGuild(String tag){
		for(Guild g : guilds.values()){
			if(g.getTag().equals(tag)){
				return g;
			}
		}
		return null;
	}
	
	public static void createGuild(String name, String tag, RpgPlayer p){
		Guild g = new Guild(nextID, name, tag);
		executeGuildUpdate(g, true);
		executeGuildExperienceAction(g, 0, "INITIAL");
		g.addPlayer(p, p, GuildRank.LEADER, true);
		guilds.put(g.getID(), g);
		nextID++;
	}
	
	public static void loadGuilds(){
		
		//Load all guilds
		try(PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT max(t_action.action_id) as action_id, t_action.action as action, "
				+ "t_action.player_member as player_id, v_players.name as player_name, "
				+ "v_guilds.guild_name as guild_name, v_guilds.guild_tag as guild_tag, v_guilds.guild_id as guild_id, v_experience.experience " +
				"FROM " + Config.databaseName + ".guildmember_action t_action " +
				"JOIN( " +
					"SELECT t_guilds.id as guild_id, t_guilds.is_active as active, t_guilds.guild_name as guild_name, t_guilds.guild_tag as guild_tag " +
					"FROM " + Config.databaseName + ".guild t_guilds " +
				") v_guilds ON(t_action.guild_id = v_guilds.guild_id) " +
				"JOIN( " +
					"SELECT t_players.name, t_players.uuid " +
					"FROM " + Config.databaseName + ".players t_players " +
				") v_players ON(t_action.player_member = v_players.uuid) " +
				"JOIN( " +
					"SELECT sum(t_experience.experience) as experience, t_experience.guild_id as id " +
					"FROM server_hangout.guildexperience_action t_experience " +
				") v_experience ON(t_action.guild_id = v_experience.id) " +
				"WHERE v_guilds.active = 1 and (action = 'ADD_PLAYER' or action = 'REMOVE_PLAYER') " +
				"GROUP BY v_players.name"
				)){
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				final String guildName = rs.getString("guild_name");
				if(GuildManager.getGuild(guildName) == null){
					Guild g = new Guild(rs.getInt("guild_id"), guildName, rs.getString("guild_tag"));
					g.addExperience(rs.getInt("guild_experience"), "DATABASE", false);
					loadBonusses(g);
				}
				if(rs.getString("action").equals("ADD_PLAYER")){
					final UUID playerID = UUID.fromString(rs.getString("player_id"));
					final String playerName = rs.getString("player_name");
					Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
						@Override
						public void run() {
							HangoutPlayer p = HangoutPlayerManager.getPlayer(playerID);
							if(p == null){
								p = Database.loadPlayer(playerID, playerName);
							}
					
							while(true){
								if(p != null && p.isReadyLoading()){
									RpgPlayer rpgP = RpgPlayerManager.getPlayer(playerID);
									if(rpgP != null){
										getGuild(guildName).addPlayer(rpgP, rpgP, GuildRank.NEWBIE, false);
										break;
									}
								}
							}
						}
					});
				}
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	
	}
	
	private static void loadBonusses(final Guild g){
		//DebugUtils.sendDebugMessage("Loading guild: " + g.getName());
		
		//Bonuses
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT action, bonus, timestamp FROM " + Config.databaseName + ".guildbonus_action WHERE guild_id = ? ORDER BY action_id ASC;")) {
			pst.setInt(1, g.getID());
			ResultSet rs = pst.executeQuery();
			
			List<GuildBonus> list = new ArrayList<GuildBonus>();
			HashMap<GuildBonusType, DateTime> toActivate = new HashMap<GuildBonusType, DateTime>();
			
			while(rs.next()){
				GuildBonusType type = GuildBonusType.valueOf(rs.getString("bonus"));
				String action = rs.getString("action");
				DateTime time = new DateTime(rs.getTimestamp("timestamp"));
								
				if(action.equals("PURCHASE")){
					list.add(new GuildBonus(type));
				}else if(action.equals("ENABLE")){
					if(time.plusDays(1).isAfter(DateTime.now())){
						toActivate.put(type, time.plusDays(1));
					}else{
						GuildBonus toRemove = null;
						for(GuildBonus b : list){
							if(b.getType() == type){
								toRemove = b;
							}
						}
						if(toRemove != null){
							list.remove(toRemove);
						}
					}
				}
				
				for(GuildBonusType t : toActivate.keySet()){
					for(GuildBonus b : list){
						if(b.getType() == t){
							b.activate(g, toActivate.get(t), false);
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void executeGuildMemberAction(Guild g, UUID playerMember, UUID playerAction, String action){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("player_member", playerMember.toString());
    	secondary.put("player_action", playerAction.toString());
    	secondary.put("guild_id", g.getID());
    	secondary.put("action", action);
    	
    	Database.saveToDatabase("guildmember_action", primary, secondary);
	}
	
	public static void executeGuildUpdate(Guild g, boolean active){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
  
    	secondary.put("guild_name", g.getName());
    	secondary.put("guild_tag", g.getTag());
    	secondary.put("is_active", active);
    	
    	Database.saveToDatabase("guild", primary, secondary);
	}
	
	public static void executeGuildExperienceAction(Guild g, int exp, String source){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("guild_id", g.getID());
    	secondary.put("experience", exp);
    	secondary.put("source", source);
    	
    	Database.saveToDatabase("guildexperience_action", primary, secondary);
	}
	
	public static void executeGuildBonusAction(Guild g, String action, GuildBonus bonus){
		HashMap<String, Object> primary = new HashMap<String, Object>();
    	HashMap<String, Object> secondary = new HashMap<String, Object>();
    	
    	secondary.put("guild_id", g.getID());
    	secondary.put("action", action);
    	secondary.put("bonus", bonus.getType().toString());
    	
    	Database.saveToDatabase("guildbonus_action", primary, secondary);
	}
	
}
