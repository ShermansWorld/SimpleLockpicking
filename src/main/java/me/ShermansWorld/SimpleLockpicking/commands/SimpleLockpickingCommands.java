package me.ShermansWorld.SimpleLockpicking.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ShermansWorld.SimpleLockpicking.CraftingRecipes;
import me.ShermansWorld.SimpleLockpicking.Main;
import me.ShermansWorld.SimpleLockpicking.lang.Languages;

public class SimpleLockpickingCommands implements CommandExecutor {
	
	public static YamlConfiguration lang = Languages.getLang();
	
	public SimpleLockpickingCommands(Main plugin) {
		
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		if (args.length != 1) {
			p.sendMessage(ChatColor.YELLOW + "[SimpleLockpicking] " + lang.getString("SimpleLockpickingCommands.Reload"));
			return false;
		}
		if (args[0].equalsIgnoreCase("reload")) {
			if (!p.hasPermission("simplelockpicking.reload")) {
				p.sendMessage(ChatColor.RED + "[SimpleLockpicking] " + lang.getString("SimpleLockpickingCommands.NoPermission"));
				return false;
			}
			Main.getInstance().reloadConfig();
	    	Main.getInstance().saveDefaultConfig();
	    	p.sendMessage(ChatColor.YELLOW + "[SimpleLockpicking] " + lang.getString("SimpleLockpickingCommands.ReloadSuccess"));
	    	return true;
		} else if (args[0].equalsIgnoreCase("give")) {
			if (!p.hasPermission("simplelockpicking.give")) {
				p.sendMessage(ChatColor.RED + "[SimpleLockpicking] " + lang.getString("SimpleLockpickingCommands.NoPermission"));
				return false;
			}
			p.getInventory().addItem(CraftingRecipes.getLockPick());
			return true;
		}
		return false;
	}
}

