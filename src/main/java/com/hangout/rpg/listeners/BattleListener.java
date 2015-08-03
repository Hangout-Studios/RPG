package com.hangout.rpg.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hangout.core.utils.mc.NumberUtils;
import com.hangout.rpg.player.PlayerStat;
import com.hangout.rpg.player.RpgPlayer;
import com.hangout.rpg.player.RpgPlayerManager;
import com.hangout.rpg.utils.PlayerOccupations;

public class BattleListener implements Listener {
	
	@EventHandler
	public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e){
		if(e.isCancelled()) return;
		
		Player hitP = null;
		Player damagerP = null;
		
		if(e.getEntity() instanceof Player){
			hitP = (Player)e.getEntity();
		}
		
		if(e.getDamager() instanceof Player){
			damagerP = (Player)e.getDamager();
		}
		
		if(e.getEntity() instanceof Projectile){
			Projectile projectile = (Projectile)e.getEntity();
			if(projectile.getShooter() instanceof Player){
				damagerP = (Player)projectile.getShooter();
			}
		}
		
		if(hitP == null || damagerP == null){
			return;
		}

		RpgPlayer hitHP = RpgPlayerManager.getPlayer(hitP.getUniqueId());
		RpgPlayer damagerHP = RpgPlayerManager.getPlayer(damagerP.getUniqueId());
		
		if(hitHP == null || damagerHP == null){
			return;
		}
		
		int meleeDamage = damagerHP.getStats().getStat(PlayerStat.DAMAGE_MELEE);
		if(e.getEntity() instanceof Player){
			e.setDamage(meleeDamage);
		}
		
		int rangedDamage = damagerHP.getStats().getStat(PlayerStat.DAMAGE_RANGED);
		if(e.getEntity() instanceof Projectile){
			e.setDamage(rangedDamage);
		}
		
		if(NumberUtils.rollPercentage(damagerHP.getStats().getStat(PlayerStat.POISON_CHANCE))){
			hitP.addPotionEffect(new PotionEffect(PotionEffectType.POISON, damagerHP.getStats().getStat(PlayerStat.POISON_DURATION), 1, false));
		}
	}
	
	@EventHandler
	public void onEntityDeath(EntityDeathEvent e){
		Player p = e.getEntity().getKiller();
		if(p == null || e.getEntity() instanceof Player) return;
		
		RpgPlayer rpgP = RpgPlayerManager.getPlayer(p.getUniqueId());
		
		rpgP.addExperience(2, true, "KILL_MOB_" + e.getEntity().getType().toString(), PlayerOccupations.WARRIOR);
		
		//Add reputation
		//rpgP.getStats().getStat(PlayerStat.REPUTATION_ON_KILL);
	}
}
