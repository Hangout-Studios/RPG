package com.hangout.rpg.player;

import com.hangout.rpg.player.PlayerStats.PlayerStatTypes;

public enum PlayerStat {
	//COMBAT STATS
	HEALTH(PlayerStatTypes.COMBAT, "Health", "", false, false),
	MOVESPEED(PlayerStatTypes.COMBAT, "Movespeed", "", false, false),
	DAMAGE_MELEE(PlayerStatTypes.COMBAT, "Melee damage", "", false, false),
	DAMAGE_RANGED(PlayerStatTypes.COMBAT, "Ranged damage", "", false, false),
	REPUTATION_ON_KILL(PlayerStatTypes.COMBAT, "Reputation on kill", "", true, false),
	POISON_CHANCE(PlayerStatTypes.COMBAT, "Poison on hit", "%", true, true),
	POISON_DURATION(PlayerStatTypes.COMBAT, "Poison duration", "s", true, false),
	
	//GATHERING
	DROP_INCREASE_ORE(PlayerStatTypes.GATHERING, "Ore multiplier chance", "%", true, true),
	DROP_AMOUNT_ORE(PlayerStatTypes.GATHERING, "Ore multiplier", "%", true, false),
	DROP_INCREASE_CROP(PlayerStatTypes.GATHERING, "Crop multiplier chance", "%", true, true),
	DROP_AMOUNT_CROP(PlayerStatTypes.GATHERING, "Crop multiplier", "%", true, false),
	DROP_INCREASE_BUILD_MATERIAL(PlayerStatTypes.GATHERING, "Material multiplier chance", "%", true, true),
	DROP_AMOUNT_BUILD_MATERIAL(PlayerStatTypes.GATHERING, "Material multiplier", "%", true, false),
	FISHING_CATCH_INCREASE(PlayerStatTypes.GATHERING, "Fishing multiplier chance", "%", true, true),
	FISHING_CATCH_AMOUNT(PlayerStatTypes.GATHERING, "Fishing multiplier", "%", true, false),
	FISHING_SPECIAL_CATCH(PlayerStatTypes.GATHERING, "Fishing special chance", "%", true, false),
	PICKAXE_DURABILITY(PlayerStatTypes.GATHERING, "Pickaxe durability", "%", false, false),
	AXE_DURABILITY(PlayerStatTypes.GATHERING, "Axe durability", "%", false, false),
	SPADE_DURABILITY(PlayerStatTypes.GATHERING, "Spade durability", "%", false, false),
	HOE_DURABILITY(PlayerStatTypes.GATHERING, "Hoe durability", "%", false, false),
	FISHINGROD_DURABILITY(PlayerStatTypes.GATHERING, "Fishing rod durability", "%", false, false),

	//CRAFTING
	CRAFT_BUILDING_CHANCE(PlayerStatTypes.CRAFTING, "Craft basic multiplier chance", "%", true, true),
	CRAFT_BUILDING_AMOUNT(PlayerStatTypes.CRAFTING, "Craft basic multiplier", "%", true, false),
	CRAFT_MECHANICS_CHANCE(PlayerStatTypes.CRAFTING, "Craft mech multiplier chance", "%", true, true),
	CRAFT_MECHANICS_AMOUNT(PlayerStatTypes.CRAFTING, "Craft mech multiplier", "%", true, false),
	CRAFT_BLACKSMITH_CHANCE(PlayerStatTypes.CRAFTING, "Craft equipment multiplier chance", "%", true, true),
	CRAFT_BLACKSMITH_AMOUNT(PlayerStatTypes.CRAFTING, "Craft equipment multiplier", "%", true, false),
	SMELT_INCREASE_CHANCE(PlayerStatTypes.CRAFTING, "Ore smelt multiplier chance", "%", true, true),
	SMELT_INCREASE_AMOUNT(PlayerStatTypes.CRAFTING, "Ore smelt multiplier", "%", true, false),
	COOK_INCREASE_CHANCE(PlayerStatTypes.CRAFTING, "Cook multiplier chance", "%", true, true),
	COOK_INCREASE_AMOUNT(PlayerStatTypes.CRAFTING, "Cook multiplier", "%", true, false),
	COOK_SPECIAL(PlayerStatTypes.CRAFTING, "Cook special chance", "%", true, false);
	
	private PlayerStatTypes type;
	private String displayName;
	private String suffix;
	private boolean canSkipNext;
	private boolean isSkippable;
	PlayerStat(PlayerStatTypes type, String displayName, String suffix, boolean isSkippable, boolean canSkipNext){
		this.type = type;
		this.displayName = displayName;
		this.suffix = suffix;
		this.canSkipNext = canSkipNext;
		this.isSkippable = isSkippable;
	}
	
	public PlayerStatTypes getType(){
		return type;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public String getSuffix(){
		return suffix;
	}
	
	public boolean isSkippable(){
		return isSkippable;
	}
	
	public boolean skipNextIfZero(){
		return canSkipNext;
	}
}