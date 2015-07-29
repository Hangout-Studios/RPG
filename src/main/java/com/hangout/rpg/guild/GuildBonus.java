package com.hangout.rpg.guild;

import org.joda.time.DateTime;

public class GuildBonus {
	
	private GuildBonusType type;
	private DateTime expireTime;
	public GuildBonus(GuildBonusType type){
		this.type = type;
	}
	
	public void activate(Guild g, DateTime until, boolean commitToDatabase) {
		expireTime = until;
		
		if(commitToDatabase){
			GuildManager.executeGuildBonusAction(g, "ENABLE", this);
		}
	}
	
	public GuildBonusType getType(){
		return type;
	}
	
	public boolean isActive(){
		if(expireTime == null){
			return false;
		}
		return true;
	}
}
