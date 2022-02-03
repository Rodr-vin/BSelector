package BSelector.Utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import BSelector.Main;
import me.clip.placeholderapi.PlaceholderAPI;

public class Utils {
	// by Esmorall
	public static ItemStack getCustomTextureHead(String value) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta meta = (SkullMeta) head.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), "");
		profile.getProperties().put("textures", new Property("textures", value));
		Field profileField = null;
		try {
			profileField = meta.getClass().getDeclaredField("profile");
			profileField.setAccessible(true);
			profileField.set(meta, profile);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		head.setItemMeta(meta);
		return head;
	}

	public static void updatePlaceholders() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getOpenInventory() != null) {
				if (p.getOpenInventory().getTitle()
						.equals(Main.getInstance().getConfig().getString("NomeMenu").replace("&", "§"))) {
					FileConfiguration config = Main.getInstance().getConfig();
					Set<String> keys = config.getConfigurationSection("Servidores").getKeys(false);

					for (String key : keys) {
						String padrao = "Servidores." + key;
						ItemStack Icone = new ItemStack(Material.DIRT);
						if (config.getString(padrao + ".Head") != null) {
							Icone = Utils.getCustomTextureHead(config.getString(padrao + ".Head"));
						} else {
							Icone = new ItemStack(Material.getMaterial(config.getString("Servidores." + key + ".Item")));
							if (config.get(padrao + ".Short") != null) {
								Icone.setDurability((short) config.getInt(padrao + ".Short"));
							}
						}
						ItemMeta IconeMeta = Icone.getItemMeta();
						IconeMeta.setDisplayName(config.getString(padrao + ".Nome").replace("&", "§"));
						List<String> lore = config
								.getStringList(padrao + ".Lore").stream().map(message -> ChatColor
										.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, message)))
								.collect(Collectors.toList());
						IconeMeta.setLore(lore);
						if (config.getBoolean(padrao + ".ItemBrilhante")) {
							IconeMeta.addEnchant(Enchantment.DURABILITY, 1, false);
							IconeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						}
						Icone.setItemMeta(IconeMeta);
						p.getOpenInventory().setItem(config.getInt(padrao + ".Slot"), Icone);
					}
				}
			}
		}
	}

}
