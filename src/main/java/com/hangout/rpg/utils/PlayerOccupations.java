package com.hangout.rpg.utils;

public enum PlayerOccupations {
	ADVENTURER("Adventurer"),
	SCOUT("Scout"),
	BUILDER("Builder"),
	WARRIOR("Warrior");
	
	String displayName;
	PlayerOccupations(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
