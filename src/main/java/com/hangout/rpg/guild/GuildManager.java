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
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.rpg.Plugin;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class GuildManager {
	
	public static HashMap<Integer, Guild> guilds = new HashMap<Integer, Guild>();
	
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
	
	public static void addGuild(Guild g){
		guilds.put(g.getID(), g);
	}
	
	public static void createGuild(final String name, final String tag, final RpgPlayer p){
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
			@Override
			public void run() {
				
				int guildID = 0;
				try(PreparedStatement pst = Database.getConnection().prepareStatement(
						"SELECT max(id) as id from " + Config.databaseName + ".guild")){
					ResultSet rs = pst.executeQuery();
					
					if(rs.next()){
						int id = rs.getInt("id");
						if(!rs.wasNull()){
							guildID = id + 1;
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				Guild g = new Guild(guildID, name, tag);
				executeGuildUpdate(g, true);
				executeGuildExperienceAction(g, 1, "INITIAL");
				g.addPlayer(p, p, GuildRank.LEADER, true);
				addGuild(g);
			}
		});
	}
	
	public static void loadGuilds(){
		
		DebugUtils.sendDebugMessage("Loading guilds...", DebugMode.INFO);
		
		//Load all guilds
		try(PreparedStatement pst = Database.getConnection().prepareStatement(
			"SELECT max(t_action.action_id) as action_id, t_action.player_member as player_id, v_player.name as player_name, t_action.action as action, " +
				"t_action.guild_id as guild_id, v_guild.name as guild_name, v_guild.tag as guild_tag, v_rank.rank_action as rank, " + 
			    "sum(v_experience.experience) as guild_experience " +
			"FROM " + Config.databaseName +".guildmember_action t_action " +
			"JOIN( " +
				"SELECT t_rank.action_id as rank_id, t_rank.player_member as player_rank_id, t_rank.action as rank_action " +
			    "FROM " + Config.databaseName +".guildmember_action t_rank " +
				"WHERE action != 'ADD_PLAYER' AND action != 'REMOVE_PLAYER' " +
			    "GROUP BY rank_id " +
			    "ORDER BY rank_id DESC" +
			") v_rank ON(t_action.player_member = v_rank.player_rank_id) " +
			"JOIN( " +
				"SELECT t_guild.id as id, t_guild.guild_name as name, t_guild.guild_tag as tag, t_guild.is_active as active " +
			    "FROM " + Config.databaseName +".guild t_guild " +
			") v_guild ON (t_action.guild_id = v_guild.id) " +
			"INNER JOIN " + Config.databaseName +".guildexperience_action AS v_experience ON t_action.guild_id = v_experience.guild_id " +
			"INNER JOIN " + Config.databaseName +".players AS v_player ON t_action.player_member = v_player.uuid " +
			"WHERE v_guild.active = true AND (action = 'ADD_PLAYER' OR action = 'REMOVE_PLAYER') " +
			"GROUP BY player_id " +
			"ORDER BY action_id DESC"
				)){
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()){
				
				final String guildName = rs.getString("guild_name");
				final String guildTag = rs.getString("guild_tag");
				final int guildID = rs.getInt("guild_id");
				
				if(GuildManager.getGuild(guildName) == null){
					Guild g = new Guild(guildID, guildName, guildTag);
					g.addExperience(rs.getInt("guild_experience"), "DATABASE", false);
					addGuild(g);
					loadBonusses(g);
				}
				
				if(rs.getString("action").equals("ADD_PLAYER")){
					final UUID playerID = UUID.fromString(rs.getString("player_id"));
					final String playerName = rs.getString("player_name");
					final GuildRank rank = GuildRank.valueOf(rs.getString("rank").split("_")[1]);
					Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
						@Override
						public void run() {
							HangoutPlayer p = HangoutPlayerManager.getPlayer(playerID);
							if(p == null){
								p = Database.loadPlayer(playerID, playerName);
							}
					
							while(true){
								if(p.isReadyLoading()){
									RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUUID());
									if(rpgP != null){
										getGuild(guildID).addPlayer(rpgP, rpgP, rank, false);
										DebugUtils.sendDebugMessage("Added player " + p.getName() + " to guild " + guildTag, DebugMode.DEBUG);
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
