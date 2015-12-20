package com.hangout.rpg.utils;

import java.util.Arrays;
import java.util.List;

public enum PlayerRaces {
	HUMAN("Human", Arrays.asList(
			"Humans are stronger and have",
			"developed advanced agricultural",
			"methods to increase farming efficiency.",
			"",
			"Bonusses:",
			"+2 Melee attack damage",
			"50$ chance to drop 2x crops")),
	DWARF("Dwarf", Arrays.asList(
			"Dwarves live mainly underground and",
			"have a very sturdy physique as well as",
			"exceptional knowledge of ores.",
			"",
			"Bonusses:",
			"+2 Health",
			"50$ chance to drop 2x ores")),
	ELF("Elf", Arrays.asList(
			"Elves are expert hunters and",
			"have a connection to nature",
			"like no other race.",
			"",
			"Bonusses:",
			"+2 Ranged attack damage",
			"50$ chance to drop 2x resources")),
	GNOME("Gnome", Arrays.asList(
			"Gnomes are small in stature and are",
			"in turn deceptively clever and fast.",
			"",
			"Bonusses:",
			"+10% movement speed",
			"50$ chance to craft 2x outcome"));
	
	String displayName;
	List<String> description;
	PlayerRaces(String displayName, List<String> description){
		this.displayName = displayName;
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public List<String> getDescription(){
		return description;
	}
}
