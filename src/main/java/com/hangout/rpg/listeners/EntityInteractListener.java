package com.hangout.rpg.listeners;

import java.util.ArrayList;
import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.npc.NPCManager;
import com.hangout.core.utils.npc.NPCManager.NPCData;
import com.hangout.core.utils.npc.NPCType;
import com.hangout.rpg.utils.PlayerOccupations;
import com.hangout.rpg.utils.PlayerRaces;

public class EntityInteractListener implements Listener {
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractEntityEvent e){
		Player p = e.getPlayer();
        HangoutPlayer hp = HangoutPlayerManager.getPlayer(p.getUniqueId());
        Entity clicked = e.getRightClicked();
        
        if(!NPCManager.isNPC(clicked)) return;
        
        NPCData npcData = NPCManager.getNPCData(clicked);
        if(npcData.getType() == NPCType.OCCUPATION_CHANGER){
	        int key = hp.generateNewCommandKey("switch_occupation");
	        
	        p.sendMessage("Which of these occupations would you like to switch to?");
	        
	        int count = 1;
	        for(PlayerOccupations o : PlayerOccupations.values()){
	        	List<String> description = new ArrayList<String>();
	        	description.add(""+ChatColor.RED + ChatColor.BOLD + o.getDisplayName());
	        	description.addAll(o.getDescription());
	        	description.add(" ");
	        	description.add("Click to switch!");
	        	new FancyMessage(count + ": ")
	        			.style(ChatColor.BOLD)
	        		.then(o.getDisplayName())
	        			.tooltip(description)
	        			.color(ChatColor.RED)
	        			.style(ChatColor.ITALIC)
	        			.command("/rpgtext occupation " + o.toString() + " " + key)
	        		.send(p);
	        	count++;
	        }
        }else if(npcData.getType() == NPCType.GUILD_EDITOR){
        	
        	
        	
        }else if(npcData.getType() == NPCType.RACE_CHANGER){
	        int key = hp.generateNewCommandKey("switch_race");
	        
	        p.sendMessage("Which of these races would you like to switch to?");
	        
	        int count = 1;
	        for(PlayerRaces r : PlayerRaces.values()){
	        	List<String> description = new ArrayList<String>();
	        	description.add(""+ChatColor.RED + ChatColor.BOLD + r.getDisplayName());
	        	description.addAll(r.getDescription());
	        	description.add(" ");
	        	description.add("Click to switch!");
	        	new FancyMessage(count + ": ")
	        			.style(ChatColor.BOLD)
	        		.then(r.getDisplayName())
	        			.tooltip(description)
	        			.color(ChatColor.RED)
	        			.style(ChatColor.ITALIC)
	        			.command("/rpgtext race " + r.toString() + " " + key)
	        		.send(p);
	        	count++;
	        }
        }
	}
}
