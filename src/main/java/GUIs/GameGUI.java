package GUIs;

import General.Game;
import General.GamePlayer;
import General.MiniGame;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GameGUI {
    private final GamePlayer gamePlayer;
    private final Inventory gui;

    public GameGUI(Player player) {
        gui = Bukkit.createInventory(player, 9,
                ChatColor.GOLD + "Game GUI");
        gamePlayer = Game.getGamePlayer(player);
        reload();
        player.openInventory(gui);
    }

    void reload() {
        ItemStack air = new NamedItem(Material.GRAY_STAINED_GLASS_PANE,
                " ").getItemStack();

        for (int i = 0; i < MiniGame.values().length; i++) {
            MiniGame currentMiniGame = Game.minigame;
            MiniGame miniGame = MiniGame.values()[i];
            ItemStack gameItem = new ItemStack(miniGame.getMinigameMaterial());
            ItemMeta gameMeta = gameItem.getItemMeta();
            gameMeta.setDisplayName(miniGame.getDisplayName());
            if (currentMiniGame == miniGame) gameMeta.addEnchant(
                    Enchantment.MENDING, 1, false);
            gameMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            gameItem.setItemMeta(gameMeta);
            gui.setItem(i + 1, gameItem);
        }

        for (int i = 0; i < gui.getSize(); i++)
            if (gui.getItem(i) == null) gui.setItem(i, air);
    }
}
