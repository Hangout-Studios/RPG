package com.hangout.rpg.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.utils.mc.ItemUtils;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.guild.GuildBonusType;
import com.hangout.rpg.guild.GuildRank;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

public class RpgMenuUtils {
	
	public static MenuInventory createFriendListMenu(HangoutPlayer p){
		MenuInventory friendMenu = MenuUtils.createMenu("Friend list", "friend_menu", p);
		friendMenu.setTemporary(true);
		
		int count = 0;
		for(HangoutPlayer friend : p.getFriends()){
			MenuUtils.createMenuItem(friendMenu, ItemUtils.getPlayerHead(friend.getUUID(), friend.getDisplayName(), friend.getDescription()), count, "profile_" + friend.getUUID());
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
		int locationOccupations = 9 + 3;
		
		if(rpgPlayer != rpgFriend){
			if(player.isFriend(friend)){
				MenuUtils.createMenuItem(friendMenu, Material.LAVA_BUCKET, "Remove friend", Arrays.asList("Click to remove!"), locationFriend, "remove_friend");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.PAPER, "Add as friend", Arrays.asList("Click to add!"), locationFriend, "add_friend");
			}
			
			if(player.hasMutedPlayer(friend.getUUID())){
				MenuUtils.createMenuItem(friendMenu, Material.BOOK_AND_QUILL, "Unmute player", Arrays.asList("Click to unmute!"), locationMute, "unmute_player");
			}else{
				MenuUtils.createMenuItem(friendMenu, Material.BOOK, "Mute player", Arrays.asList("Click to mute!"), locationMute, "mute_player");
			}
			
			if(rpgFriend.isInGuild()){
				MenuUtils.createMenuItem(friendMenu, Material.BANNER, rpgFriend.getGuild().getName(), Arrays.asList("Click to check this guild!"), locationGuild, "guild_menu_open_" + rpgFriend.getGuild().getTag());
			}
			
			if(rpgPlayer.isInGuild()){
				GuildRank pRank = rpgPlayer.getGuild().getPlayerRank(rpgPlayer);
				if(rpgFriend.isInGuild()){
					GuildRank fRank = rpgFriend.getGuild().getPlayerRank(rpgFriend);
					if(rpgFriend.getGuild() == rpgPlayer.getGuild()){
						if(pRank.canKick(fRank)){
							MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Kick from guild", Arrays.asList("Click to remove this", "player from the guild."), locationGuild + 9 + 1, "guild_remove_player");
						}
						
						if(pRank.canPromote(fRank)){
							MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Promote member", Arrays.asList("Click to promote this", "member to " + rpgFriend.getGuild().getRankName(GuildRank.getNextRank(fRank)) + "."), locationGuild + 9, "guild_promote_player");
						}
						
						if(pRank.canDemote(fRank)){
							MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Demote member", Arrays.asList("Click to demote this", "member to " + rpgFriend.getGuild().getRankName(GuildRank.getPreviousRank(fRank)) + "."), locationGuild + 18, "guild_demote_player");
						}
					}
				}else{
					if(pRank.canInvite()){
						MenuUtils.createMenuItem(friendMenu, Material.BANNER, "Invite to guild", Arrays.asList("Click to invite!"), locationGuild, "guild_invite_player");
					}else{
						MenuUtils.createMenuItem(friendMenu, Material.BARRIER, "Cannot invite to guild", Arrays.asList("Your rank isn't high", "enough to invite others", "into the guild!"), locationGuild, "guild_cannot_invite_player");
					}
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
		
		MenuUtils.createMenuItem(friendMenu, Material.SKULL_ITEM, friend.getDisplayName(), friend.getDescription(), locationProfile, "icon_friend");
		
		MenuUtils.createMenuItem(friendMenu, Material.IRON_HELMET, ChatColor.RED + "Occupation levels", rpgFriend.getOccupationDescriptions(), locationOccupations, "occupations_friend");
		
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
		int locationGuildBoosters = 13;
		
		if(g.getMembers().contains(rpgPlayer)){			
			if(g.getPlayerRank(rpgPlayer).getRank() >= 3){
				locationGuildInfo = 2;
				locationGuildMembers = 4;
				MenuUtils.createMenuItem(guildMenu, Material.PAPER, "Guild settings", g.getDescription(), locationGuildSettings, "guild_settings_"+g.getTag());
			}
			
			MenuUtils.createMenuItem(guildMenu, Material.ANVIL, "Guild boosters", Arrays.asList("Check out guild boosters", "available for purchase!"), locationGuildBoosters, "guild_boosters");
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
			ItemStack playerHead = ItemUtils.getPlayerHead(member.getHangoutPlayer().getUUID(), member.getHangoutPlayer().getDisplayName(), member.getHangoutPlayer().getDescription());
			MenuUtils.createMenuItem(membersMenu, playerHead, count, "profile_" + member.getHangoutPlayer().getUUID());
			count++;
		}		
		return membersMenu;
	}
	
	public static MenuInventory createGuildBoostersMenu(HangoutPlayer player, Guild g){
		MenuInventory boosterMenu = MenuUtils.createMenu(g.getName() + " boosters", g.getTag() + "_boosters_menu", player);
		boosterMenu.setTemporary(true);
		
		RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(player.getUUID());
		
		int count = 0;
		for(GuildBonusType type : GuildBonusType.values()){
			if(!type.isPermanent()){
				List<String> description= new ArrayList<String>();
				description.add("In guild stock: " + g.getBoosterAmount(type));
				description.add(" ");
				description.addAll(type.getDescription());
				MenuUtils.createMenuItem(boosterMenu, type.getMaterial(), type.getDisplayName(), description, count, "guild_booster_" + type.toString());
				MenuUtils.createMenuItem(boosterMenu, Material.GOLD_INGOT, "Buy booster - " + type.getDisplayName(), Arrays.asList("Price: " + type.getPrice() + "G", " ", "The guild leader or an officer", "can activate the booster."), count + 9, "guild_booster_buy_" + type.toString());
				
				if(g.getPlayerRank(rpgPlayer).getRank() >= 3){
					MenuUtils.createMenuItem(boosterMenu, Material.NETHER_STAR, "Activate booster - " + type.getDisplayName(), Arrays.asList("Activate this booster!"), count + 18, "guild_booster_activate_" + type.toString());
				}
				count++;
			}
		}
		return boosterMenu;
	}
}
