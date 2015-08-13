package com.hangout.rpg;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.item.CustomItem;
import com.hangout.core.item.CustomItemManager;
import com.hangout.core.item.CustomItemRarity;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.utils.ExperienceTable;

public class Config {

	public static void loadData() {
		FileConfiguration config = Plugin.getInstance().getConfig();
		
		//Experience table
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
		
		ConfigurationSection items = config.getConfigurationSection("Custom_Items");
		for(String tag : items.getKeys(false)){
			/*
			Material: "DIRT"
			Name: "Dirt"
			Description:
			- "Found everywhere around"
			- "the world."
			Replace_Default: true
			*/
			
			ConfigurationSection section = items.getConfigurationSection(tag);
			
			Material mat = Material.valueOf(section.getString("Material"));
			String displayName = section.getString("Name", mat.name());
			List<String> description = section.getStringList("Description");
			boolean replaceDefault = section.getBoolean("Replace_Default");
			boolean useEvent = section.getBoolean("Use_Event", false);
			boolean rightClickEvent = section.getBoolean("Inventory_Click_Event", false);
			boolean dropable = section.getBoolean("Dropable", true);
			boolean dropOnDeath = section.getBoolean("Drop_On_Death", true);
			CustomItemRarity rarity = CustomItemRarity.valueOf(section.getString("Rarity", "COMMON"));
			
			ItemStack item = ItemUtils.createItem(mat, displayName, description);
			
			if(replaceDefault){
				CustomItemManager.addDefaultItem(new CustomItem(item, tag, useEvent, rightClickEvent, dropable, dropOnDeath, rarity));
			}else{
				CustomItemManager.addItem(new CustomItem(item, tag, useEvent, rightClickEvent, dropable, dropOnDeath, rarity));
			}
		}
	}
}
