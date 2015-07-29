package com.hangout.rpg;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.hangout.core.HangoutAPI;
import com.hangout.core.utils.database.Database.PropertyTypes;
import com.hangout.core.utils.lang.MessageManager;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.listeners.ChatListener;
import com.hangout.rpg.listeners.MenuListener;
import com.hangout.rpg.listeners.PlayerListener;
import com.mysql.jdbc.StringUtils;

public class Plugin extends JavaPlugin {
	
	private static Plugin instance;
	private MessageManager messageBundle;
	
	public void onEnable(){
		
		instance = this;
		messageBundle = new MessageManager(getConfig().getString("locale", "en"));
		
		this.saveDefaultConfig();
		
		GuildManager.loadGuilds();
		
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		this.getServer().getPluginManager().registerEvents(new MenuListener(), this);
		this.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		
		HangoutAPI.addCustomPlayerProperty("race", PropertyTypes.STRING);
		
		HangoutAPI.createMenuItem(HangoutAPI.getMenu("mainmenu"), Material.ARROW, "Friends list", Arrays.asList("Click to check out your friends"), 4 + 9, "friend_item");
	}
	
	public void onDisable(){
		
	}
	
	public static Plugin getInstance(){
		return instance;
	}
	
	public static String getString(String s){
        if (Plugin.instance.messageBundle == null) {
            return s;
        }
        String message = Plugin.instance.messageBundle.getString(s);
        if (StringUtils.isNullOrEmpty(message)) {
            return s;
        } else {
            return ChatColor.translateAlternateColorCodes('&', message.replace("%&", " "));
        }
	}
}
