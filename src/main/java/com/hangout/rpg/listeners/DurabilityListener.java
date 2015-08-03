package com.hangout.rpg.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class DurabilityListener implements Listener{
	
	@EventHandler
	public void onPlayerItemDamage(PlayerItemDamageEvent e){
		RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUniqueId());
		Material toolMat = e.getItem().getType();
		if(ItemUtils.isAxe(toolMat)){
			e.setDamage(e.getDamage() * p.getStats().getStat(PlayerStat.AXE_DURABILITY));
		}
		if(ItemUtils.isPickaxe(toolMat)){
			e.setDamage(e.getDamage() * p.getStats().getStat(PlayerStat.PICKAXE_DURABILITY));
		}
		if(ItemUtils.isHoe(toolMat)){
			e.setDamage(e.getDamage() * p.getStats().getStat(PlayerStat.HOE_DURABILITY));
		}
		if(ItemUtils.isSpade(toolMat)){
			e.setDamage(e.getDamage() * p.getStats().getStat(PlayerStat.SPADE_DURABILITY));
		}
		if(toolMat == Material.FISHING_ROD){
			e.setDamage(e.getDamage() * p.getStats().getStat(PlayerStat.FISHINGROD_DURABILITY));
		}
	}
}
