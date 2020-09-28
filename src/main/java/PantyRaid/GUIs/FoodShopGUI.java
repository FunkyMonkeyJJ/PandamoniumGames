package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FoodShopGUI {
    public FoodShopGUI(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Inventory gui = Bukkit.createInventory(player, 27,
                gamePlayer.getTeamColor().getTextColor() + "Food Shop");

        ItemStack air = new NamedItem(Material.GRAY_STAINED_GLASS_PANE,
                " ").getItemStack();

        ItemStack chicken = new ItemStack(Material.COOKED_CHICKEN, 4);
        ItemMeta chickenMeta = chicken.getItemMeta();
        chickenMeta.setDisplayName(ChatColor.YELLOW + "Chimken");
        chickenMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "4 Iron Ingots"));
        chicken.setItemMeta(chickenMeta);

        ItemStack pork = new ItemStack(Material.COOKED_PORKCHOP, 4);
        ItemMeta porkMeta = pork.getItemMeta();
        porkMeta.setDisplayName(ChatColor.YELLOW + "Pork");
        porkMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "4 Iron Ingots"));
        pork.setItemMeta(porkMeta);

        ItemStack beef = new ItemStack(Material.COOKED_BEEF, 4);
        ItemMeta beefMeta = beef.getItemMeta();
        beefMeta.setDisplayName(ChatColor.YELLOW + "Steak");
        beefMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "4 Iron Ingots"));
        beef.setItemMeta(beefMeta);

        ItemStack carrot = new ItemStack(Material.GOLDEN_CARROT, 6);
        ItemMeta carrotMeta = carrot.getItemMeta();
        carrotMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString()
                + "Golden Carrot");
        carrotMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "1 Gold Ingot", ChatColor.GREEN +
                "Eating this gives you brief night vision."));
        carrot.setItemMeta(carrotMeta);

        ItemStack cake = new ItemStack(Material.CAKE);
        ItemMeta cakeMeta = cake.getItemMeta();
        cakeMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString()
                + "Cake");
        cakeMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "3 Gold Ingots", ChatColor.GREEN +
                "Eating this gives you brief regeneration."));
        cake.setItemMeta(cakeMeta);

        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 4);
        ItemMeta appleMeta = apple.getItemMeta();
        appleMeta.setDisplayName(ChatColor.GOLD + ChatColor.BOLD.toString()
                + "Gapple");
        appleMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "5 Gold Ingots"));
        apple.setItemMeta(appleMeta);

        gui.setItem(0, new NamedItem(Material.BARRIER,
                gamePlayer.getTeamColor().getTextColor() +
                        "Go Back To Shop Selection").getItemStack());
        gui.setItem(10, chicken);
        gui.setItem(11, pork);
        gui.setItem(12, beef);
        gui.setItem(14, carrot);
        gui.setItem(15, cake);
        gui.setItem(16, apple);

        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, air);
            }
        }

        player.openInventory(gui);
    }
}
