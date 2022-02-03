package BSelector.Command;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import BSelector.Main;
import BSelector.Utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;

public class Command implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String lb, String[] args) {
		if (!(sender instanceof Player))
			return true;
		Player p = (Player) sender;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				sender.sendMessage("§aConfiguração recarregada.");
				Main.getInstance().reloadConfig();
				return true;
			} 
		}
		abrirMenu(p);
		return false;
	}

	public static void abrirMenu(Player p) {
		FileConfiguration config = Main.getInstance().getConfig();
		Inventory Selector = Bukkit.createInventory(null, Main.getInstance().getConfig().getInt("ChestSlots"),
				Main.getInstance().getConfig().getString("NomeMenu").replace("&", "§"));
		Set<String> keys = config.getConfigurationSection("Servidores").getKeys(false);

		for (String key : keys) {
			String padrao = "Servidores." + key;
			ItemStack Icone = new ItemStack(Material.DIRT);
			if (config.getString(padrao + ".Head") != null) {
				Icone = Utils.getCustomTextureHead(config.getString(padrao + ".Head"));
			} else {
				Icone = new ItemStack(Material.getMaterial(config.getString("Servidores." + key + ".Item")));
				Icone.setDurability((short) config.getInt(padrao + ".Short"));
			}
			ItemMeta IconeMeta = Icone.getItemMeta();
			IconeMeta.setDisplayName(config.getString(padrao + ".Nome").replace("&", "§"));
			List<String> lore = config.getStringList(padrao + ".Lore").stream().map(
					message -> ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(p, message)))
					.collect(Collectors.toList());
			IconeMeta.setLore(lore);
			if (config.getBoolean(padrao + ".ItemBrilhante")) {
				IconeMeta.addEnchant(Enchantment.DURABILITY, 1, false);
				IconeMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			Icone.setItemMeta(IconeMeta);
			Selector.setItem(config.getInt(padrao + ".Slot"), Icone);
		}

		p.openInventory(Selector);
	}

}
