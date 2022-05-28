package me.ShermansWorld.SimpleLockpicking.hooks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.craftbook.CraftBookMechanic;
import com.sk89q.craftbook.bukkit.CraftBookPlugin;
import com.sk89q.craftbook.mechanics.area.simple.Gate;

import me.ShermansWorld.SimpleLockpicking.CraftingRecipes;
import me.ShermansWorld.SimpleLockpicking.Main;
import me.ShermansWorld.SimpleLockpicking.lang.Languages;
import me.ShermansWorld.SimpleLockpicking.listeners.LockpickListener;

public class CraftBookCompatibility {

	public static boolean init = false;
	public static YamlConfiguration lang = Languages.getLang();
	private static Gate gate;
	private static CraftBookPlugin craftBook;
	private static final Set<Material> signs = new HashSet<>();
	static {
		signs.add(Material.ACACIA_SIGN);
		signs.add(Material.BIRCH_SIGN);
		signs.add(Material.DARK_OAK_SIGN);
		signs.add(Material.JUNGLE_SIGN);
		signs.add(Material.SPRUCE_SIGN);
		signs.add(Material.OAK_SIGN);
		signs.add(Material.CRIMSON_SIGN);
		signs.add(Material.WARPED_SIGN);
		signs.add(Material.ACACIA_WALL_SIGN);
		signs.add(Material.BIRCH_WALL_SIGN);
		signs.add(Material.DARK_OAK_WALL_SIGN);
		signs.add(Material.JUNGLE_WALL_SIGN);
		signs.add(Material.SPRUCE_WALL_SIGN);
		signs.add(Material.OAK_WALL_SIGN);
		signs.add(Material.CRIMSON_WALL_SIGN);
		signs.add(Material.WARPED_WALL_SIGN);
	}
	private static final Set<Material> gateBlocks = new HashSet<>();
	static {
		
		// fences
		gateBlocks.add(Material.ACACIA_FENCE);
		gateBlocks.add(Material.BIRCH_FENCE);
		gateBlocks.add(Material.JUNGLE_FENCE);
		gateBlocks.add(Material.OAK_FENCE);
		gateBlocks.add(Material.SPRUCE_FENCE);
		gateBlocks.add(Material.DARK_OAK_FENCE);
		gateBlocks.add(Material.CRIMSON_FENCE);
		gateBlocks.add(Material.WARPED_FENCE);
		gateBlocks.add(Material.NETHER_BRICK_FENCE);
		
		// glass panes
		gateBlocks.add(Material.BLACK_STAINED_GLASS_PANE);
		gateBlocks.add(Material.BLUE_STAINED_GLASS_PANE);
		gateBlocks.add(Material.BROWN_STAINED_GLASS_PANE);
		gateBlocks.add(Material.CYAN_STAINED_GLASS_PANE);
		gateBlocks.add(Material.GLASS_PANE);
		gateBlocks.add(Material.GRAY_STAINED_GLASS_PANE);
		gateBlocks.add(Material.GREEN_STAINED_GLASS_PANE);
		gateBlocks.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
		gateBlocks.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
		gateBlocks.add(Material.LIME_STAINED_GLASS_PANE);
		gateBlocks.add(Material.MAGENTA_STAINED_GLASS_PANE);
		gateBlocks.add(Material.ORANGE_STAINED_GLASS_PANE);
		gateBlocks.add(Material.PINK_STAINED_GLASS_PANE);
		gateBlocks.add(Material.PURPLE_STAINED_GLASS_PANE);
		gateBlocks.add(Material.RED_STAINED_GLASS_PANE);
		gateBlocks.add(Material.WHITE_STAINED_GLASS_PANE);
		gateBlocks.add(Material.YELLOW_STAINED_GLASS_PANE);
		
		//other
		gateBlocks.add(Material.IRON_BARS);
	}

	public static void initCraftBook() {
		craftBook = JavaPlugin.getPlugin(CraftBookPlugin.class);
		for (CraftBookMechanic mechanic : craftBook.getMechanics()) {
			if (mechanic.toString().contains("Gate")) {
				gate = (Gate) mechanic;
			}
		}
		init = true;
	}
	
	public static boolean isGateBlock(Block block) {
		// check initial block for a gate block (fence, iron bars, etc)
		if (gateBlocks.contains(block.getType())) {
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

	public static void toggleGate(Player player, Block gateSign) {
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

}
