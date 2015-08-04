package com.hangout.rpg;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import com.hangout.rpg.utils.ExperienceTable;

public class Config {

	public static void loadData() {
		FileConfiguration config = Plugin.getInstance().getConfig();
		
		for(String s : config.getStringList("Experience_Table.Gathering")){
			String[] split = s.split("=");
			ExperienceTable.addGathering(Material.valueOf(split[0]), Integer.valueOf(split[1]));
		}
		
		for(String s : config.getStringList("Experience_Table.Crafting")){
			String[] split = s.split("=");
			ExperienceTable.addCrafting(Material.valueOf(split[0]), Integer.valueOf(split[1]));
		}
		
		for(String s : config.getStringList("Experience_Table.Entities")){
			String[] split = s.split("=");
			ExperienceTable.addMob(EntityType.valueOf(split[0]), Integer.valueOf(split[1]));
		}
	}
}
