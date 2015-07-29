package com.hangout.rpg.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.hangout.core.events.ChatPlayerClickEvent;
import com.hangout.rpg.utils.MenuUtils;

public class ChatListener implements Listener {
	
	@EventHandler
	public void onChatPlayerClick(ChatPlayerClickEvent e){
		MenuUtils.createPlayerMenu(e.getPlayer(), e.getClicked()).openMenu(e.getPlayer());
	}
}
