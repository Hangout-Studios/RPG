package com.hangout.rpg.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;

import org.joda.time.DateTime;

import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.utils.Experience;

public class Guild {
	
	/*
	 * Size limit
		Guild players
		Guild experience
		Guild currency(?)
		Guild bonuses
	 */
	
	private int id;
	private String name;
	private String tag;
	private int sizeLimit = 100;
	private HashMap<RpgPlayer, GuildRank> players = new HashMap<RpgPlayer, GuildRank>();
	private Experience experience = new Experience(this, 5);
	private List<GuildBonus> bonusses = new ArrayList<GuildBonus>();
	private HashMap<GuildRank, String> customRanks = new HashMap<GuildRank, String>();
	
	public Guild(int id, String name, String tag){
		this.id = id;
		this.name = name;
		this.tag = tag;
		
		DebugUtils.sendDebugMessage("Created guild " + name + " with tag " + tag, DebugMode.INFO);
	}
	
	public String getName(){
		return name;
	}
	
	public String getTag(){
		return tag;
	}
	
	public int getID(){
		return id;
	}
	
	public void setID(int i){
		id = i;
	}
	
	public List<RpgPlayer> getMembers(){
		return new ArrayList<RpgPlayer>(players.keySet());
	}
	
	public List<String> getDescription(){
		List<String> list = new ArrayList<String>();
		
		list.add(ChatColor.RED + "Tag: " + ChatColor.WHITE + ChatColor.ITALIC + getTag());
		list.add(ChatColor.RED + "Members: " + ChatColor.WHITE + ChatColor.ITALIC + getMembers().size());
		list.add(ChatColor.RED + "Level: " + ChatColor.WHITE + ChatColor.ITALIC + getLevel());
		list.add(ChatColor.RED + "Experience: " + ChatColor.WHITE + ChatColor.ITALIC + experience.getExperience() + "/" + experience.getExpToNextLevel());
		
		return list;
	}
	
	public GuildRank getPlayerRank(RpgPlayer p){
		if(players.containsKey(p)){
			return players.get(p);
		}
		return null;
	}
	
