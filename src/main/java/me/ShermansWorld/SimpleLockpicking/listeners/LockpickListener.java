package me.ShermansWorld.SimpleLockpicking.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.ShulkerBox;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.Gate;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import me.ShermansWorld.SimpleLockpicking.CraftingRecipes;
import me.ShermansWorld.SimpleLockpicking.Main;
import me.ShermansWorld.SimpleLockpicking.hooks.CraftBookCompatibility;
import me.ShermansWorld.SimpleLockpicking.hooks.TownyCompatibility;
import me.ShermansWorld.SimpleLockpicking.lang.Languages;

public class LockpickListener implements Listener {

	public static YamlConfiguration lang = Languages.getLang();

	private static String mess(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	PluginManager pm = Main.getInstance().getServer().getPluginManager();

	private static boolean chestsAllowed = true;
	private static boolean trappedChestsAllowed = true;
	private static boolean ironDoorsAllowed = true;
	private static boolean ironTrapdoorsAllowed = true;
	private static boolean barrelsAllowed = true;
	private static Random random = new Random();

	public static Map<String, Integer> lockpickMap = new HashMap<>();

	private static final List<Material> normalDoors = new ArrayList<>(Tag.WOODEN_DOORS.getValues());
	private static final List<Material> gates = new ArrayList<>(Tag.FENCE_GATES.getValues());
	private static final List<Material> trapdoors = new ArrayList<>(Tag.WOODEN_TRAPDOORS.getValues());
	private static final List<Material> shulkers = new ArrayList<>(Tag.SHULKER_BOXES.getValues());

	public static void removeDisabled() {
		List<String> disabledConfigList = Main.getInstance().getConfig().getStringList("Disabled");
		for (int i = 0; i < disabledConfigList.size(); i++) {
			switch (disabledConfigList.get(i)) {
			case "ACACIA_DOOR":
				normalDoors.remove(Material.ACACIA_DOOR);
				break;
			case "BAMBOO_DOOR":
				normalDoors.remove(Material.BAMBOO_DOOR);
				break;
			case "BIRCH_DOOR":
				normalDoors.remove(Material.BIRCH_DOOR);
				break;
			case "CHERRY_DOOR":
				normalDoors.remove(Material.BIRCH_DOOR);
				break;
			case "CRIMSON_DOOR":
				normalDoors.remove(Material.CRIMSON_DOOR);
				break;
			case "DARK_OAK_DOOR":
				normalDoors.remove(Material.DARK_OAK_DOOR);
				break;
			case "JUNGLE_DOOR":
				normalDoors.remove(Material.JUNGLE_DOOR);
				break;
			case "OAK_DOOR":
				normalDoors.remove(Material.OAK_DOOR);
				break;
			case "SPRUCE_DOOR":
				normalDoors.remove(Material.SPRUCE_DOOR);
				break;
			case "WARPED_DOOR":
				normalDoors.remove(Material.WARPED_DOOR);
				break;
			case "ACACIA_FENCE_GATE":
				gates.remove(Material.ACACIA_FENCE_GATE);
				break;
			case "BAMBOO_FENCE_GATE":
				gates.remove(Material.BAMBOO_FENCE_GATE);
				break;
			case "BIRCH_FENCE_GATE":
				gates.remove(Material.BIRCH_FENCE_GATE);
				break;
			case "CHERRY_FENCE_GATE":
				gates.remove(Material.CHERRY_FENCE_GATE);
				break;
			case "CRIMSON_FENCE_GATE":
				gates.remove(Material.CRIMSON_FENCE_GATE);
				break;
			case "DARK_OAK_FENCE_GATE":
				gates.remove(Material.DARK_OAK_FENCE_GATE);
				break;
			case "JUNGLE_FENCE_GATE":
				gates.remove(Material.JUNGLE_FENCE_GATE);
				break;
			case "OAK_FENCE_GATE":
				gates.remove(Material.OAK_FENCE_GATE);
				break;
			case "SPRUCE_FENCE_GATE":
				gates.remove(Material.SPRUCE_FENCE_GATE);
				break;
			case "WARPED_FENCE_GATE":
				gates.remove(Material.WARPED_FENCE_GATE);
				break;
			case "ACACIA_TRAPDOOR":
				trapdoors.remove(Material.ACACIA_TRAPDOOR);
				break;
			case "BAMBOO_TRAPDOOR":
				trapdoors.remove(Material.BAMBOO_TRAPDOOR);
				break;
			case "BIRCH_TRAPDOOR":
				trapdoors.remove(Material.BIRCH_TRAPDOOR);
				break;
			case "CHERRY_TRAPDOOR":
				trapdoors.remove(Material.CHERRY_TRAPDOOR);
				break;
			case "CRIMSON_TRAPDOOR":
				trapdoors.remove(Material.CRIMSON_TRAPDOOR);
				break;
			case "DARK_OAK_TRAPDOOR":
				trapdoors.remove(Material.DARK_OAK_TRAPDOOR);
				break;
			case "JUNGLE_TRAPDOOR":
				trapdoors.remove(Material.JUNGLE_TRAPDOOR);
				break;
			case "OAK_TRAPDOOR":
				trapdoors.remove(Material.OAK_TRAPDOOR);
				break;
			case "SPRUCE_TRAPDOOR":
				trapdoors.remove(Material.SPRUCE_TRAPDOOR);
				break;
			case "WARPED_TRAPDOOR":
				trapdoors.remove(Material.WARPED_TRAPDOOR);
				break;
			case "IRON_DOOR":
				ironDoorsAllowed = false;
				break;
			case "IRON_TRAPDOOR":
				ironTrapdoorsAllowed = false;
				break;
			case "TRAPPED_CHEST":
				trappedChestsAllowed = false;
				break;
			case "CHEST":
				chestsAllowed = false;
				break;
			case "SHULKER_BOX":
				shulkers.remove(Material.SHULKER_BOX);
			case "BLACK_SHULKER_BOX":
				shulkers.remove(Material.BLACK_SHULKER_BOX);
			case "BLUE_SHULKER_BOX":
				shulkers.remove(Material.BLUE_SHULKER_BOX);
			case "BROWN_SHULKER_BOX":
				shulkers.remove(Material.BROWN_SHULKER_BOX);
			case "CYAN_SHULKER_BOX":
				shulkers.remove(Material.CYAN_SHULKER_BOX);
			case "GRAY_SHULKER_BOX":
				shulkers.remove(Material.GRAY_SHULKER_BOX);
			case "GREEN_SHULKER_BOX":
				shulkers.remove(Material.GREEN_SHULKER_BOX);
			case "LIGHT_BLUE_SHULKER_BOX":
				shulkers.remove(Material.LIGHT_BLUE_SHULKER_BOX);
			case "LIGHT_GRAY_SHULKER_BOX":
				shulkers.remove(Material.LIGHT_GRAY_SHULKER_BOX);
			case "LIME_SHULKER_BOX":
				shulkers.remove(Material.LIME_SHULKER_BOX);
			case "MAGENTA_SHULKER_BOX":
				shulkers.remove(Material.MAGENTA_SHULKER_BOX);
			case "ORANGE_SHULKER_BOX":
				shulkers.remove(Material.ORANGE_SHULKER_BOX);
			case "PINK_SHULKER_BOX":
				shulkers.remove(Material.PINK_SHULKER_BOX);
			case "PURPLE_SHULKER_BOX":
				shulkers.remove(Material.PURPLE_SHULKER_BOX);
			case "RED_SHULKER_BOX":
				shulkers.remove(Material.RED_SHULKER_BOX);
			case "WHITE_SHULKER_BOX":
				shulkers.remove(Material.WHITE_SHULKER_BOX);
			case "YELLOW_SHULKER_BOX":
				shulkers.remove(Material.YELLOW_SHULKER_BOX);
			case "BARREL":
				barrelsAllowed = false;
			default:
				// code block
			}

		}
	}

	public static int getRandom(int chance) {
		return random.nextInt(chance) + 1;
	}

	public static void openDoor(final Block b, final World w, final Player p) {
		if (!b.hasMetadata("door")) {
			b.setMetadata("door", new FixedMetadataValue(Main.getInstance(), "opened"));
			p.getInventory().clear(p.getInventory().getHeldItemSlot());
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
					Door d = (Door) b.getBlockData();
					if (!d.isOpen()) {
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 5F, 1F);
					}
					d.setOpen(true);
					b.setBlockData(d);
					b.removeMetadata("door", Main.getInstance());
					p.getInventory().addItem(CraftingRecipes.getLockPick());
					lockpickMap.put(p.getName(), 0);
				}
			}.runTaskLater(Main.getInstance(), 40);
			if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Door d = (Door) b.getBlockData();
						d.setOpen(false);
						b.setBlockData(d);
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 5F, 1F);
					}
				}.runTaskLater(Main.getInstance(),
						(long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses") * 20) + 40);
			}
		}
	}

	public static void openGate(final Block b, final World w, final Player p) {
		if (!b.hasMetadata("gate")) {
			p.getInventory().clear(p.getInventory().getHeldItemSlot());
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
					Gate g = (Gate) b.getBlockData();
					if (!g.isOpen()) {
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 5F, 1F);
					}
					g.setOpen(true);
					b.setBlockData(g);
					b.removeMetadata("door", Main.getInstance());
					p.getInventory().addItem(CraftingRecipes.getLockPick());
					lockpickMap.put(p.getName(), 0);
				}
			}.runTaskLater(Main.getInstance(), 40);
			if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Gate g = (Gate) b.getBlockData();
						g.setOpen(false);
						b.setBlockData(g);
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_DOOR_CLOSE, 5F, 1F);
					}
				}.runTaskLater(Main.getInstance(),
						(long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses") * 20) + 40);
			}
		}

	}

	public static void openTrapdoor(final Block b, final World w, final Player p) {
		if (!b.hasMetadata("trapdoor")) {
			b.setMetadata("trapdoor", new FixedMetadataValue(Main.getInstance(), "trapdoor"));
			p.getInventory().clear(p.getInventory().getHeldItemSlot());
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
					TrapDoor t = (TrapDoor) b.getBlockData();
					if (!t.isOpen()) {
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_DOOR_OPEN, 5F, 1F);
					}
					t.setOpen(true);
					b.setBlockData(t);
					b.removeMetadata("trapdoor", Main.getInstance());
					p.getInventory().addItem(CraftingRecipes.getLockPick());
					lockpickMap.put(p.getName(), 0);
				}
			}.runTaskLater(Main.getInstance(), 40);
			if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						TrapDoor t = (TrapDoor) b.getBlockData();
						t.setOpen(false);
						b.setBlockData(t);
						w.playSound(b.getLocation(), Sound.BLOCK_WOODEN_TRAPDOOR_CLOSE, 5F, 1F);
					}
				}.runTaskLater(Main.getInstance(),
						(long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses") * 20) + 40);
			}
		}

	}

	public static void openIronDoor(final Block b, final World w, final Player p) {
		if (!b.hasMetadata("door")) {
			b.setMetadata("door", new FixedMetadataValue(Main.getInstance(), "opened"));
			p.getInventory().clear(p.getInventory().getHeldItemSlot());
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
					Door d = (Door) b.getBlockData();
					if (!d.isOpen()) {
						w.playSound(b.getLocation(), Sound.BLOCK_IRON_DOOR_OPEN, 5F, 1F);
					}
					d.setOpen(true);
					b.setBlockData(d);
					b.removeMetadata("door", Main.getInstance());
					p.getInventory().addItem(CraftingRecipes.getLockPick());
					lockpickMap.put(p.getName(), 0);
				}
			}.runTaskLater(Main.getInstance(), 40);
			if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Door d = (Door) b.getBlockData();
						d.setOpen(false);
						b.setBlockData(d);
						w.playSound(b.getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 5F, 1F);
					}
				}.runTaskLater(Main.getInstance(),
						(long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses") * 20) + 40);
			}

		}

	}

	public static void openIronTrapdoor(final Block b, final World w, final Player p) {
		if (!b.hasMetadata("trapdoor")) {
			b.setMetadata("trapdoor", new FixedMetadataValue(Main.getInstance(), "trapdoor"));
			p.getInventory().clear(p.getInventory().getHeldItemSlot());
			new BukkitRunnable() {
				@Override
				public void run() {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
					TrapDoor t = (TrapDoor) b.getBlockData();
					if (!t.isOpen()) {
						w.playSound(b.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 5F, 1F);
					}
					t.setOpen(true);
					b.setBlockData(t);
					b.removeMetadata("trapdoor", Main.getInstance());
					p.getInventory().addItem(CraftingRecipes.getLockPick());
					lockpickMap.put(p.getName(), 0);
				}
			}.runTaskLater(Main.getInstance(), 40);
			if (Main.getInstance().getConfig().getBoolean("ClosesAfterPicked")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						TrapDoor t = (TrapDoor) b.getBlockData();
						t.setOpen(false);
						b.setBlockData(t);
						w.playSound(b.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_CLOSE, 5F, 1F);
					}
				}.runTaskLater(Main.getInstance(),
						(long) (Main.getInstance().getConfig().getInt("SecondsUntilCloses") * 20) + 40);
			}
		}

	}

	public static void openChest(final Block b, final World w, final Player p) {
		b.setMetadata("chest", new FixedMetadataValue(Main.getInstance(), "chest"));
		p.getInventory().clear(p.getInventory().getHeldItemSlot());
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
				Chest chest = (Chest) b.getState();
				p.openInventory(chest.getInventory());
				w.playSound(b.getLocation(), Sound.BLOCK_CHEST_OPEN, 5F, 1F);
				p.getInventory().addItem(CraftingRecipes.getLockPick());
				lockpickMap.put(p.getName(), 0);
			}
		}.runTaskLater(Main.getInstance(), 40);
	}

	public static void openShulker(final Block b, final World w, final Player p) {
		b.setMetadata("shulker", new FixedMetadataValue(Main.getInstance(), "shulker"));
		p.getInventory().clear(p.getInventory().getHeldItemSlot());
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
				ShulkerBox shulkerbox = (ShulkerBox) b.getState();
				p.openInventory(shulkerbox.getInventory());
				w.playSound(b.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, 5F, 1F);
				p.getInventory().addItem(CraftingRecipes.getLockPick());
				lockpickMap.put(p.getName(), 0);
			}
		}.runTaskLater(Main.getInstance(), 40);
	}
	
	public static void openBarrel(final Block b, final World w, final Player p) {
		b.setMetadata("barrel", new FixedMetadataValue(Main.getInstance(), "barrel"));
		p.getInventory().clear(p.getInventory().getHeldItemSlot());
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8[&eSL&8] &a" + lang.getString("Lockpicks.Success")));
				Barrel barrel = (Barrel) b.getState();
				p.openInventory(barrel.getInventory());
				w.playSound(b.getLocation(), Sound.BLOCK_BARREL_OPEN, 5F, 1F);
				p.getInventory().addItem(CraftingRecipes.getLockPick());
				lockpickMap.put(p.getName(), 0);
			}
		}.runTaskLater(Main.getInstance(), 40);
	}

	public static void pickFailed(final Player p) {
		p.getInventory().clear(p.getInventory().getHeldItemSlot());
		new BukkitRunnable() {
			@Override
			public void run() {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						"&8[&eSL&8] &c" + lang.getString("Lockpicks.Fail1")));
				p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1.0F, 0.1F);
				if (p.getHealth() > 1.0D) {
					p.setHealth(p.getHealth() - 1.0D);
				} else {
					p.setHealth(0.0D);
				}
				p.getLocation().getWorld().playEffect(p.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
				lockpickMap.put(p.getName(), 0);
			}
		}.runTaskLater(Main.getInstance(), 40);
	}

	public static boolean checkLockpickMap(Player p) {
		if (!lockpickMap.containsKey(p.getName())) {
			lockpickMap.put(p.getName(), 0);
		} else {
			if (lockpickMap.get(p.getName()) == 1) {
				p.sendMessage(mess("&8[&eSL&8] &c" + lang.getString("Lockpicks.Fail2")));
				return true;
			}
		}
		lockpickMap.put(p.getName(), 1);
		return false;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Action action = e.getAction();
		if (action == Action.LEFT_CLICK_BLOCK && p.isSneaking()
				&& p.getInventory().getItemInMainHand().getType() == Material.IRON_HOE
				&& p.getInventory().getItemInMainHand().getItemMeta().hasLore()
				&& p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()
						.equalsIgnoreCase(mess(Main.getInstance().getConfig().getString("Lockpicks.DisplayName")))) {
			if (p.getGameMode() == GameMode.CREATIVE) {
				e.setCancelled(true);
			}
			Block block = e.getClickedBlock();
			World w = p.getWorld();
			if (!p.hasPermission("simplelockpicking.use")) {
				p.sendMessage(mess("&8[&eSL&8] &c" + lang.getString("NoPermission")));
				return;
			}
			if (shulkers.contains(block.getType())) {
				if (pm.getPlugin("Towny") != null) {
					TownyCompatibility.TownyShulker(p, block, w);
					return;
				} else {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.ShulkerBox")) == 1) {
						openShulker(block, w, p);
					} else {
						pickFailed(p);
					}
					return;
				}
			} else if (block.getType() == Material.BARREL && barrelsAllowed) {
				if (pm.getPlugin("Towny") != null) {
					TownyCompatibility.TownyBarrel(p, block, w);
					return;
				} else {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Barrel")) == 1) {
						openBarrel(block, w, p);
					} else {
						pickFailed(p);
					}
					return;
				}
			} else if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
				if ((block.getType() == Material.CHEST && !chestsAllowed)
						|| (block.getType() == Material.TRAPPED_CHEST && !trappedChestsAllowed)) {
					return;
				}
				if (pm.getPlugin("Towny") != null) {
					TownyCompatibility.TownyChest(p, block, w);
					return;
				} else {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Chests")) == 1) {
						openChest(block, w, p);
					} else {
						pickFailed(p);
					}
					return;
				}
			} else if (normalDoors.contains(block.getType())) {
				Door d = (Door) block.getBlockData();
				if (!d.isOpen()) {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Wood")) == 1) {
						openDoor(block, w, p);
					} else {
						pickFailed(p);
					}
				}
			} else if (gates.contains(block.getType())) {
				Gate g = (Gate) block.getBlockData();
				if (!g.isOpen()) {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Wood")) == 1) {
						openGate(block, w, p);
					} else {
						pickFailed(p);
					}
				}
			} else if (block.getType() == Material.IRON_DOOR && ironDoorsAllowed) {
				Door d = (Door) block.getBlockData();
				if (!d.isOpen()) {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Iron")) == 1) {
						openIronDoor(block, w, p);
					} else {
						pickFailed(p);
					}
				}
			} else if (trapdoors.contains(block.getType())) {
				TrapDoor t = (TrapDoor) block.getBlockData();
				if (!t.isOpen()) {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Wood")) == 1) {
						openTrapdoor(block, w, p);
					} else {
						pickFailed(p);
					}
				}
			} else if (block.getType() == Material.IRON_TRAPDOOR && ironTrapdoorsAllowed) {
				TrapDoor t = (TrapDoor) block.getBlockData();
				if (!t.isOpen()) {
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Chances.Iron")) == 1) {
						openTrapdoor(block, w, p);
					} else {
						pickFailed(p);
					}
				}
			} else if (Main.usingCraftBook) {
				// if its a gate block
				if (CraftBookCompatibility.isGateBlock(block)) {
					// if gate lockpicking is disabled
					if (!Main.getInstance().getConfig().getBoolean("Gate.Enabled")) {
						return;
					}
				} else {
					return;
				}

				// null if gate sign is found
				Block gateSign = CraftBookCompatibility.reverseGateCheck(block, p);
				if (gateSign != null) {
					// check if already lockpicking something
					if (checkLockpickMap(p)) {
						return;
					}
					p.sendMessage(mess("&8[&eSL&8] &7" + lang.getString("Lockpicks.Use")));
					if (getRandom(Main.getInstance().getConfig().getInt("Gate.Chance")) == 1) {
						// open/close gate
						CraftBookCompatibility.toggleGate(p, gateSign);
					} else {
						pickFailed(p);
					}
				}
			}
		}
	}
}