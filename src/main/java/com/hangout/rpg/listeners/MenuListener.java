package com.hangout.rpg.listeners;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.events.MenuCreateEvent;
import com.hangout.core.events.MenuItemClickEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.RpgMenuUtils;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onMenuCreate(MenuCreateEvent e){
		MenuInventory menu = e.getMenu();
		if(e.getMenu().getTag().equals("main_menu")){
			MenuUtils.createMenuItem(menu, Material.ANVIL, "Profile & Settings", Arrays.asList("Check your own profile", "and change all kinds of", "settings!"), 1 + 9, "profile_item");
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMenuClick(MenuItemClickEvent e){
		
		String itemTag = e.getItem().getTag();
		
		if(itemTag.equals("friend_item")){
			RpgMenuUtils.createFriendListMenu(e.getPlayer()).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("profile_item")){
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), e.getPlayer()).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("remove_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().removeFriend(HangoutPlayerManager.getPlayer(friendID), true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("add_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addFriend(HangoutPlayerManager.getPlayer(friendID), true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("mute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addMutedPlayer(friendID, true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("unmute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			e.getPlayer().removeMutedPlayer(friendID, true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("guild_remove_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			rpgPlayer.getGuild().removePlayer(rpgPlayer, rpgFriend, true);
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("guild_invite_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			CommandPreparer commandAccept = rpgFriend.getHangoutPlayer().createCommandPreparer("guild_accept");
			commandAccept.append("guild");
			commandAccept.append("accept");
			commandAccept.append(""+rpgPlayer.getGuild().getID());
			commandAccept.append(rpgPlayer.getHangoutPlayer().getUUID().toString());
			
			CommandPreparer command = rpgFriend.getHangoutPlayer().createCommandPreparer("guild_decline");
			command.append("guild");
			command.append("decline");
			command.append(""+rpgPlayer.getGuild().getID());
			command.append(rpgPlayer.getHangoutPlayer().getUUID().toString());
			
			HashMap<String, Object> nameConfig =  rpgPlayer.getHangoutPlayer().getClickableNameConfig(rpgFriend.getHangoutPlayer());
			
			//Invite text
			new FancyMessage((String)nameConfig.get("text"))
					.color((ChatColor)nameConfig.get("color"))
					.style((ChatColor[])nameConfig.get("styles"))
					.command((String)nameConfig.get("command"))
					.tooltip((List<String>)nameConfig.get("tooltip"))
				.then(" has invited you the guild " + rpgPlayer.getGuild().getName() + ".")
				.send(rpgFriend.getHangoutPlayer().getPlayer());
			
			//Accept or decline
			new FancyMessage("Accept")
				.color(ChatColor.GREEN)
				.style(ChatColor.BOLD)
				.command("/text execute prepared guild_accept")
			.then(" or ")
			.then("decline")
				.color(ChatColor.GREEN)
				.style(ChatColor.BOLD)
				.command("/text execute prepared guild_decline")
			.send(rpgFriend.getHangoutPlayer().getPlayer());
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.startsWith("guild_menu_open_")){
			Guild g = GuildManager.getGuild(itemTag.split("_")[3]);
			RpgMenuUtils.createGuildMenu(e.getPlayer(), g).openMenu(e.getPlayer());
			return;
		}
		
		//guild_members_tag_page_0
		if(itemTag.startsWith("guild_members_")){
			String[] split = itemTag.split("_");
			Guild g = GuildManager.getGuild(split[2]);
			int page = Integer.parseInt(split[4]);
			RpgMenuUtils.createGuildMembersMenu(e.getPlayer(), g, page).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.startsWith("guild_member_")){
			HangoutPlayer p = HangoutPlayerManager.getPlayer(UUID.fromString(itemTag.split("_")[2]));
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), p).openMenu(e.getPlayer());
			return;
		}
	}
}
