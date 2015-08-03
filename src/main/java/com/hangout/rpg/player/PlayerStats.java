package com.hangout.rpg.player;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.rpg.utils.PlayerRaces;

public class PlayerStats {
	
	private RpgPlayer p;
	private HashMap<PlayerStat, Integer> stats = new HashMap<PlayerStat, Integer>();
	
	public PlayerStats(RpgPlayer p){
		this.p = p;
	}
	
	public void update(){
		stats.clear();
		
		//Default stats
		addStat(PlayerStat.HEALTH, 20);
		addStat(PlayerStat.DAMAGE_MELEE, 2);
		addStat(PlayerStat.DAMAGE_RANGED, 2);
		
		if(p.getRace() == PlayerRaces.DWARF){
			addStat(PlayerStat.HEALTH, 4);
			addStat(PlayerStat.DROP_INCREASE_ORE, 50);
			addStat(PlayerStat.DROP_AMOUNT_ORE, 2);
			
		}else if(p.getRace() == PlayerRaces.ELF){
			addStat(PlayerStat.DAMAGE_RANGED, 2);
			addStat(PlayerStat.MOVESPEED, 10);
			
		}else if(p.getRace() == PlayerRaces.HUMAN){
			addStat(PlayerStat.DAMAGE_MELEE, 2);
			addStat(PlayerStat.DROP_INCREASE_CROP, 50);
			addStat(PlayerStat.DROP_AMOUNT_CROP, 2);
		}
	}
	
	public void apply(){
		Player p = this.p.getHangoutPlayer().getPlayer();
		
		p.setMaxHealth(getStat(PlayerStat.HEALTH));
		p.setWalkSpeed(p.getWalkSpeed() * (1 + (getStat(PlayerStat.MOVESPEED) / 100)));
		
		DebugUtils.sendDebugMessage("Stats applied to " + p.getName(), DebugMode.DEBUG);
	}
	
	private void addStat(PlayerStat stat, int value){
		int i = 0;
		if(stats.containsKey(stat)){
			i = stats.get(stat);
		}
		i += value;
		
		System.out.print("Set stat " + stat.toString() + " to " + value);
		stats.put(stat, i);
	}
	
	public int getStat(PlayerStat stat){
		if(stats.containsKey(stat)){
			return stats.get(stat);
		}
		return 0;
	}
}