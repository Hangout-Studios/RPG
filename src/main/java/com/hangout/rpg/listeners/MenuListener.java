package com.hangout.rpg.listeners;

import java.util.Arrays;
import java.util.UUID;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.MenuCreateEvent;
import com.hangout.core.events.MenuItemClickEvent;
import com.hangout.core.menu.MenuInventory;
import com.hangout.core.menu.MenuUtils;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.core.player.PlayerRank;
import com.hangout.core.utils.mc.CommandPreparer;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.guild.GuildBonusType;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.guild.GuildRank;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.RpgMenuUtils;

public class MenuListener implements Listener {
	
	@EventHandler
	public void onMenuCreate(MenuCreateEvent e){
		MenuInventory menu = e.getMenu();
		if(e.getMenu().getTag().equals("main_menu")){
			MenuUtils.createMenuItem(menu, Material.ANVIL, "Profile", Arrays.asList("Check your own profile!"), 1 + 9, "profile_item");
		}
	}
	
	@EventHandler
	public void onMenuClick(MenuItemClickEvent e){
		
		String itemTag = e.getItem().getTag();
		
		if(itemTag.equals("friend_list")){
			RpgMenuUtils.createFriendListMenu(e.getPlayer()).openMenu(e.getPlayer(), true);
			return;
		}
		
		if(itemTag.equals("profile_item")){
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), e.getPlayer()).openMenu(e.getPlayer(), true);
			return;
		}
		
		if(itemTag.equals("remove_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().removeFriend(HangoutPlayerManager.getPlayer(friendID), true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("add_friend")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addFriend(HangoutPlayerManager.getPlayer(friendID), true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("mute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);			
			e.getPlayer().addMutedPlayer(friendID, true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("unmute_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			e.getPlayer().removeMutedPlayer(friendID, true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("report_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			e.getPlayer().getPlayer().closeInventory();
			
			CommandPreparer c = e.getPlayer().createCommandPreparer("report_player", true);
			c.append("report player " + HangoutPlayerManager.getPlayer(friendID).getName());
			
			e.getPlayer().getPlayer().sendMessage(ChatColor.ITALIC + "Please write your explaination of the report in chat.");
			return;
		}
		
		if(itemTag.equals("guild_remove_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			rpgPlayer.getGuild().removePlayer(rpgPlayer, rpgFriend, true);
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("guild_promote_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			rpgPlayer.getGuild().setRank(rpgPlayer, rpgFriend, GuildRank.getNextRank(rpgFriend.getGuild().getPlayerRank(rpgFriend)), true);
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("guild_demote_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			rpgPlayer.getGuild().setRank(rpgPlayer, rpgFriend, GuildRank.getPreviousRank(rpgFriend.getGuild().getPlayerRank(rpgFriend)), false);
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("guild_invite_player")){
			UUID friendID = UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]);
			RpgPlayer rpgFriend = RpgPlayerManager.getPlayer(friendID);
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			
			CommandPreparer commandAccept = rpgFriend.getHangoutPlayer().createCommandPreparer("guild_accept", false);
			commandAccept.append("guild");
			commandAccept.append("accept");
			commandAccept.append(""+rpgPlayer.getGuild().getID());
			commandAccept.append(rpgPlayer.getHangoutPlayer().getUUID().toString());
			
			CommandPreparer command = rpgFriend.getHangoutPlayer().createCommandPreparer("guild_decline", false);
			command.append("guild");
			command.append("decline");
			command.append(""+rpgPlayer.getGuild().getID());
			command.append(rpgPlayer.getHangoutPlayer().getUUID().toString());
			
			//Invite text
			rpgPlayer.getHangoutPlayer().getClickableName(rpgFriend.getHangoutPlayer(), false, false)
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
			
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), HangoutPlayerManager.getPlayer(friendID)).openMenu(e.getPlayer(), false);
			return;
		}
		
		if(itemTag.equals("guild_boosters")){
			RpgPlayer rpgPlayer = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			Guild g = rpgPlayer.getGuild();
			RpgMenuUtils.createGuildBoostersMenu(e.getPlayer(), g).openMenu(e.getPlayer(), true);
			return;
		}
		
		//guild_booster_activate_name
		if(itemTag.startsWith("guild_booster_")){
			String[] split = itemTag.split("_");
			String action = split[2];
			GuildBonusType type = GuildBonusType.valueOf(split[3]);
			RpgPlayer p = RpgPlayerManager.getPlayer(e.getPlayer().getUUID());
			Guild g = p.getGuild();
			
			if(action.equals("buy")){
				if(g.buyBonus(type, p)){
					RpgMenuUtils.createGuildBoostersMenu(e.getPlayer(), g).openMenu(e.getPlayer(), false);
					return;
				}
			}else if(action.equals("activate")){
				if(g.activateBonus(type, p)){
					RpgMenuUtils.createGuildBoostersMenu(e.getPlayer(), g).openMenu(e.getPlayer(), false);
					return;
				}
			}
		}
		
		if(itemTag.startsWith("guild_menu_open_")){
			Guild g = GuildManager.getGuild(itemTag.split("_")[3]);
			RpgMenuUtils.createGuildMenu(e.getPlayer(), g).openMenu(e.getPlayer(), true);
			return;
		}
		
		//guild_members_tag_page_0
		if(itemTag.startsWith("guild_members_")){
			String[] split = itemTag.split("_");
			Guild g = GuildManager.getGuild(split[2]);
			int page = Integer.parseInt(split[4]);
			RpgMenuUtils.createGuildMembersMenu(e.getPlayer(), g, page).openMenu(e.getPlayer(), true);
			return;
		}
		
		if(itemTag.startsWith("profile_")){
			HangoutPlayer p = HangoutPlayerManager.getPlayer(UUID.fromString(itemTag.split("_")[1]));
			RpgMenuUtils.createPlayerMenu(e.getPlayer(), p).openMenu(e.getPlayer(), true);
			return;
		}
		
		if(itemTag.equals("icon_friend") && e.getPlayer().getHighestRank().isRankOrHigher(PlayerRank.ADMIN)){
			HangoutPlayer p = HangoutPlayerManager.getPlayer(UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[0]));
			RpgMenuUtils.createPlayerRankMenu(e.getPlayer(), p).openMenu(e.getPlayer(), true);
			return;
		}
		
		if(itemTag.startsWith("rank_")){
			PlayerRank r = PlayerRank.valueOf(itemTag.split("_")[1]);
			HangoutPlayer p = HangoutPlayerManager.getPlayer(UUID.fromString(e.getPlayer().getOpenMenu().getTag().split("_")[1]));
			p.addRank(r, true);
			
			p.getPlayer().sendMessage(""+ ChatColor.GREEN + ChatColor.BOLD + "Your rank has been set to " + r.getDisplayName());
			e.getPlayer().getPlayer().sendMessage(""+ ChatColor.GREEN + ChatColor.BOLD + "Your have set " + p.getName() + "'s rank to " + r.getDisplayName());
			
			RpgMenuUtils.createPlayerRankMenu(e.getPlayer(), p).openMenu(e.getPlayer(), true);
		}
	}
}
