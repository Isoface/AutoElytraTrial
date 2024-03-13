package es.outlook.adriansrj.atly.handler;

import es.outlook.adriansrj.atly.AutoElytra;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author AdrianSR / 12/3/2024 / 2:13 p. m.
 */
public final class AutoElytraHandler implements Listener {
	
	/**
	 * @author AdrianSR / 12/3/2024 / 8:22 p. m.
	 */
	private static class Data {
		
		private boolean   enabled;
		private ItemStack backup;
	}
	
	private static final Map < UUID, Data > DATA_MAP = new HashMap <> ( );
	
	public AutoElytraHandler ( AutoElytra plugin ) {
		Bukkit.getPluginManager ( ).registerEvents ( this , plugin );
	}
	
	@EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onFall ( PlayerMoveEvent event ) {
		Player   player = event.getPlayer ( );
		Location from   = event.getFrom ( );
		Location to     = event.getTo ( );
		
		if ( to == null ) {
			return;
		} else if ( to.getY ( ) >= from.getY ( ) ) {
			elytraCheck ( player , false );
			return;
		}
		
		float fallenSoFar = player.getFallDistance ( );
		
		if ( fallenSoFar >= 5.0F ) {
			elytraCheck ( player , true );
		}
	}
	
	@EventHandler ( priority = EventPriority.MONITOR )
	public void onFallDamage ( EntityDamageEvent event ) {
		if ( event.getEntity ( ) instanceof Player player
				&& event.getCause ( ) == EntityDamageEvent.DamageCause.FALL ) {
			elytraCheck ( player , false );
		}
	}
	
	private void elytraCheck ( Player player , boolean enable ) {
		Data      data  = getData ( player );
		ItemStack chest = player.getInventory ( ).getChestplate ( );
		
		if ( enable && !data.enabled ) {
			data.enabled = true;
			data.backup  = chest != null ? chest.clone ( ) : null;
			
			if ( chest == null || chest.getType ( ) != Material.ELYTRA ) {
				player.getInventory ( ).setChestplate ( new ItemStack ( Material.ELYTRA ) );
			}
		} else if ( !enable && data.enabled ) {
			player.getInventory ( ).setChestplate ( data.backup );
			
			data.enabled = false;
			data.backup  = null;
		}
	}
	
	private Data getData ( Player player ) {
		return DATA_MAP.computeIfAbsent ( player.getUniqueId ( ) , k -> new Data ( ) );
	}
}