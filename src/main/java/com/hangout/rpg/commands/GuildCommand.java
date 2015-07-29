package com.hangout.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;

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
		}
		
		return false;
	}

}
