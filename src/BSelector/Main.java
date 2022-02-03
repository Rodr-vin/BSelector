package BSelector;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import BSelector.Command.Command;
import BSelector.Events.EventHandler;
import BSelector.Utils.Utils;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
			getLogger().warning("§cPlaceHolderAPI não encontrado. Plugin desabilitado.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		Bukkit.getMessenger().registerOutgoingPluginChannel((Plugin) this, "BungeeCord");
		getCommand("selector").setExecutor(new Command());
		Bukkit.getPluginManager().registerEvents((Listener) new EventHandler(), (Plugin) this);
		saveDefaultConfig();
		temporizador();
	}

	public void temporizador() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin) this, new Runnable() {
			public void run() {
				Utils.updatePlaceholders();
			}
		}, 0L, 20L * 3);
	}

	@Override
	public void onDisable() {
		Bukkit.getMessenger().unregisterIncomingPluginChannel(getInstance());
	}

	public static Plugin getInstance() {
		return (Main) getPlugin(Main.class);
	}

}
