package com.hangout.rpg.guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
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
	private List<RpgPlayer> players = new ArrayList<RpgPlayer>();
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
		return players;
	}
	
	public List<String> getDescription(){
		List<String> list = new ArrayList<String>();
		
		list.add("Tag: " + getTag());
		list.add("Member count: " + getMembers().size());
		list.add("Level: " + getLevel());
		list.add("Experience: " + experience.getExperience() + "/" + experience.getExpToNextLevel());
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public void addPlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(!players.contains(p)){
			if(players.size() >= sizeLimit){
				executor.getHangoutPlayer().getPlayer().sendMessage("The guild is full.");
			}
			players.add(p);
			
			p.setGuild(this);
			
			if(commitToDatabase){
				for(RpgPlayer guildie : players){
					if(guildie.getHangoutPlayer().isOnline()){
						HashMap<String, Object> nameConfig =  p.getHangoutPlayer().getClickableNameConfig(guildie.getHangoutPlayer());
						new FancyMessage("Please welcome ")
						.then((String)nameConfig.get("text"))
							.color((ChatColor)nameConfig.get("color"))
							.style((ChatColor[])nameConfig.get("styles"))
							.command((String)nameConfig.get("command"))
							.tooltip((List<String>)nameConfig.get("tooltip"))
						.then(" to the guild!")
						.send(guildie.getHangoutPlayer().getPlayer());
					}
				}
			
				GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "ADD_PLAYER");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void removePlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(players.contains(p)){
			players.remove(p);
			
			p.setGuild(null);
			
			HashMap<String, Object> nameConfig =  executor.getHangoutPlayer().getClickableNameConfig(executor.getHangoutPlayer());
			
			p.getHangoutPlayer().getPlayer().sendMessage("You have been removed from the guild by ");
			new FancyMessage("You have been removed from the guild by ")
				.then((String)nameConfig.get("text"))
					.color((ChatColor)nameConfig.get("color"))
					.style((ChatColor[])nameConfig.get("styles"))
					.command((String)nameConfig.get("command"))
					.tooltip((List<String>)nameConfig.get("tooltip"))
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
