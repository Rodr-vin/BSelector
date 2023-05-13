package BSelector.Events;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import BSelector.Main;
import me.clip.placeholderapi.PlaceholderAPI;

public class EventHandler implements Listener {

	@org.bukkit.event.EventHandler
	public void interagirInv(InventoryClickEvent e) {
		String invName = e.getView().getTitle();
		if (e.getView() != null && invName != null
				&& invName.equalsIgnoreCase(Main.getInstance().getConfig().getString("NomeMenu").replace("&", "§"))) {
			ItemStack item = e.getCurrentItem();
			e.setCancelled(true);
			if (item != null && (item.hasItemMeta() && (item.getItemMeta().hasDisplayName()))) {
				Player p = (Player) e.getWhoClicked();
				Set<String> keys = Main.getInstance().getConfig().getConfigurationSection("Servidores").getKeys(false);

				for (String key : keys) {
					if (Main.getInstance().getConfig().getString("Servidores." + key + ".BungeeName") != null) {
						if (Main.getInstance().getConfig().getInt("Servidores." + key + ".Slot") == e.getSlot()) {
							ByteArrayOutputStream b = new ByteArrayOutputStream();
							DataOutputStream out = new DataOutputStream(b);
							try {
								out.writeUTF("Connect");
								out.writeUTF("" + servidor);
							} catch (IOException excep) {
								excep.printStackTrace();
							}
							p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
						}
					}
				}
			}

		}
	}

}
