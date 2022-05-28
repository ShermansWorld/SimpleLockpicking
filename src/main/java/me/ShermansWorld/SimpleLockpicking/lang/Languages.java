package me.ShermansWorld.SimpleLockpicking.lang;


import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ShermansWorld.SimpleLockpicking.Main;

public class Languages {
	
	public static void initEnglish() {
		File english = new File("plugins" + File.separator + "SimpleLockpicking" + File.separator + "lang" + File.separator + "enUS.yml");
		if (!english.exists()) {
			try {
				english.createNewFile();
			} catch (IOException e) {
				Bukkit.getLogger().warning("[SimpleLockpicking] Error when creating language file 'enUS'");
			}
			YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(english);
			yamlConfiguration.set("NoPermission", "You do not have permission to do this");
			yamlConfiguration.set("SimpleLockpickingCommands", null);
			yamlConfiguration.set("SimpleLockpickingCommands.Reload", "To reload the config type /simplelockpicking reload");
			yamlConfiguration.set("SimpleLockpickingCommands.ReloadSuccess", "config.yml reloaded");
			yamlConfiguration.set("Lockpicks", null);
			yamlConfiguration.set("Lockpicks.Success", "The lock has successfully been opened!");
			yamlConfiguration.set("Lockpicks.Fail1", "The lockpick broke!");
			yamlConfiguration.set("Lockpicks.Fail2", "You are already trying to lockpick something!");
			yamlConfiguration.set("Lockpicks.Use", "Here goes nothing...");
			yamlConfiguration.set("Towny", null);
			yamlConfiguration.set("Towny.Fail1", "You cannot lockpick a town's chest!");
			yamlConfiguration.set("Towny.Fail2", "You cannot lockpick your town's chest!");
			try {
				yamlConfiguration.save(english);
			} catch (IOException e) {
				Bukkit.getLogger().warning("[CharacterCards] Error when saving language file 'enUS'");
			}
		}
	}
	
	public static void initLangs() {
		initEnglish();
	}
	
	public static YamlConfiguration getEnglish() {
		File english = new File("plugins" + File.separator + "SimpleLockpicking" + File.separator + "lang" + File.separator + "enUS.yml");
		return YamlConfiguration.loadConfiguration(english);
	}
	
	public static YamlConfiguration getLang() {
		if (Main.getInstance().getConfig().getString("lang").equalsIgnoreCase("enUs")) {
			return getEnglish();
		} else {
			File otherLang = new File("plugins" + File.separator + "SimpleLockpicking" + File.separator + "lang" + File.separator + Main.getInstance().getConfig().getString("lang"));
			return YamlConfiguration.loadConfiguration(otherLang);
		}
	}
}

