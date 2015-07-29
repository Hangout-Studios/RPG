package com.hangout.rpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.rpg.player.RpgPlayer;

public class PlayerLevelUpEvent extends Event  {
	
	private RpgPlayer p;
	private int level;
	public PlayerLevelUpEvent(RpgPlayer p, int level){
		this.p = p;
		this.level = level;
	}
	
	public RpgPlayer getPlayer(){
		return p;
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
