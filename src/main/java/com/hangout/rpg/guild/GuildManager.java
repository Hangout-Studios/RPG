package com.hangout.rpg.guild;

import org.bukkit.Bukkit;

import com.hangout.rpg.Plugin;

public class GuildManager {
	
	public static void loadGuilds(){
		
		Bukkit.getScheduler().runTaskAsynchronously(Plugin.getInstance(), new Runnable(){
			@Override
			public void run() {
				
				//Load all active players in the guild
				
				
			}
		});		
	}
}
