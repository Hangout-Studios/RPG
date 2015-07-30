package com.hangout.rpg.utils;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;

import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class RpgMenuUtils {
	
	public static MenuInventory createFriendListMenu(HangoutPlayer p){
		MenuInventory friendMenu = MenuUtils.createMenu("Friend list", 3, "friend_menu", p);
		friendMenu.setTemporary(true);
		
		int count = 0;
		for(HangoutPlayer friend : p.getFriends()){
			MenuUtils.createMenuItem(friendMenu, Material.SKULL, friend.getDisplayName(), friend.getDescription(), count, "friend_" + friend.getUUID());
			count++;
		}
		
		return friendMenu;
	}
	
	public static MenuInventory createPlayerMenu(HangoutPlayer player, HangoutPlayer friend){
		MenuInventory friendMenu = MenuUtils.createMenu(friend.getName(), 1, friend.getUUID() + "_menu", player);
		friendMenu.setTemporary(true);
		
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friend.getUUID());
		
		int locationProfile = 1;
		int locationFriend = 3;
		int locationMute = 5;
		int locationGuild = 7;
		
		MenuUtils.createMenuItem(friendMenu, Material.SKULL_ITEM, friend.getDisplayName() + ChatColor.WHITE + "'s profile", friend.getDescription(), locationProfile, "profile_friend");
		
		if(rpgPlayer != rpgFriend){
			if(player.isFriend(friend.getUUID())){
				MenuUtils.createMenuItem(friendMenu, Material.LAVA_BUCKET, "Remove friend", Arrays.asList("Click to remove!"), locationFriend, "remove_friend");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.PAPER, "Add as friend", Arrays.asList("Click to add!"), locationFriend, "add_friend");
			}
		}else{
			MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "This is yourself!", Arrays.asList("You cannot add yourself,", "silly you!"), locationFriend, "same_friend");
		}
		
		if(rpgPlayer != rpgFriend){
			if(player.hasMutedPlayer(friend.getUUID())){
				MenuUtils.createMenuItem(friendMenu, Material.BOOK_AND_QUILL, "Unmute player", Arrays.asList("Click to unmute!"), locationMute, "unmute_player");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.BOOK, "Mute player", Arrays.asList("Click to mute!"), locationMute, "mute_player");
			}
		}else{
			MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "This is yourself!", Arrays.asList("You cannot mute yourself,", "silly you!"), locationMute, "same_friend");
		}
		
		if(rpgPlayer != rpgFriend){
			if(rpgPlayer.isInGuild()){
				if(rpgFriend.isInGuild()){
					if(rpgFriend.getGuild() == rpgPlayer.getGuild()){
						MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Kick from guild", Arrays.asList("Click to kick!"), locationGuild, "guild_remove_player");
					}else{
						MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Player is in another guild", Arrays.asList("Cannot invite players", "who are in a different", "guild."), locationGuild, "guild_another_player");
					}
				}else{
					MenuUtils.createMenuItem(friendMenu, Material.BANNER, "Invite to guild", Arrays.asList("Click to invite!"), locationGuild, "guild_invite_player");
				}
			}else{
				if(rpgFriend.isInGuild()){
					MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Player is in another guild", Arrays.asList("Cannot invite players", "who are in a different", "guild."), locationGuild, "guild_another_player");
				}else{
					MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "No guild", Arrays.asList("This player is not in", "a guild. Perhaps you", "can start one together?"), locationGuild, "guild_no_guild");
				}
			}
		}else{
			if(rpgPlayer.isInGuild()){
				MenuUtils.createMenuItem(friendMenu, Material.BANNER, "Check your guild", Arrays.asList("Click to check!"), locationGuild, "guild_menu_open");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "No guild", Arrays.asList("You're not in a guild.", "Ask around to join one!"), locationGuild, "guild_no_guild");
			}
		}
		
		return friendMenu;
	}
	
	public static MenuInventory createGuildMenu(HangoutPlayer player){
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		Guild g = rpgPlayer.getGuild();
		MenuInventory guildMenu = null;
		
		if(g == null){
			guildMenu = MenuUtils.createMenu("No guild", 1, "guild_menu", player);
			guildMenu.setTemporary(true);
			return guildMenu;
		}
		
		guildMenu = MenuUtils.createMenu(g.getName(), 1, g.getTag() + "_menu", player);
		guildMenu.setTemporary(true);
		
		//Add guild items
		MenuUtils.createMenuItem(guildMenu, Material.BANNER, "Guild information", g.getDescription(), 3, "guild_information");
		MenuUtils.createMenuItem(guildMenu, Material.SKULL_ITEM, "Guild members", Arrays.asList("Check player members!"), 5, "guild_members_page_0");
		
		return guildMenu;
	}
	
	public static MenuInventory createGuildMembersMenu(HangoutPlayer player, Guild g){
		MenuInventory membersMenu = MenuUtils.createMenu(g.getName() + " members", 3, g.getTag() + "_members_menu", player);
		membersMenu.setTemporary(true);
		
		int count = 0;
		for(RpgPlayer member : g.getMembers()){
			MenuUtils.createMenuItem(membersMenu, ItemUtils.getPlayerHead(member.getHangoutPlayer().getPlayer(), member.getHangoutPlayer().getName(), member.getHangoutPlayer().getDescription()), count, "guild_member_" + member.getHangoutPlayer().getUUID());
			count++;
		}
		
		//RpgMenuUtils.createPlayerMenu(player, friend)
		
		return membersMenu;
	}
}
