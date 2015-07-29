package com.hangout.rpg.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.HangoutAPI;
import com.hangout.core.events.MenuClickEvent;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.MenuUtils;

public class MenuListener implements Listener {
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMenuClick(MenuClickEvent e){
		
		String itemTag = e.getItem().getTag();
		
		if(itemTag.equals("friend_item")){
			MenuUtils.createFriendListMenu(e.getPlayer()).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("remove_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().removeFriend(HangoutAPI.getPlayer(friendID), true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("add_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addFriend(HangoutAPI.getPlayer(friendID), true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("mute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addMutedPlayer(friendID, true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("unmute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			e.getPlayer().removeMutedPlayer(friendID, true);
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("guild_remove_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			rpgPlayer.getGuild().removePlayer(rpgPlayer, rpgFriend, true);
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
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
				.command("text execute prepared guild_accept")
			.then(" or ")
			.then("decline")
				.color(ChatColor.GREEN)
				.style(ChatColor.BOLD)
				.command("text execute prepared guild_decline")
			.send(rpgFriend.getHangoutPlayer().getPlayer());
			
			MenuUtils.createPlayerMenu(e.getPlayer(), HangoutAPI.getPlayer(friendID)).openMenu(e.getPlayer());
			return;
		}
		
		if(itemTag.equals("guild_menu_open")){
			MenuUtils.createGuildMenu(e.getPlayer()).openMenu(e.getPlayer());
		}
	}
}
