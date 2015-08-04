package com.hangout.rpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.ExperienceTable;
import com.hangout.rpg.utils.PlayerOccupations;

public class CraftListener implements Listener {
	
	@EventHandler
	public void onCraftItem(CraftItemEvent e){
		Player p = (Player) e.getWhoClicked();
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUniqueId());
		ItemStack item = e.getCurrentItem();
		
		if(ItemUtils.isBuildingBlock(item.getType())){
			rpgP.addExperience(ExperienceTable.getCraftingExperience(item.getType()), true, "CRAFT_BUILDING_" + item.getType().toString(), PlayerOccupations.CRAFTER);
			if(NumberUtils.rollPercentage(rpgP.getStats().getStat(PlayerStat.CRAFT_BUILDING_CHANCE))){
				item.setAmount(NumberUtils.multiplyByPercentage(item.getAmount(), rpgP.getStats().getStat(PlayerStat.CRAFT_BUILDING_AMOUNT)));
				e.setCurrentItem(item);
			}
			return;
		}
		
		if(ItemUtils.isRedstoneComponent(item.getType())){
			rpgP.addExperience(ExperienceTable.getCraftingExperience(item.getType()), true, "CRAFT_MECHANIC_" + item.getType().toString(), PlayerOccupations.TINKER);
			if(NumberUtils.rollPercentage(rpgP.getStats().getStat(PlayerStat.CRAFT_MECHANICS_CHANCE))){
				item.setAmount(NumberUtils.multiplyByPercentage(item.getAmount(), rpgP.getStats().getStat(PlayerStat.CRAFT_MECHANICS_AMOUNT)));
				e.setCurrentItem(item);
			}
			return;
		}
		
		if(ItemUtils.isBlacksmithItem(item.getType())){
			rpgP.addExperience(ExperienceTable.getCraftingExperience(item.getType()), true, "CRAFT_EQUIPMENT_" + item.getType().toString(), PlayerOccupations.BLACKSMITH);
			if(NumberUtils.rollPercentage(rpgP.getStats().getStat(PlayerStat.CRAFT_BLACKSMITH_CHANCE))){
				item.setAmount(NumberUtils.multiplyByPercentage(item.getAmount(), rpgP.getStats().getStat(PlayerStat.CRAFT_BLACKSMITH_AMOUNT)));
				e.setCurrentItem(item);
			}
			return;
		}
		
		if(ItemUtils.isFood(item.getType())){
			rpgP.addExperience(ExperienceTable.getCraftingExperience(item.getType()), true, "COOK_FOOD_" + item.getType().toString(), PlayerOccupations.COOK);
			
			if(NumberUtils.rollPercentage(rpgP.getStats().getStat(PlayerStat.COOK_SPECIAL))){
				
				//Get special food and replace old food
				
			}
			
			if(NumberUtils.rollPercentage(rpgP.getStats().getStat(PlayerStat.COOK_INCREASE_CHANCE))){
				item.setAmount(NumberUtils.multiplyByPercentage(item.getAmount(), rpgP.getStats().getStat(PlayerStat.COOK_INCREASE_AMOUNT)));
				e.setCurrentItem(item);
			}
			return;
		}
	}
}
