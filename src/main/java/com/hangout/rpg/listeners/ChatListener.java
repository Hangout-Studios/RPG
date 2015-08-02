package com.hangout.rpg.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.hangout.core.chat.ChatManager;
import com.hangout.core.events.ChatPlayerClickEvent;
import com.hangout.core.player.HangoutPlayer;
import com.hangout.core.player.HangoutPlayerManager;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.RpgMenuUtils;

public class ChatListener implements Listener {
		
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		e.setCancelled(true);
		
		HangoutPlayer p = HangoutPlayerManager.getPlayer(e.getPlayer());
		if(!p.getChatChannel().getPlugin().equals("rpg")) return;
		
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUUID());
		List<HangoutPlayer> receipents = new ArrayList<HangoutPlayer>();
		String tag = p.getChatChannel().getTag();
		if(tag.equals("guild") && rpgP.getGuild() != null){
			for(RpgPlayer member : rpgP.getGuild().getMembers()){
				receipents.add(member.getHangoutPlayer());
			}
		}
		
		String message = e.getMessage();
		ChatManager.sendMessage(p, message, p.getChatChannel(), receipents);
	}
	
	@EventHandler
	public void onChatPlayerClick(ChatPlayerClickEvent e){
		RpgMenuUtils.createPlayerMenu(e.getPlayer(), e.getClicked()).openMenu(e.getPlayer(), true);
	}
}
