package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ShopGUI {
    public ShopGUI(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Inventory gui = Bukkit.createInventory(player, 27,
                gamePlayer.getTeamColor().getTextColor() +
                        "Shop Selection");
        ItemStack air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ").getItemStack();

        gui.setItem(10, new NamedItem(Material.IRON_HELMET,
                ChatColor.GOLD + "Armor Shop").getItemStack());
        gui.setItem(11, new NamedItem(Material.DIAMOND_SWORD,
                ChatColor.GOLD + "Tool / Weapon Shop").getItemStack());
        gui.setItem(12, new NamedItem(Material.BEACON,
                ChatColor.GOLD + "Team Upgrade Shop").getItemStack());

        gui.setItem(14, new NamedItem(Material.COOKED_BEEF,
                ChatColor.GOLD + "Food Shop").getItemStack());
        gui.setItem(15, new NamedItem(Material.COBBLESTONE,
                ChatColor.GOLD + "Block Shop").getItemStack());
        gui.setItem(16, new NamedItem(Material.TURTLE_HELMET,
                ChatColor.GOLD + "Item Shop").getItemStack());

        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, air);
            }
        }

        player.openInventory(gui);
    }
}
