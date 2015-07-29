package com.hangout.rpg.guild;

import java.util.ArrayList;
import java.util.List;

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
	
	public void addPlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(!players.contains(p)){
			if(players.size() >= sizeLimit){
				executor.getHangoutPlayer().getPlayer().sendMessage("The guild is full.");
			}
			players.add(p);
			
			p.setGuild(this);
			
			if(commitToDatabase){
				GuildManager.executeGuildMemberAction(this, p.getHangoutPlayer().getUUID(), executor.getHangoutPlayer().getUUID(), "ADD_PLAYER");
			}
		}
	}
	
	public void removePlayer(RpgPlayer executor, RpgPlayer p, boolean commitToDatabase){
		if(players.contains(p)){
			players.remove(p);
			
			p.setGuild(null);
			
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
			b.activate(DateTime.now().plusDays(1), true);
			GuildManager.executeGuildBonusAction(this, "ENABLE", b);
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
