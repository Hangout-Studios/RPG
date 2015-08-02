package com.hangout.rpg.guild;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum GuildBonusType{
	EXPERIENCE(ChatColor.RED + "Hunting Horns", Material.EXP_BOTTLE,
			Arrays.asList("Receive a 10% increase in experience", "for a day upon activation."), 
			1000, 2, false, true),
	MONEY(ChatColor.RED + "Bank Deal", Material.GOLD_BLOCK,
			Arrays.asList("Receive a 10% increase in gold", "for a day upon activation."), 
			1000, 4, false, true),
	MOBDROP(ChatColor.RED + "Carving Knives", Material.DIAMOND_SWORD,
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when killing a mob", "for a day upon activation."), 
			1000, 1, false, true),
	BLOCKDROP(ChatColor.RED + "Prospecting Kit", Material.WORKBENCH,
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when breaking blocks", "for a day upon activation."), 
			1000, 3, false, true);
	
	private String displayName;
	private List<String> description;
	private int price;
	private boolean isAvailable;
	private boolean permanent;
	private int level;
	private Material material;
	
	GuildBonusType(String displayName, Material iconMaterial, List<String> description, int price, int requiredLevel, boolean permanent, boolean isAvailable){
		this.displayName = displayName;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.permanent = permanent;
		this.material = iconMaterial;
	}
	
	public String getDisplayName(){
		return displayName;
	}
	
	public List<String> getDescription(){
		return description;
	}
	
	public int getPrice(){
		return price;
	}
	
	public int getRequiredLevel(){
		return level;
	}
	
	public boolean isAvailable(){
		return isAvailable;
	}
	
	public boolean isPermanent(){
		return permanent;
	}
	
	public Material getMaterial(){
		return material;
	}
}
