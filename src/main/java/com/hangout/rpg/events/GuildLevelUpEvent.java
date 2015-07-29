package com.hangout.rpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.rpg.guild.Guild;

public class GuildLevelUpEvent extends Event  {
	
	private Guild g;
	private int level;
	public GuildLevelUpEvent(Guild g, int level){
		this.g = g;
		this.level = level;
	}
	
	public Guild getGuild(){
		return g;
	}
	
	public int getLevel(){
		return level;
	}
	
	/*
	 * Handlers
	 */
	private static final HandlerList handlers = new HandlerList();
	 
	public HandlerList getHandlers() {
	    return handlers;
	}
	 
	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
