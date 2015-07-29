package com.hangout.rpg.listeners;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.HangoutAPI;
import com.hangout.core.events.MenuClickEvent;
import com.hangout.rpg.utils.MenuUtils;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onMenuClick(MenuClickEvent e){
		
		String itemTag = e.getItem().getTag();
		
		if(itemTag.equals("friend_item")){
			MenuUtils.createFriendListMenu(e.getPlayer()).openMenu(e.getPlayer());
		}
		
		if(itemTag.equals("remove_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().removeFriend(HangoutAPI.getPlayer(friendID), true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
		}
		
		if(itemTag.equals("add_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addFriend(HangoutAPI.getPlayer(friendID), true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
		}
		
		if(itemTag.equals("mute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addMutedPlayer(friendID, true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
		}
		
		if(itemTag.equals("unmute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().removeMutedPlayer(friendID, true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
		}
	}
}
