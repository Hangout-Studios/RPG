package com.hangout.rpg.utils;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class ExperienceTable {
	
	private static HashMap<EntityType, Integer> mobs = new HashMap<EntityType, Integer>();
	private static HashMap<Material, Integer> crafting = new HashMap<Material, Integer>();
	private static HashMap<Material, Integer> gathering = new HashMap<Material, Integer>();
	
	public static int getMobExperience(EntityType type){
		if(mobs.containsKey(type)){
			return mobs.get(type);
		}
		return 1;
	}
	
	public static int getCraftingExperience(Material material){
		if(crafting.containsKey(material)){
			return crafting.get(material);
		}
		return 1;
	}
	
	public static int getGatheringExperience(Material material){
		if(gathering.containsKey(material)){
			return gathering.get(material);
		}
		return 1;
	}
	
	public static void addMob(EntityType t, int i){
		mobs.put(t, i);
	}
	
	public static void addCrafting(Material m, int i){
		crafting.put(m, i);
	}
	
	public static void addGathering(Material m, int i){
		gathering.put(m, i);
	}
}
