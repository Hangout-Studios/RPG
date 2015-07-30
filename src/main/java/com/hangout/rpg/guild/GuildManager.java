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
		g.addPlayer(p, p, true);
		guilds.put(g.getID(), g);
		nextID++;
	}
	
	public static void loadGuilds(){
		
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT id FROM " + Config.databaseName + ".guild ORDER BY id DESC LIMIT 1")) {
			ResultSet rs = pst.executeQuery();
			
			if(rs.first()){
				nextID = rs.getInt("id") + 1;
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Load all guilds
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT id, guild_name, guild_tag FROM " + Config.databaseName + ".guild WHERE is_active = 1;")) {
			ResultSet rs = pst.executeQuery();

			while(rs.next()){
				int id = rs.getInt("id");
				String name = rs.getString("guild_name");
				String tag = rs.getString("guild_tag");

				Guild g = new Guild(id, name, tag);
				loadGuild(g);
				guilds.put(g.getID(), g);
			}

			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	private static void loadGuild(final Guild g){
		DebugUtils.sendDebugMessage("Loading guild: " + g.getName());
		
		int memberCount = 0;
		
		//Players
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
				"SELECT guildmember_action.player_member AS 'player_member', guildmember_action.action AS 'action', players.name AS 'member_name' "
				+ "FROM " + Config.databaseName + ".guildmember_action, " + Config.databaseName + ".players "
				+ "WHERE guildmember_action.guild_id = ? AND players.uuid = guildmember_action.player_member "
				+ "ORDER BY guildmember_action.action_id ASC;")) {
			pst.setInt(1, g.getID());
			ResultSet rs = pst.executeQuery();
			
			HashMap<UUID, String> currentMembers = new HashMap<UUID, String>();
			while(rs.next()){
				UUID id = UUID.fromString(rs.getString("player_member"));
				String action = rs.getString("action");
				String name = rs.getString("member_name");
				if(action.equals("ADD_PLAYER")){
					currentMembers.put(id, name);
				}else if(action.equals("REMOVE_PLAYER")){
					currentMembers.remove(id);
				}
			}
			
			pst.close();
			
			for(final UUID id : currentMembers.keySet()){
				final String playerName = currentMembers.get(id);
				Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
					@Override
					public void run() {
						HangoutPlayer p = HangoutPlayerManager.getPlayer(id);
						if(p == null){
							p = Database.loadPlayer(id, playerName);
						}
				
						while(true){
							if(p != null && p.isReadyLoading()){
								RpgPlayer rpgP = RpgPlayerManager.getPlayer(id);
								if(rpgP != null){
									g.addPlayer(rpgP, rpgP, false);
									break;
								}
							}
						}
					}
				});
			}
			
			memberCount = currentMembers.size();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		//Experience
		try (PreparedStatement pst = Database.getConnection().prepareStatement(
                "SELECT sum(experience) as 'exp_sum' FROM " + Config.databaseName + ".guildexperience_action WHERE guild_id = ?;")) {
			pst.setInt(1, g.getID());
			ResultSet rs = pst.executeQuery();
			
			int experience = 0;
			if(rs.first()){
				experience = rs.getInt("exp_sum");
			}
			
			pst.close();
			
			g.addExperience(experience, "DATABASE", false);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
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
		
		DebugUtils.sendDebugMessage("Loading complete: " + memberCount + " members");
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
