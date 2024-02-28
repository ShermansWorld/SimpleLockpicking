package me.ShermansWorld.SimpleLockpicking.hooks;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.craftbook.CraftBookMechanic;
import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import com.sk89q.craftbook.mechanics.area.simple.Door;
import com.sk89q.craftbook.mechanics.area.simple.Gate;

import me.ShermansWorld.SimpleLockpicking.CraftingRecipes;
import me.ShermansWorld.SimpleLockpicking.Main;
import me.ShermansWorld.SimpleLockpicking.lang.Languages;
import me.ShermansWorld.SimpleLockpicking.listeners.LockpickListener;

public class CraftBookCompatibility {
	
	public static YamlConfiguration lang = Languages.getLang();
	private static Gate gate;
	private static Door door;
	private static CraftBookPlugin craftBook;
	private static final List<Material> signs = new ArrayList<>(Tag.SIGNS.getValues());
	private static List<Material> gateBlocks = new ArrayList<>();
	private static List<Material> doorBlocks = new ArrayList<>();

	public static void initCraftBook() {
		craftBook = JavaPlugin.getPlugin(CraftBookPlugin.class);
		for (CraftBookMechanic mechanic : craftBook.getMechanics()) {
			if (mechanic.toString().contains("Gate")) {
				gate = (Gate) mechanic;
				for (String blockStr : gate.getDefaultBlocks()) {
					//remove "minecraft:" in string
					blockStr = blockStr.substring(10);
					blockStr = blockStr.toUpperCase();
					gateBlocks.add(Material.getMaterial(blockStr));
				}
			} else if (mechanic.toString().contains("Door")) {
				door = (Door) mechanic;
				for (String blockStr : door.getDefaultBlocks()) {
					//remove "minecraft:" in string
					blockStr = blockStr.substring(10);
					blockStr = blockStr.toUpperCase();
					doorBlocks.add(Material.getMaterial(blockStr));
				}
			}
		}
	}
	
	public static boolean isGateBlock(Block block) {
		// check initial block for a gate block (fence, iron bars, etc)
		if (gateBlocks.contains(block.getType())) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isDoorBlock(Block block) {
		// check initial block for a door block
		if (doorBlocks.contains(block.getType())) {
			return true;
		} else {
			return false;
		}
	}
	
	// returns gate sign if found, if not found returns null
	public static Block reverseGateCheck(Block block, Player player) {
		
	
		// check if sign is clicked
		if (isGateSign(block)) {
			return block;
		}		
		
		// Search radius for gate sign
		int radius = 10;
		for (int x = -(radius); x <= radius; x++) {
			for (int y = -(radius); y <= radius; y++) {
				for (int z = -(radius); z <= radius; z++) {
					if (isGateSign(block.getRelative(x, y, z))) { 
						return block.getRelative(x, y, z);
					}
				}
			}
		}
		return null;
	}

	public static void toggleGate(final Player player, final Block gateSign) {
		player.getInventory().clear(player.getInventory().getHeldItemSlot());
		new BukkitRunnable() {
			@Override
			public void run() {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
				// toggles gate closest to a location
				// block: used for location
				// false: small search radius
				// null: toggle gate (true = close, false = open)
				gate.toggleGates(craftBook.wrapPlayer(player), gateSign, false, null);
				player.getInventory().addItem(CraftingRecipes.getLockPick());
				LockpickListener.lockpickMap.put(player.getName(), 0);
			}
		}.runTaskLater(Main.getInstance(), 40);
		if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) { 
			new BukkitRunnable() {
				@Override
				public void run() {
					gate.toggleGates(craftBook.wrapPlayer(player), gateSign, false, null);
				}
			}.runTaskLater(Main.getInstance(), (long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses")*20) + 40);
		}
	}
	
	public static void toggleDoor(final Player player, final Block doorSign) {
		// TODO:
	}

	public static boolean isGateSign(Block block) {
		if (block.getType().isAir()) {
			return false;
		}
		//Bukkit.broadcastMessage(block.getType().toString());
		if (signs.contains(block.getType())) {
			Sign sign = (Sign) block.getState();
			if (sign.getLine(1).contentEquals("[Gate]")) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isDoorSign(Block block) {
		if (block.getType().isAir()) {
			return false;
		}
		//Bukkit.broadcastMessage(block.getType().toString());
		if (signs.contains(block.getType())) {
			Sign sign = (Sign) block.getState();
			if (sign.getLine(1).contentEquals("[Door]")) {
				return true;
			}
		}
		return false;
	}

}
