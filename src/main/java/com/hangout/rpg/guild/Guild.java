package com.hangout.rpg.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mkremins.fanciful.FancyMessage;
import net.md_5.bungee.api.ChatColor;

import org.joda.time.DateTime;

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
	
	public Guild(int id, String name, String tag){
		this.id = id;
		this.name = name;
		this.tag = tag;
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
		
		list.add(ChatColor.WHITE + "Tag: " + ChatColor.WHITE + ChatColor.ITALIC + getTag());
		list.add(ChatColor.WHITE + "Member count: " + ChatColor.WHITE + ChatColor.ITALIC + getMembers().size());
		list.add(ChatColor.WHITE + "Level: " + ChatColor.WHITE + ChatColor.ITALIC + getLevel());
		list.add(ChatColor.WHITE + "Experience: " + ChatColor.WHITE + ChatColor.ITALIC + experience.getExperience() + "/" + experience.getExpToNextLevel());
		
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
			}
		}
	}
	
	public void removePlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(players.containsKey(p)){
			players.remove(p);
			
			p.setGuild(null);
			
			executor.getHangoutPlayer().addClickableName(new FancyMessage("You have been removed from the guild by "), p.getHangoutPlayer())
				.send(p.getHangoutPlayer().getPlayer());
			
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
	
	public void activateBonus(GuildBonusType type, RpgPlayer p){
		GuildBonus b = null;
		for(GuildBonus bonus : bonusses){
			if(bonus.getType() == type){
				if(bonus.isActive()){
					p.getHangoutPlayer().getPlayer().sendMessage("A bonus of that kind is already active.");
					return;
				}else{
					b = bonus;
					break;
				}
			}
		}
		
		if(b == null){
			p.getHangoutPlayer().getPlayer().sendMessage("You don't have a bonus of this type.");
		}else{
			b.activate(this, DateTime.now().plusDays(1), true);
			p.getHangoutPlayer().getPlayer().sendMessage(type.getDisplayName() + " succesfully activated!");
		}
	}
	
	public void buyBonus(GuildBonusType type, RpgPlayer p){
		if(p.getHangoutPlayer().getCurrency() >= type.getPrice()){
			GuildBonus bonus = new GuildBonus(type);
			bonusses.add(bonus);
			p.getHangoutPlayer().modifyGold(-type.getPrice(), "BUY_GUILD_BONUS_"+type.toString(), true);
			
			GuildManager.executeGuildBonusAction(this, "PURCHASE", bonus);
		}
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
	
	public void addExperience(int exp, String source, boolean commitToDatabase){
		experience.addExperience(exp);
		
		if(commitToDatabase){
			GuildManager.executeGuildExperienceAction(this, exp, source);
		}
	}
	
	public int getLevel(){
		return experience.getLevel();
	}
}
