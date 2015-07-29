package com.hangout.rpg.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;

import com.hangout.core.HangoutAPI;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.utils.Experience;
import com.hangout.rpg.utils.OccupationAction;
import com.hangout.rpg.utils.PlayerOccupations;
import com.hangout.rpg.utils.PlayerRaces;

public class RpgPlayer {
	
	private HangoutPlayer hp;
	private Experience experience = new Experience();
	private PlayerRaces race = PlayerRaces.ELF;
	private PlayerOccupations occupation = PlayerOccupations.WARRIOR;
	private List<PlayerOccupations> unlockedOccupations = new ArrayList<PlayerOccupations>();
	private Guild guild = null;

	public RpgPlayer(HangoutPlayer hp) {
		this.hp = hp;
		updateDescription();
	}
	
	public HangoutPlayer getHangoutPlayer(){
		return hp;
	}
	
	public void addExperience(int exp, boolean commitToDatabase, String source){
		experience.addExperience(exp);
		if(commitToDatabase){
			RpgPlayerManager.commitExperienceAction(this, source, exp);
		}
		updateDescription();
		
		HangoutAPI.sendDebugMessage(hp.getName() + " gained " + exp + " experience from " + source);
	}
	
	public int getExperience(){
		return experience.getExperience();
	}
	
	public int getLevel(){
		return experience.getLevel();
	}
	
	public PlayerRaces getRace(){
		return race;
	}
	
	public void setRace(PlayerRaces race){
		this.race = race;
		updateDescription();
	}
	
	public PlayerOccupations getOccupation(){
		return occupation;
	}
	
	public void setOccupation(PlayerOccupations occupation, boolean commitToDatabase){
		this.occupation = occupation;
		if(commitToDatabase){
			RpgPlayerManager.commitOccupationAction(this, OccupationAction.SWITCH, occupation);
		}
		updateDescription();
		
		HangoutAPI.sendDebugMessage(hp.getName() + " set occupation to " + occupation.toString());
	}
	
	//Exists only for reflection
	public void unlockOccupation(String s){
		PlayerOccupations occupation = PlayerOccupations.valueOf(s);
		unlockOccupation(occupation);
	}
	
	public void unlockOccupation(PlayerOccupations occupation){
		if(!unlockedOccupations.contains(occupation)){
			unlockedOccupations.add(occupation);
			RpgPlayerManager.commitOccupationAction(this, OccupationAction.ADD, occupation);
		}
	}
	
	public void updateDescription(){
		hp.setDescription(Arrays.asList(
				ChatColor.RED + "Race: " + ChatColor.WHITE + ChatColor.ITALIC + race.getDisplayName(),
				ChatColor.RED + "Level: " + ChatColor.WHITE + ChatColor.ITALIC + getLevel(),
				ChatColor.RED + "Occupation: " + ChatColor.WHITE + ChatColor.ITALIC + occupation.getDisplayName()));
	}
}
