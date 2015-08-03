package com.hangout.rpg.utils;

import java.util.Arrays;

import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.PlayerStats;

public enum PlayerOccupations {
	CRAFTER("Crafter"),
	GATHERER("Gatherer"),
	WARRIOR("Warrior"),
	FISHERMAN("Fisherman"),
	FARMER("Farmer"),
	MINER("Miner"),
	COOK("Cook");
	
	String displayName;
	PlayerOccupations(String displayName){
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
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
				stats.addStat(PlayerStat.SPADE_DURABILITY, -100);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.CRAFT_BUILDING_CHANCE, 2);
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
				break;
			case 2:
			case 3:
			case 4:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				break;
			case 5:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 9);
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				break;
			case 10:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
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
				break;
			case 20:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_ORE, 50);
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
				break;
			case 30:
				stats.addStat(PlayerStat.DROP_INCREASE_ORE, 2);
				stats.addStat(PlayerStat.DROP_AMOUNT_ORE, 50);
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
				stats.addStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL, 100);
				break;
			case 30:
				stats.addStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL, 2);
				break;
			}
		}
	}
}
