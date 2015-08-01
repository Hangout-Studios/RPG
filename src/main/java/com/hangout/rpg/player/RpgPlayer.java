package com.hangout.rpg.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.DebugUtils;
import com.hangout.core.utils.mc.DebugUtils.DebugMode;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.guild.GuildBonusType;
import com.hangout.rpg.utils.Experience;
import com.hangout.rpg.utils.OccupationAction;
import com.hangout.rpg.utils.PlayerOccupations;
import com.hangout.rpg.utils.PlayerRaces;

public class RpgPlayer {
	
	private HangoutPlayer hp;
	private Experience experience = new Experience(this, 100);
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
		if(guild != null && guild.hasBonusActive(GuildBonusType.EXPERIENCE)){
			exp += (exp / 10);
		}
		
		experience.addExperience(exp);
		if(commitToDatabase){
			RpgPlayerManager.commitExperienceAction(this, source, exp);
		}
		updateDescription();
		
		DebugUtils.sendDebugMessage(hp.getName() + " gained " + exp + " experience from " + source, DebugMode.DEBUG);
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
	
	public void setGuild(Guild g){
		this.guild = g;
		
		DebugUtils.sendDebugMessage("Added player " + getHangoutPlayer().getName() + " to guild " + g.getName(), DebugMode.INFO);
		updateDescription();
	}
	
	public Guild getGuild(){
		return guild;
	}
	
	public boolean isInGuild(){
		if(guild != null){
			return true;
		}else{
			return false;
		}
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
		
		DebugUtils.sendDebugMessage(hp.getName() + " set occupation to " + occupation.toString(), DebugMode.INFO);
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
		List<String> description = new ArrayList<String>();
		description.add(ChatColor.RED + "Race: " + ChatColor.WHITE + ChatColor.ITALIC + race.getDisplayName());
		description.add(ChatColor.RED + "Level: " + ChatColor.WHITE + ChatColor.ITALIC + getLevel());
		description.add(ChatColor.RED + "Occupation: " + ChatColor.WHITE + ChatColor.ITALIC + occupation.getDisplayName());
		if(guild != null){
			description.add(ChatColor.RED + "Guild: " + ChatColor.WHITE + ChatColor.ITALIC + getGuild().getName());
		}
		
		hp.setDescription(description);
	}
}
