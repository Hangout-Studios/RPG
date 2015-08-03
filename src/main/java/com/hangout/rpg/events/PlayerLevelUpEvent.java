package com.hangout.rpg.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.utils.PlayerOccupations;

public class PlayerLevelUpEvent extends Event  {
	
	private RpgPlayer p;
	private PlayerOccupations occupation;
	private int level;
	public PlayerLevelUpEvent(RpgPlayer p, PlayerOccupations occupation, int level){
		this.p = p;
		this.occupation = occupation;
		this.level = level;
	}
	
	public RpgPlayer getPlayer(){
		return p;
	}
	
	public PlayerOccupations getOccupation(){
		return occupation;
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
