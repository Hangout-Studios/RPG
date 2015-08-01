package com.hangout.rpg.guild;

public enum GuildRank {
	NEWBIE("Newbie"),
	VETERAN("Veteran"),
	OFFICER("Officer"),
	LEADER("Leader");
	
	private String displayName;
	GuildRank(String displayName){
		this.displayName = displayName;
	}
	
	public String getDefaultName(){
		return displayName;
	}
}