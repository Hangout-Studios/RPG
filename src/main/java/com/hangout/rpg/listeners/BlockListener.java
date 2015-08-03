package com.hangout.rpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerOccupations;

public class BlockListener implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		if(p.getOccupation() == PlayerOccupations.BUILDER){
			p.addExperience(1, true, "BLOCK_BREAK_" + e.getBlock().getType().toString(), PlayerOccupations.BUILDER);
		}
		
		if(ItemUtils.isCrop(e.getBlock().getType())){
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.DROP_INCREASE_CROP))){
				for(ItemStack item : e.getBlock().getDrops(e.getPlayer().getItemInHand())){
					ItemStack i = new ItemStack(item.getType(), item.getAmount() * p.getStats().getStat(PlayerStat.DROP_AMOUNT_CROP));
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
				return;
			}
		}
		
		if(ItemUtils.isOre(e.getBlock().getType())){
			if(NumberUtils.rollPercentage(p.getStats().getStat(PlayerStat.DROP_INCREASE_ORE))){
				for(ItemStack item : e.getBlock().getDrops(e.getPlayer().getItemInHand())){
					ItemStack i = new ItemStack(item.getType(), item.getAmount() * p.getStats().getStat(PlayerStat.DROP_AMOUNT_ORE));
					e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), i);
				}
				return;
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
	}
}
