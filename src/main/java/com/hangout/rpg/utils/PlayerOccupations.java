package com.hangout.rpg.utils;

import java.util.Arrays;
import java.util.List;

import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.PlayerStats;

public enum PlayerOccupations {
	CRAFTER("Crafter", Arrays.asList(
			"Become way better at crafting", "items that are used regularly", "by having a chance to get", "more items per try.")),
	GATHERER("Gatherer", Arrays.asList(
			"With their dexterous hands,", "gatherers can gather raw", "crafting materials with more", "efficiency, sometimes gaining", "double the items.")),
	WARRIOR("Warrior", Arrays.asList(
			"Excels at combat with", "blades coated in poison and", "more fortitude.")),
	FISHERMAN("Fisherman", Arrays.asList(
			"Devoting their lives near", "any place that is near water,", "fishermen catch more fish", "at once and maybe even rare", "items not found anywhere else.")),
	FARMER("Farmer", Arrays.asList(
			"Their knowledge of agriculture", "is impeccible, allowing", "them to harvest more crops", "than any other occupation.")),
	MINER("Miner", Arrays.asList(
			"Dwellers of the underground,", "they know all about ores", "and the ways of extracting", "them efficiently from the earth.")),
	COOK("Cook", Arrays.asList(
			"Masters of food and taste,", "cooking with smartly with", "the ingredients given, they", "can cook more with less.")),
	TINKER("Tinker", Arrays.asList(
			"With mechanical glory and", "a technical approach, the Tinker", "has the chance to create", "great amount of tech in", "a short amount of time.")),
	BLACKSMITH("Blacksmith", Arrays.asList(
			"Their experienced, strong hands", "are capable of creating", "great armor and weapons with", "their advanced forges."));
	
	String displayName;
	List<String> description;
	PlayerOccupations(String displayName, List<String> description){
		this.displayName = displayName;
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public void addStatUntil(PlayerStats stats, int level){
		for(int i = 0; i < level; i++){
			addStat(stats, i);
		}
	}
	
	public void addStat(PlayerStats stats, int level){
		if(this == PlayerOccupations.CRAFTER){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 25);
				stats.addStat(PlayerStat.CRAFT_BUILDING_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 9);
				stats.addStat(PlayerStat.CRAFT_BUILDING_AMOUNT, 50);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_BUILDING_AMOUNT, 50);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			}
		} else if(this == PlayerOccupations.COOK){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.COOK_SPECIAL, 10);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 25);
				stats.addStat(PlayerStat.COOK_INCREASE_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.COOK_SPECIAL, 9);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.COOK_SPECIAL, 7);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_AMOUNT, 100);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.COOK_SPECIAL, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_CHANCE, 2);
				stats.addStat(PlayerStat.COOK_INCREASE_AMOUNT, 100);
				break;
			}
			
		} else if(this == PlayerOccupations.WARRIOR){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.HEALTH, 1);
				stats.addStat(PlayerStat.DAMAGE_MELEE, 1);
				stats.addStat(PlayerStat.DAMAGE_RANGED, 1);
				stats.addStat(PlayerStat.REPUTATION_ON_KILL, 1);
				stats.addStat(PlayerStat.POISON_CHANCE, 10);
				stats.addStat(PlayerStat.POISON_DURATION, 2);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.POISON_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.HEALTH, 1);
				stats.addStat(PlayerStat.POISON_CHANCE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.POISON_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.HEALTH, 1);
				stats.addStat(PlayerStat.DAMAGE_MELEE, 1);
				stats.addStat(PlayerStat.DAMAGE_RANGED, 1);
				stats.addStat(PlayerStat.REPUTATION_ON_KILL, 1);
				stats.addStat(PlayerStat.POISON_CHANCE, 7);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.POISON_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.HEALTH, 1);
				stats.addStat(PlayerStat.DAMAGE_MELEE, 1);
				stats.addStat(PlayerStat.DAMAGE_RANGED, 1);
				stats.addStat(PlayerStat.POISON_CHANCE, 2);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
			case 30:
				stats.addStat(PlayerStat.POISON_CHANCE, 2);
				break;
			}
		} else if(this == PlayerOccupations.FISHERMAN){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 25);
				stats.addStat(PlayerStat.FISHING_CATCH_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				stats.addStat(PlayerStat.FISHINGROD_DURABILITY, -100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				stats.addStat(PlayerStat.FISHING_SPECIAL_CATCH, 20);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.FISHING_CATCH_INCREASE, 2);
				stats.addStat(PlayerStat.FISHING_SPECIAL_CATCH, 10);
				break;
			}
		}else if (this == PlayerOccupations.FARMER){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 25);
				stats.addStat(PlayerStat.DROP_AMOUNT_CROP, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				stats.addStat(PlayerStat.HOE_DURABILITY, -100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_CROP, 50);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.DROP_INCREASE_CROP, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_CROP, 50);
				break;
			}
		}else if (this == PlayerOccupations.MINER){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 25);
				stats.addStat(PlayerStat.DROP_AMOUNT_ORE, 100);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 25);
				stats.addStat(PlayerStat.SMELT_INCREASE_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 9);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				stats.addStat(PlayerStat.PICKAXE_DURABILITY, -100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_ORE, 100);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_AMOUNT, 100);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_ORE, 100);
				stats.addStat(PlayerStat.SMELT_INCREASE_CHANCE, 2);
				stats.addStat(PlayerStat.SMELT_INCREASE_AMOUNT, 100);
				break;
			}
		} else if( this == PlayerOccupations.GATHERER){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 25);
				stats.addStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 9);
				stats.addStat(PlayerStat.SPADE_DURABILITY, -100);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL, 100);
				stats.addStat(PlayerStat.AXE_DURABILITY, -100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL, 100);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL, 100);
				break;
			}
		} else if( this == PlayerOccupations.TINKER){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 25);
				stats.addStat(PlayerStat.CRAFT_MECHANICS_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_MECHANICS_AMOUNT, 100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_MECHANICS_AMOUNT, 100);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.CRAFT_MECHANICS_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_MECHANICS_AMOUNT, 100);
				break;
			}
		} else if( this == PlayerOccupations.BLACKSMITH){
			switch(level){
			case 1:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 25);
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_AMOUNT, 100);
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_AMOUNT, 100);
				break;
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
			case 19:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				break;
			case 20:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_AMOUNT, 100);
				break;
			case 21:
			case 22:
			case 23:
			case 24:
			case 25:
			case 26:
			case 27:
			case 28:
			case 29:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				break;
			case 30:
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE, 2);
				stats.addStat(PlayerStat.CRAFT_BLACKSMITH_AMOUNT, 100);
				break;
			}
		}
	}
}
