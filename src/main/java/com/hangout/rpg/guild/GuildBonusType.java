package com.hangout.rpg.guild;

import java.util.Arrays;
import java.util.List;

public enum GuildBonusType{
	EXPERIENCE("Hunting Horns", 
			Arrays.asList("Receive a 10% increase in experience", "for a day upon activation."), 
			1000, true),
	MONEY("Bank Deal", 
			Arrays.asList("Receive a 10% increase in gold", "for a day upon activation."), 
			1000, true),
	MOB_DROP("Carving Knives", 
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when killing a mob", "for a day upon activation."), 
			1000, true),
	BLOCK_DROP("Prospecting Kit", 
			Arrays.asList("Receive 10% increase chance to", "receive extra drops when breaking blocks", "for a day upon activation."), 
			1000, true);
	
	private String displayName;
	private List<String> description;
	private int price;
	private boolean isAvailable;
	
	GuildBonusType(String displayName, List<String> description, int price, boolean isAvailable){
		this.displayName = displayName;
		this.description = description;
		this.price = price;
		this.isAvailable = isAvailable;
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
	
	public boolean isAvailable(){
		return isAvailable;
	}
}
