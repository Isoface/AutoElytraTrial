package es.outlook.adriansrj.atly;

import es.outlook.adriansrj.atly.handler.AutoElytraHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AdrianSR / 12/3/2024 / 2:11 p. m.
 */
public final class AutoElytra extends JavaPlugin {
	
	@Override
	public void onEnable ( ) {
		new AutoElytraHandler ( this );
	}
}