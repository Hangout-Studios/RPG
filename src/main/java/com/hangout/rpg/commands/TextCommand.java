package com.hangout.rpg.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerOccupations;

public class TextCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		
		//text occupation <occupation> <key>
		if(args.length == 3 && args[0].equals("occupation")){
			HangoutPlayer p = HangoutPlayerManager.getPlayer((Player)sender);
			if(Integer.parseInt(args[2]) == p.getCommandKey("switch_occupation")){
				p.removeCommandKey("switch_occupation");

				RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUUID());
				PlayerOccupations occupation = PlayerOccupations.valueOf(args[1]);
				rpgP.setOccupation(occupation, true);
				
				p.getPlayer().sendMessage("There you go. Now go do " + occupation.getDisplayName() + "-y stuff.");
				return true;
			}else{
				p.getPlayer().sendMessage("Talk to me again if you wish to change occupation.");
				return true;
			}
		}
		
		return false;
	}

}
