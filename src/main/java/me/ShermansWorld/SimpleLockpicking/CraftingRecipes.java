package me.ShermansWorld.SimpleLockpicking;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

//import org.bukkit.inventory.FurnaceRecipe;
//import org.bukkit.inventory.ItemStack;


public class CraftingRecipes implements Listener {
	
	public String msg(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}
	
	public static ItemStack getLockPick() {
		ItemStack lockpick = new ItemStack(Material.IRON_HOE, 1);
	    ItemMeta meta = lockpick.getItemMeta();
	    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',Main.getInstance().getConfig().getString("Lockpicks.DisplayName")));
	    meta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
	    meta.setCustomModelData(14100);
	    ArrayList<String> lore = new ArrayList<String>();
	    List<String> loreConfigList = Main.getInstance().getConfig().getStringList("Lockpicks.Lore");
	    for (int i = 0; i < loreConfigList.size(); i++) {
	    	lore.add(ChatColor.translateAlternateColorCodes('&',loreConfigList.get(i)));
	    }
	    meta.setLore(lore);
	    lockpick.setItemMeta(meta);
	    return (lockpick);
	}
	
	
	public void lockpickRecipe() {
		ItemStack lockpick = getLockPick();
	    NamespacedKey key = new NamespacedKey(Main.getInstance(), Main.getInstance().getDescription().getName() + "3");
	    ShapedRecipe lockpickRecipe = new ShapedRecipe(key, lockpick);
	    lockpickRecipe.shape("@@ ", " % ", "%  ");
	    lockpickRecipe.setIngredient('@', Material.IRON_INGOT);
	    lockpickRecipe.setIngredient('%', Material.STICK);
	    Main.getInstance().getServer().addRecipe(lockpickRecipe);
	}
    
	
	@EventHandler
	public void CraftListener(CraftItemEvent e) {

		if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Lockpicks.DisplayName")))) {
			if (e.getWhoClicked() instanceof Player) {
				Player p = (Player) e.getWhoClicked();
				if (!p.hasPermission("simplelockpicking.craft")) {
					e.setCancelled(true);
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[SL] You don't have permission to craft lockpicks!"));
				}
			}

		}
	}

}
	
	
	//FurnaceRecipe breadRecipe = new FurnaceRecipe(bakersBread, Material.WHEAT);
