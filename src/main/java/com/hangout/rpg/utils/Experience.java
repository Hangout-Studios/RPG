package com.hangout.rpg.utils;

import com.hangout.core.HangoutAPI;

public class Experience {
	
	private int experience = 0;
	private int level = 0; //Cache this value
	
	public Experience(){
		
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getExperience(){
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
		int i = 0;
		while(experience >= getExpToLevel(i)){
			i++;
		}
		this.level = i;
		HangoutAPI.sendDebugMessage("Level updated to " + i);
	}
	
	public int getExpToLevel(int level){
		return ((10 * level) + (level * (level * 2)) * 100);
	}
}
