package com.hangout.rpg.utils;

import java.util.Arrays;

import org.bukkit.Material;

import com.hangout.core.HangoutAPI;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class MenuUtils {
	
	public static MenuInventory createFriendListMenu(HangoutPlayer p){
		MenuInventory friendMenu = HangoutAPI.createMenu(
				ItemUtils.createItem(Material.DIRT, "frienditem", Arrays.asList("lel")), "Friend list", 3, "friend_menu");
		friendMenu.setTemporary(true);
		
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
		
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friend.getUUID());
		
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
		
		if(rpgPlayer != rpgFriend){
			if(rpgPlayer.isInGuild()){
				if(rpgFriend.isInGuild()){
					if(rpgFriend.getGuild() == rpgPlayer.getGuild()){
						HangoutAPI.createMenuItem(friendMenu, Material.BARRIER, "Kick from guild", Arrays.asList("Click to kick!"), 6, "guild_remove_player");
					}else{
						HangoutAPI.createMenuItem(friendMenu, Material.BARRIER, "Player is in another guild", Arrays.asList("Cannot invite players", "who are in a different", "guild."), 6, "guild_another_player");
					}
				}else{
					HangoutAPI.createMenuItem(friendMenu, Material.BANNER, "Invite to guild", Arrays.asList("Click to invite!"), 6, "guild_invite_player");
				}
			}
		}else{
			if(rpgPlayer.isInGuild()){
				HangoutAPI.createMenuItem(friendMenu, Material.BARRIER, "No guild", Arrays.asList("You're not in a guild.", "Ask around to join one!"), 6, "guild_no_guild");
			}else{
				HangoutAPI.createMenuItem(friendMenu, Material.BANNER, "Check your guild", Arrays.asList("Click to check!"), 6, "guild_menu_open");
			}
		}
		
		return friendMenu;
	}
	
	public static MenuInventory createGuildMenu(HangoutPlayer player){
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		Guild g = rpgPlayer.getGuild();
		MenuInventory guildMenu = null;
		
		if(g == null){
			guildMenu = HangoutAPI.createMenu(
					ItemUtils.createItem(Material.DIRT, "guilditem", Arrays.asList("lel")), "No guild", 1, "guild_menu");
			guildMenu.setTemporary(true);
			return guildMenu;
		}
		
		guildMenu = HangoutAPI.createMenu(
				ItemUtils.createItem(Material.DIRT, "guilditem", Arrays.asList("lel")), g.getName(), 1, g.getTag() + "_menu");
		guildMenu.setTemporary(true);
		
		//Add guild items
		
		return guildMenu;
	}
}
