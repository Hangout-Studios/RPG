package com.hangout.rpg.guild;

import java.util.Arrays;
import java.util.List;

public enum GuildBonusType{
	EXPERIENCE("Hunting Horns", 
			Arrays.asList("Receive a 10% increase in experience", "for a day upon activation."), 
			1000, 2, false, true),
	MONEY("Bank Deal", 
			Arrays.asList("Receive a 10% increase in gold", "for a day upon activation."), 
			1000, 4, false, true),
	MOB_DROP("Carving Knives", 
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when killing a mob", "for a day upon activation."), 
			1000, 1, false, true),
	BLOCK_DROP("Prospecting Kit", 
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when breaking blocks", "for a day upon activation."), 
			1000, 3, false, true);
	
	private String displayName;
	private List<String> description;
	private int price;
	private boolean isAvailable;
	private boolean permanent;
	private int level;
	
	GuildBonusType(String displayName, List<String> description, int price, int requiredLevel, boolean permanent, boolean isAvailable){
		this.displayName = displayName;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
		this.permanent = permanent;
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
}
