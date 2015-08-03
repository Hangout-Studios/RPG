package com.hangout.rpg.utils;

import org.bukkit.Bukkit;

import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.rpg.events.GuildLevelUpEvent;
import com.hangout.rpg.events.PlayerLevelUpEvent;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.player.RpgPlayer;

public class Experience {
	
	private Object parent;
	private int maxLevel;
	private int experience = 0;
	private int level = 0; //Cache this value
	private PlayerOccupations occupation = null;
	
	public Experience(Object parent, PlayerOccupations occupation, int maxLevel){
		this.maxLevel = maxLevel;
		this.parent = parent;
		this.occupation = occupation;
		addExperience(1);
	}
	
	public Object getParent(){
		return parent;
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getExperience(){
		return experience - getExpToLevel(level - 1);
	}
	
	public int getTotalExperience(){
		return experience;
	}
	
	public void addExperience(int exp){
		experience += exp;
		updateLevel();
	}
	
	public int getExpToNextLevel(){
		return getExpToLevel(level) - getExpToLevel(level - 1);
	}
	
	private void updateLevel(){
		if(this.level >= maxLevel){
			this.level = maxLevel;
			return;
		}
		
		int i = 0;
		while(experience >= getExpToLevel(i)){
			i++;
		}
		if(i > level){
			this.level = i;
			DebugUtils.sendDebugMessage("Level updated to " + i, DebugMode.DEBUG);
			for(int count = level; count < i; count++){
				if(parent instanceof Guild){
					Bukkit.getPluginManager().callEvent(new GuildLevelUpEvent((Guild)parent, count));
				}else if(parent instanceof RpgPlayer){
					Bukkit.getPluginManager().callEvent(new PlayerLevelUpEvent((RpgPlayer)parent, occupation, count));
				}
			}
		}
	}
	
	public int getExpToLevel(int level){
		return ((level * (level * 2)) * 100);
	}
}
