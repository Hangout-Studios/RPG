package com.hangout.rpg.utils;

import java.util.Arrays;

import org.bukkit.Material;

import com.hangout.core.HangoutAPI;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.ItemUtils;

public class MenuUtils {
	
	public static MenuInventory createFriendListMenu(HangoutPlayer p){
		MenuInventory friendMenu = HangoutAPI.createMenu(
				ItemUtils.createItem(Material.DIRT, "frienditem", Arrays.asList("lel")), "Friend list", 3, "friend_menu");
		friendMenu.setTemporary(true);
		
		//Add fwiends!
		//HangoutAPI.createMenuItem(friendMenu, Material.SKULL, "Friend 1", Arrays.asList("Friend description"), 1, "friend_1");
		
		int count = 0;
		for(HangoutPlayer friend : p.getFriends()){
			HangoutAPI.createMenuItem(friendMenu, Material.SKULL, friend.getDisplayName(), friend.getDescription(), count, "friend_" + friend.getUUID());
			count++;
		}
		
		return friendMenu;
	}
	
	public static MenuInventory createPlayerMenu(HangoutPlayer player, HangoutPlayer friend){
		MenuInventory friendMenu = HangoutAPI.createMenu(
				ItemUtils.createItem(Material.DIRT, "frienditem", Arrays.asList("lel")), friend.getName(), 1, friend.getUUID() + "_menu");
		friendMenu.setTemporary(true);
		
		if(player.isFriend(friend.getUUID())){
			HangoutAPI.createMenuItem(friendMenu, Material.PAPER, "Remove friend", Arrays.asList("Click to remove!"), 2, "remove_friend");
		}else{
			HangoutAPI.createMenuItem(friendMenu, Material.PAPER, "Add as friend", Arrays.asList("Click to add!"), 2, "add_friend");
		}
		
		if(player.hasMutedPlayer(friend.getUUID())){
			HangoutAPI.createMenuItem(friendMenu, Material.BOOK_AND_QUILL, "Unmute player", Arrays.asList("Click to unmute!"), 4, "unmute_player");
		}else{
			HangoutAPI.createMenuItem(friendMenu, Material.BOOK, "Mute player", Arrays.asList("Click to mute!"), 4, "mute_player");
		}
		
		return friendMenu;
	}

}
