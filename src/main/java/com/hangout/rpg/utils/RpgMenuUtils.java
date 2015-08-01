package com.hangout.rpg.utils;

import java.util.Arrays;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class RpgMenuUtils {
	
	public static MenuInventory createFriendListMenu(HangoutPlayer p){
		MenuInventory friendMenu = MenuUtils.createMenu("Friend list", "friend_menu", p);
		friendMenu.setTemporary(true);
		
		int count = 0;
		for(HangoutPlayer friend : p.getFriends()){
			MenuUtils.createMenuItem(friendMenu, ItemUtils.getPlayerHead(friend.getPlayer(), friend.getDisplayName(), friend.getDescription()), count, "friend_" + friend.getUUID());
			count++;
		}
		
		return friendMenu;
	}
	
	public static MenuInventory createPlayerMenu(HangoutPlayer player, HangoutPlayer friend){
		MenuInventory friendMenu = MenuUtils.createMenu(friend.getName(), friend.getUUID() + "_menu", player);
		friendMenu.setTemporary(true);
		
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friend.getUUID());
		
		int locationProfile = 1;
		int locationFriend = 3;
		int locationMute = 5;
		int locationGuild = 7;
		
		if(rpgPlayer != rpgFriend){
			if(player.isFriend(friend.getUUID())){
				MenuUtils.createMenuItem(friendMenu, Material.LAVA_BUCKET, "Remove friend", Arrays.asList("Click to remove!"), locationFriend, "remove_friend");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.PAPER, "Add as friend", Arrays.asList("Click to add!"), locationFriend, "add_friend");
			}
			
			if(player.hasMutedPlayer(friend.getUUID())){
				MenuUtils.createMenuItem(friendMenu, Material.BOOK_AND_QUILL, "Unmute player", Arrays.asList("Click to unmute!"), locationMute, "unmute_player");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.BOOK, "Mute player", Arrays.asList("Click to mute!"), locationMute, "mute_player");
			}
			
			if(rpgPlayer.isInGuild()){
				if(rpgFriend.isInGuild()){
					if(rpgFriend.getGuild() == rpgPlayer.getGuild()){
						MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Kick from guild", Arrays.asList("Click to kick!"), locationGuild, "guild_remove_player");
					}else{
						MenuUtils.createMenuItem(friendMenu, Material.BANNER, rpgFriend.getGuild().getName(), Arrays.asList("Click to check this guild!"), locationGuild, "guild_menu_open_" + rpgFriend.getGuild().getTag());
					}
				}else{
					MenuUtils.createMenuItem(friendMenu, Material.BANNER, "Invite to guild", Arrays.asList("Click to invite!"), locationGuild, "guild_invite_player");
				}
			}else{
				if(rpgFriend.isInGuild()){
					MenuUtils.createMenuItem(friendMenu, Material.BANNER, rpgFriend.getGuild().getName(), Arrays.asList("Click to check this guild!"), locationGuild, "guild_menu_open_" + rpgFriend.getGuild().getTag());
				}else{
					MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "No guild", Arrays.asList("This player is not in", "a guild. Perhaps you", "can start one together?"), locationGuild, "guild_no_guild");
				}
			}
		}else{
			
			locationProfile = locationFriend;
			locationGuild = locationMute;
			
			if(rpgPlayer.isInGuild()){
				MenuUtils.createMenuItem(friendMenu, Material.BANNER, rpgPlayer.getGuild().getName(), Arrays.asList("Click to check your guild!"), locationGuild, "guild_menu_open_" + rpgPlayer.getGuild().getTag());
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "No guild", Arrays.asList("You're not in a guild.", "Ask around to join one!"), locationGuild, "guild_no_guild");
			}
		}
		
		MenuUtils.createMenuItem(friendMenu, Material.SKULL_ITEM, friend.getDisplayName() + ChatColor.WHITE + "'s profile", friend.getDescription(), locationProfile, "profile_friend");
		
		return friendMenu;
	}
	
	public static MenuInventory createGuildMenu(HangoutPlayer player, Guild g){
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		MenuInventory guildMenu = null;
		
		if(g == null){
			guildMenu = MenuUtils.createMenu("No guild", "guild_menu", player);
			guildMenu.setTemporary(true);
			return guildMenu;
		}
		
		guildMenu = MenuUtils.createMenu(g.getName(), g.getTag() + "_menu", player);
		guildMenu.setTemporary(true);
		
		int locationGuildInfo = 3;
		int locationGuildMembers = 5;
		int locationGuildSettings = 6;
		
		if(g.getMembers().contains(rpgPlayer)){
			locationGuildInfo = 2;
			locationGuildMembers = 4;
			MenuUtils.createMenuItem(guildMenu, Material.PAPER, "Guild settings", g.getDescription(), locationGuildSettings, "guild_settings_"+g.getTag());
		}
		
		//Add guild items
		MenuUtils.createMenuItem(guildMenu, Material.BANNER, "Guild information", g.getDescription(), locationGuildInfo, "guild_information");
		MenuUtils.createMenuItem(guildMenu, Material.SKULL_ITEM, "Guild members", Arrays.asList("Check player members!"), locationGuildMembers, "guild_members_"+g.getTag()+"_page_0");
		
		return guildMenu;
	}
	
	public static MenuInventory createGuildMembersMenu(HangoutPlayer player, Guild g, int page){
		MenuInventory membersMenu = MenuUtils.createMenu(g.getName() + " members", g.getTag() + "_members_menu", player);
		membersMenu.setTemporary(true);
		
		int count = 0;
		for(RpgPlayer member : g.getMembers()){
			ItemStack playerHead = ItemUtils.getPlayerHead(member.getHangoutPlayer().getPlayer(), member.getHangoutPlayer().getDisplayName(), member.getHangoutPlayer().getDescription());
			MenuUtils.createMenuItem(membersMenu, playerHead, count, "guild_member_" + member.getHangoutPlayer().getUUID());
			count++;
		}		
		return membersMenu;
	}
}
