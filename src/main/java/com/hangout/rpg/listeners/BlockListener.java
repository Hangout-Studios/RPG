package com.hangout.rpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.ExperienceTable;
import com.hangout.rpg.utils.PlayerOccupations;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		
		if(ItemUtils.isCrop(e.getBlock().getType())){
			p.addExperience(ExperienceTable.getGatheringExperience(e.getBlock().getType()), true, "HARVEST_CROP_" + e.getBlock().getType().toString(), PlayerOccupations.FARMER);
			
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.DROP_INCREASE_CROP))){
				for(ItemStack item : e.getBlock().getDrops(e.getPlayer().getItemInHand())){
					ItemStack i = new ItemStack(item.getType(), NumberUtils.multiplyByPercentage(item.getAmount(), p.getStats().getStat(PlayerStat.DROP_AMOUNT_CROP)));
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
				return;
			}
		}
		
		if(ItemUtils.isOre(e.getBlock().getType())){
			p.addExperience(ExperienceTable.getGatheringExperience(e.getBlock().getType()), true, "MINE_ORE_" + e.getBlock().getType().toString(), PlayerOccupations.MINER);
			
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.DROP_INCREASE_ORE))){
				for(ItemStack item : e.getBlock().getDrops(e.getPlayer().getItemInHand())){
					ItemStack i = new ItemStack(item.getType(), NumberUtils.multiplyByPercentage(item.getAmount(), p.getStats().getStat(PlayerStat.DROP_AMOUNT_ORE)));
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
				return;
			}
		}
		
		if(ItemUtils.isBuildingMaterial(e.getBlock().getType())){
			p.addExperience(ExperienceTable.getGatheringExperience(e.getBlock().getType()), true, "GATHER_BLOCK_" + e.getBlock().getType().toString(), PlayerOccupations.GATHERER);
			
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.DROP_INCREASE_BUILD_MATERIAL))){
				for(ItemStack item : e.getBlock().getDrops(e.getPlayer().getItemInHand())){
					ItemStack i = new ItemStack(item.getType(), NumberUtils.multiplyByPercentage(item.getAmount(), p.getStats().getStat(PlayerStat.DROP_AMOUNT_BUILD_MATERIAL)));
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
				return;
			}
		}
	}
}
