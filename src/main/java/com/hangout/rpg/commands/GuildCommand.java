package com.hangout.rpg.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.rpg.guild.Guild;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.RpgMenuUtils;

public class GuildCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		if(!(sender instanceof Player)) return false;
		HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUUID());
		
		//guild create <tag> <name>
		if(args[0].equalsIgnoreCase("create")){
			String tag = args[1];
			String name = args[2];
			
			GuildManager.createGuild(name, tag, rpgP);
			return true;
		}
		
		//guild accept <id> <inviter uuid>
		if(args[1].equals("accept")){
			Guild g = GuildManager.getGuild(Integer.parseInt(args[2]));
			Player inviter = Bukkit.getPlayer(UUID.fromString(args[3]));
			RpgPlayer inviterRPG = RpgPlayerManager.getPlayer(inviter.getUniqueId());
			
			g.addPlayer(inviterRPG, rpgP, true);
			
			if(inviterRPG.getHangoutPlayer().isInMenu() && inviterRPG.getHangoutPlayer().getOpenMenu().getTitle().equals(p.getName())){
				RpgMenuUtils.createPlayerMenu(inviterRPG.getHangoutPlayer(), p).openMenu(inviterRPG.getHangoutPlayer());
			}
			
			p.clearComandPreparer("guild_invite");
			p.clearComandPreparer("guild_decline");
			return true;
		}
		
		//guild decline <inviter uuid>
		if(args[1].equals("decline")){
			Player inviter = Bukkit.getPlayer(UUID.fromString(args[2]));
			HangoutPlayer inviterHP = HangoutPlayerManager.getPlayer(inviter);
			
			p.getClickableName(inviterHP, false)
				.then(" has declined your guild invite.")
			.send(inviterHP.getPlayer());
			
			p.clearComandPreparer("guild_invite");
			p.clearComandPreparer("guild_decline");
			return true;
		}
		
		return false;
	}

}