	public void addPlayer(RpgPlayer executor, RpgPlayer p, GuildRank rank, boolean commitToDatabase){
		if(!players.containsKey(p)){
			if(players.size() >= sizeLimit){
				executor.getHangoutPlayer().getPlayer().sendMessage("The guild is full.");
			}
			
			players.put(p, rank);
			p.setGuild(this);
			
			if(commitToDatabase){
				for(RpgPlayer guildie : getMembers()){
					if(guildie.getHangoutPlayer().isOnline()){
						p.getHangoutPlayer().addClickableName(new FancyMessage("Please welcome "), guildie.getHangoutPlayer())
							.then(" to the guild!")
							.send(guildie.getHangoutPlayer().getPlayer());
					}
				}
			
				GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "ADD_PLAYER");
				GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "RANK_" + rank.toString());
			}
		}
	}
	
	public void setRank(RpgPlayer executor, RpgPlayer p, GuildRank r, boolean promote){
		if(!players.containsKey(p) || !players.containsKey(executor)) return;
		
		GuildRank executorRank = getPlayerRank(executor);
		int difference = executorRank.difference(r);
		if(difference >= 1){
			players.put(p, r);
			p.updateDescription();
			
			executor.getHangoutPlayer().getClickableName(p.getHangoutPlayer(), false, false)
				.then(" has " + (promote ? "promoted" : "demoted") + " you to " + getRankName(r) + "!")
				.send(p.getHangoutPlayer().getPlayer());
			
			p.getHangoutPlayer().addClickableName(new FancyMessage("You have  " + (promote ? "promoted" : "demoted") + "  "), executor.getHangoutPlayer())
				.then(" to " + getRankName(r) + "!")
				.send(executor.getHangoutPlayer().getPlayer());
			
			GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "RANK_" + r.toString());
		}else if(difference == 0){
			executor.getHangoutPlayer().getPlayer().sendMessage("You cannot " + (promote ? "promote" : "demote") + " a player to your rank.");
		}else{
			executor.getHangoutPlayer().getPlayer().sendMessage("That player is a higher rank than you.");
		}
	}
	
	public void removePlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(players.containsKey(p)){
			players.remove(p);
			
			p.setGuild(null);
			
			executor.getHangoutPlayer().addClickableName(new FancyMessage("You have been removed from the guild by "), p.getHangoutPlayer())
				.send(p.getHangoutPlayer().getPlayer());
			
			p.getHangoutPlayer().addClickableName(new FancyMessage("You have removed "), executor.getHangoutPlayer())
				.then(" from the guild")
				.send(executor.getHangoutPlayer().getPlayer());
			
			if(commitToDatabase){
				GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "REMOVE_PLAYER");
			}
		}
	}
	
	public void increaseSizeLimit(RpgPlayer p){
		int price = getSizeUpgradePrice();
		if(p.getHangoutPlayer().getCurrency() >= price){
			setSizeLimit(sizeLimit + 5);
			p.getHangoutPlayer().modifyGold(-price, "INCREASE_GUILD_SIZE", true);
		}
	}
	
	public int getSizeUpgradePrice(){
		return (sizeLimit % 5) * 1000;
	}
	
	public void setSizeLimit(int i){
		sizeLimit = i;
	}
	
	public boolean activateBonus(GuildBonusType type, RpgPlayer p){
		GuildBonus b = null;
		for(GuildBonus bonus : bonusses){
			if(bonus.getType() == type){
				if(bonus.isActive()){
					p.getHangoutPlayer().getPlayer().sendMessage("A bonus of that kind is already active.");
					return false;
				}else{
					b = bonus;
					break;
				}
			}
		}
		
		if(b == null){
			p.getHangoutPlayer().getPlayer().sendMessage("You don't have a bonus of this type.");
			return false;
		}else{
			b.activate(this, DateTime.now().plusDays(1), true);
			p.getHangoutPlayer().getPlayer().sendMessage(type.getDisplayName() + " succesfully activated!");
			return true;
		}
	}
	
	public boolean buyBonus(GuildBonusType type, RpgPlayer p){
		if(p.getHangoutPlayer().getCurrency() >= type.getPrice()){
			GuildBonus bonus = new GuildBonus(type);
			bonusses.add(bonus);
			p.getHangoutPlayer().modifyGold(-type.getPrice(), "BUY_GUILD_BONUS_"+type.toString(), true);
			
			GuildManager.executeGuildBonusAction(this, "PURCHASE", bonus);
			
			p.getHangoutPlayer().getPlayer().sendMessage("You have purchased " + type.getDisplayName() + " for " + type.getPrice() + ".");
			return true;
		}
		
		p.getHangoutPlayer().getPlayer().sendMessage("You don't have enough gold. (" + type.getPrice() + "G)");
		return false;
	}
	
	public boolean hasBonusActive(GuildBonusType type){
		if(type.isPermanent() && getLevel() >= type.getRequiredLevel()){
			return true;
		}
		
		for(GuildBonus bonus : bonusses){
			if(bonus.getType() == type){
				if(bonus.isActive()){
					return true;
				}
			}
		}
		return false;
	}
	
	public void setBonusses(List<GuildBonus> bonusses){
		this.bonusses = bonusses;
	}
	
	public int getBoosterAmount(GuildBonusType type){
		int count = 0;
		for(GuildBonus bonus : bonusses){
			if(bonus.getType() == type){
				count++;
			}
		}
		return count;
	}
	
	public void addExperience(int exp, String source, boolean commitToDatabase){
		experience.addExperience(exp);
		
		if(commitToDatabase){
			GuildManager.executeGuildExperienceAction(this, exp, source);
		}
	}
	
	public int getLevel(){
		return experience.getLevel();
	}
	
	public String getRankName(GuildRank r){
		if(customRanks.containsKey(r)){
			return customRanks.get(r);
		}
		return r.getDefaultName();
	}
}
