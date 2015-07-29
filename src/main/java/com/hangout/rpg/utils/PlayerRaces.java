package com.hangout.rpg.utils;

public enum PlayerRaces {
	HUMAN("Human"),
	DWARF("Dwarf"),
	ELF("Elf");
	
	String displayName;
	PlayerRaces(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
