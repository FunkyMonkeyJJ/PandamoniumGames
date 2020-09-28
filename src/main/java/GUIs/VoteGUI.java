package GUIs;

import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VoteGUI {
    private final Inventory gui;

    public VoteGUI(Player player) {
        gui = Bukkit.createInventory(player, 9,
                ChatColor.GOLD + "Vote GUI");
        reload();
        player.openInventory(gui);
    }

    void reload() {
        ItemStack air = new NamedItem(Material.GRAY_STAINED_GLASS_PANE,
                " ").getItemStack();

        gui.setItem(1, new NamedItem(Material.ARMOR_STAND,
                ChatColor.DARK_AQUA + "Vote on the Game!").
                getItemStack());
        gui.setItem(4, new NamedItem(Material.ZOMBIE_HEAD,
                ChatColor.DARK_AQUA + "Vote on the GameMode!").
                getItemStack());
        gui.setItem(7, new NamedItem(Material.MAP,
                ChatColor.DARK_AQUA + "Vote on the Map!").
                getItemStack());

        for (int i = 0; i < gui.getSize(); i++)
            if (gui.getItem(i) == null) gui.setItem(i, air);
    }
}
