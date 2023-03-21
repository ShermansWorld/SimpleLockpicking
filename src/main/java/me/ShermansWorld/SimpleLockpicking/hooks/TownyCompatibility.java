package me.ShermansWorld.SimpleLockpicking.hooks;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.WorldCoord;

import me.ShermansWorld.SimpleLockpicking.Main;
import me.ShermansWorld.SimpleLockpicking.lang.Languages;
import me.ShermansWorld.SimpleLockpicking.listeners.LockpickListener;


public class TownyCompatibility {
	public static YamlConfiguration lang = Languages.getLang();
	public static void TownyChest(Player p, Block b, World w) {
		try {
			TownBlock townBlock = WorldCoord.parseWorldCoord(b.getLocation()).getTownBlock();
			Resident resident = TownyAPI.getInstance().getResident(p);
			if (!resident.hasTown()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Towny.Fail1")));
				return;
			}
			Town town = resident.getTown();
			if (town.hasTownBlock(townBlock)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Towny.Fail2")));
				return;
			}
		} catch (NotRegisteredException ev) {
			if (!LockpickListener.lockpickMap.containsKey(p.getName())) {
				LockpickListener.lockpickMap.put(p.getName(), 0);
        	} else {
        		if (LockpickListener.lockpickMap.get(p.getName()) == 1) {
        			p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Lockpicks.Fail2")));
        			return;
        		}
        	}
			LockpickListener.lockpickMap.put(p.getName(), 1);
        	p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
			if (LockpickListener.getRandom(Main.getInstance().getConfig().getInt("Chances.Chests")) == 1) {
				LockpickListener.openChest(b, w, p);
			} else {
				LockpickListener.pickFailed(p);
			}
		}
	}
	
	public static void TownyShulker(Player p, Block b, World w) {
		try {
			TownBlock townBlock = WorldCoord.parseWorldCoord(b.getLocation()).getTownBlock();
			Resident resident = TownyAPI.getInstance().getResident(p);
			if (!resident.hasTown()) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Towny.Fail1")));
				return;
			}
			Town town = resident.getTown();
			if (town.hasTownBlock(townBlock)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Towny.Fail2")));
				return;
			}
		} catch (NotRegisteredException ev) {
			if (!LockpickListener.lockpickMap.containsKey(p.getName())) {
				LockpickListener.lockpickMap.put(p.getName(), 0);
        	} else {
        		if (LockpickListener.lockpickMap.get(p.getName()) == 1) {
        			p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &c" + lang.getString("Lockpicks.Fail2")));
        			return;
        		}
        	}
			LockpickListener.lockpickMap.put(p.getName(), 1);
        	p.sendMessage(ChatColor.translateAlternateColorCodes('&',"&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
			if (LockpickListener.getRandom(Main.getInstance().getConfig().getInt("Chances.ShulkerBox")) == 1) {
				LockpickListener.openShulker(b, w, p);
			} else {
				LockpickListener.pickFailed(p);
			}
		}
	}
}
