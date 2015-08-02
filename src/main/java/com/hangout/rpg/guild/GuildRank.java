package com.hangout.rpg.guild;

public enum GuildRank {
	NEWBIE("Newbie", 1),
	VETERAN("Veteran", 2),
	OFFICER("Officer", 3),
	LEADER("Leader", 4);
	
	private String displayName;
	private int rank;
	GuildRank(String displayName, int rank){
		this.displayName = displayName;
		this.rank = rank;
	}
	
	public String getDefaultName(){
		return displayName;
	}
	
	public int getRank(){
		return rank;
	}
	
	public boolean canInvite(){
		if(getRank() >= 1){
			return true;
		}
		return false;
	}
	
	public boolean canKick(GuildRank rank){
		if(getRank() >= 2){
			return true;
		}
		return false;
	}
	
	public boolean canPromote(GuildRank rank){
		if(difference(rank) >= 2){
			return true;
		}
		return false;
	}
	
	public boolean canDemote(GuildRank rank){
		if(difference(rank) >= 1 && rank.getRank() != 1){
			return true;
		}
		return false;
	}
	
	public int difference(GuildRank rank){
		return getRank() - rank.getRank();
	}
	
	public static GuildRank getNextRank(GuildRank r){
		for(GuildRank rank : GuildRank.values()){
			if(rank.getRank() == r.getRank() + 1){
				return rank;
			}
		}
		return null;
	}
	
	public static GuildRank getPreviousRank(GuildRank r){
		for(GuildRank rank : GuildRank.values()){
			if(rank.getRank() == r.getRank() - 1){
				return rank;
			}
		}
		return null;
	}
}