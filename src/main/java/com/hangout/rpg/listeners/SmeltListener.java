package com.hangout.rpg.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.FurnaceManager;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.ExperienceTable;
import com.hangout.rpg.utils.PlayerOccupations;

public class SmeltListener implements Listener {
	
	@EventHandler
	public void onFurnaceSmelt(FurnaceSmeltEvent e){
		
		UUID id = FurnaceManager.getPlayer(e.getBlock());
		
		if(id == null) return;
		
		RpgPlayer p = RpgPlayerManager.getPlayer(id);
		
		ItemStack result = e.getResult();
		ItemStack source = e.getSource();
		
		
		
		if(ItemUtils.isFood(result.getType())){
			p.addExperience(ExperienceTable.getCraftingExperience(result.getType()), true, "COOK_FOOD_" + source.getType().toString(), PlayerOccupations.COOK);
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.COOK_INCREASE_CHANCE))){
				result.setAmount(NumberUtils.multiplyByPercentage(result.getAmount(), p.getStats().getStat(PlayerStat.COOK_INCREASE_AMOUNT)));
				e.setResult(result);
				return;
			}
		}
		
		if(ItemUtils.isOre(source.getType())){
			p.addExperience(ExperienceTable.getCraftingExperience(result.getType()), true, "SMELT_ORE_" + source.getType().toString(), PlayerOccupations.MINER);
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.SMELT_INCREASE_CHANCE))){
				result.setAmount(NumberUtils.multiplyByPercentage(result.getAmount(), p.getStats().getStat(PlayerStat.SMELT_INCREASE_AMOUNT)));
				e.setResult(result);
				return;
			}
		}
	}
	
}
