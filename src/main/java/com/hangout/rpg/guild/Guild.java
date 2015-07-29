package com.hangout.rpg.guild;

import java.util.ArrayList;
import java.util.List;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.rpg.player.RpgPlayer;

public class Guild {
	
	/*
	 * Size limit
		Guild players
		Guild experience
		Guild currency(?)
		Guild bonuses
	 */
	
	private String name;
	private String tag;
	private int sizeLimit = 5;
	private List<HangoutPlayer> players = new ArrayList<HangoutPlayer>();
	private int experience = 0;
	private List<GuildBonus> bonusses = new ArrayList<GuildBonus>();
	
	public Guild(String name, String tag){
		this.name = name;
		this.tag = tag;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTag(){
		return tag;
	}
	
	public void increaseSizeLimit(RpgPlayer p){
		int price = getSizeUpgradePrice();
		if(p.getHangoutPlayer().getCurrency() >= price){
			sizeLimit += 5;
			p.getHangoutPlayer().modifyGold(-price, "INCREASE_GUILD_SIZE", true);
		}
	}
	
	public int getSizeUpgradePrice(){
		return (sizeLimit % 5) * 1000;
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
			b.activate();
			p.getHangoutPlayer().getPlayer().sendMessage(type.getDisplayName() + " succesfully activated!");
		}
	}
	
	public void buyBonus(GuildBonusType type, RpgPlayer p){
		if(p.getHangoutPlayer().getCurrency() >= type.getPrice()){
			bonusses.add(new GuildBonus(type));
			p.getHangoutPlayer().modifyGold(-type.getPrice(), "BUY_GUILD_BONUS_"+type.toString(), true);
		}
	}
}
