package com.hangout.rpg.player;

import java.util.ArrayList;
import java.util.HashMap;
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
	private HashMap<PlayerOccupations, Experience> experience = new HashMap<PlayerOccupations, Experience>();
	private PlayerRaces race = PlayerRaces.ELF;
	private PlayerOccupations occupation = PlayerOccupations.WARRIOR;
	private List<PlayerOccupations> unlockedOccupations = new ArrayList<PlayerOccupations>();
	private Guild guild = null;
	private PlayerStats stats = new PlayerStats(this);

	public RpgPlayer(HangoutPlayer hp) {
		this.hp = hp;
		
		for(PlayerOccupations o : PlayerOccupations.values()){
			experience.put(o, new Experience(this, o, 100));
		}
		
		updateDescription();
	}
	
	public HangoutPlayer getHangoutPlayer(){
		return hp;
	}
	
	public void addExperience(int exp, boolean commitToDatabase, String source, PlayerOccupations o){
		if(guild != null && guild.hasBonusActive(GuildBonusType.EXPERIENCE)){
			exp += (exp / 10);
		}
		
		Experience e = experience.get(o);
		if(e == null){
			e = new Experience(this, o, 100);
		}
		e.addExperience(exp);
		experience.put(o, e);
		
		if(commitToDatabase){
			RpgPlayerManager.commitExperienceAction(this, source, o, exp);
		}
		updateDescription();
		
		DebugUtils.sendDebugMessage(hp.getName() + " gained " + exp + " experience from " + source, DebugMode.DEBUG);
	}
	
	public int getExperience(PlayerOccupations o){
		return experience.get(o).getExperience();
	}
	
	public int getLevel(PlayerOccupations o){
		return experience.get(o).getLevel();
	}
	
	public int getExperienceToNextLevel(PlayerOccupations o){
		return experience.get(o).getExpToNextLevel();
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
		
		if(g != null){
			DebugUtils.sendDebugMessage("Added player " + getHangoutPlayer().getName() + " to guild " + g.getName(), DebugMode.INFO);
		}else{
			DebugUtils.sendDebugMessage("Removed player " + getHangoutPlayer().getName() + " from guild.", DebugMode.INFO);
		}
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
			updateStats();
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
	
	public List<String> getOccupationDescriptions(){
		List<String> list = new ArrayList<String>();
		for(PlayerOccupations o : experience.keySet()){
			list.add(ChatColor.RED + o.getDisplayName() + ChatColor.WHITE + " level " + 
					getLevel(o) + " - Exp " + getExperience(o) + "/" + getExperienceToNextLevel(o));
		}
		return list;
	}
	
	public void updateDescription(){
		List<String> description = new ArrayList<String>();
		description.add(ChatColor.RED + "Race: " + ChatColor.WHITE + ChatColor.ITALIC + race.getDisplayName());
		description.add(ChatColor.RED + "Occupation: " + ChatColor.WHITE + ChatColor.ITALIC + occupation.getDisplayName());
		description.add(ChatColor.RED + "Level: " + ChatColor.WHITE + ChatColor.ITALIC + getLevel(occupation));
		description.add(ChatColor.RED + "Experience: " + ChatColor.WHITE + ChatColor.ITALIC + getExperience(occupation) + "/" + getExperienceToNextLevel(occupation));
		if(guild != null){
			description.add(" ");
			description.add(ChatColor.RED + "Guild: " + ChatColor.WHITE + ChatColor.ITALIC + getGuild().getName());
			description.add(ChatColor.RED + "Rank: " + ChatColor.WHITE + ChatColor.ITALIC + getGuild().getRankName(getGuild().getPlayerRank(this)));
		}
		
		hp.setDescription(description);
	}
	
	public void updateStats(){
		stats.update();
		stats.apply();
	}
	
	public PlayerStats getStats(){
		return stats;
	}
}
