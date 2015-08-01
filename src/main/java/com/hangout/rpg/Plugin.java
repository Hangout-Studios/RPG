package com.hangout.rpg;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.hangout.core.chat.ChatChannel.ChatChannelType;
import com.hangout.core.chat.ChatManager;
import com.hangout.core.utils.database.Database;
import com.hangout.core.utils.database.Database.PropertyTypes;
import com.hangout.core.utils.lang.MessageManager;
import com.hangout.rpg.commands.GuildCommand;
import com.hangout.rpg.guild.GuildManager;
import com.hangout.rpg.listeners.BattleListener;
import com.hangout.rpg.listeners.ChatListener;
import com.hangout.rpg.listeners.LevelListener;
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
		this.getServer().getPluginManager().registerEvents(new LevelListener(), this);
		this.getServer().getPluginManager().registerEvents(new BattleListener(), this);
		
		this.getCommand("guild").setExecutor(new GuildCommand());
		
		Database.addCustomPlayerProperty("race", PropertyTypes.STRING);
		
		//HangoutAPI.createMenuItem(Material.ARROW, "Friends list", Arrays.asList("Click to check out your friends"), 4 + 9, "friend_item");
		
		ChatManager.createChannel("guild", ChatColor.GREEN + "Guild", Arrays.asList("Only for guild members."), ChatChannelType.SERVER_WIDE, Material.BANNER, true, true);
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
