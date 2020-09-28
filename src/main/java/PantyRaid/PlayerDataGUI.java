package PantyRaid;

import General.GamePlayer;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerDataGUI {
    public PlayerDataGUI(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        PlayerData playerData = gamePlayer.getPlayerData();
        Inventory gui = Bukkit.createInventory(player, 54,
                gamePlayer.getTeamColor().getTextColor() +
                        "PantyRaid Player Data");
        ItemStack air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ").getItemStack();

        gui.setItem(9, new NamedItem(Material.PAPER,
                ChatColor.GOLD + "Total Games Played: " +
                        playerData.gamesPlayed).getItemStack());
        gui.setItem(10, new NamedItem(Material.DIAMOND,
                ChatColor.GOLD + "Total Wins: " + playerData.wins).
                getItemStack());
        gui.setItem(11, new NamedItem(Material.COAL,
                ChatColor.GOLD + "Total Losses: " +
                        playerData.losses).getItemStack());
        gui.setItem(12, new NamedItem(Material.EMERALD,
                ChatColor.GOLD + "Win/Loss Ratio: " +
                        playerData.getWinLossRatio()).getItemStack());

        gui.setItem(14, new NamedItem(Material.IRON_SWORD,
                ChatColor.GOLD + "Kills: " + playerData.kills).
                getItemStack());
        gui.setItem(15, new NamedItem(Material.NETHERITE_SWORD,
                ChatColor.GOLD + "Finals Kills: " +
                        playerData.finalKills).getItemStack());
        gui.setItem(16, new NamedItem(Material.SKELETON_SKULL,
                ChatColor.GOLD + "Deaths: " + playerData.deaths).
                getItemStack());
        gui.setItem(17, new NamedItem(Material.DIAMOND_AXE,
                ChatColor.GOLD + "Kill/Death Ratio: " +
                        playerData.getKillDeathRatio()).getItemStack());

        gui.setItem(18, new NamedItem(Material.RED_BANNER,
                ChatColor.GOLD + "Number of Flags Stolen: " +
                        playerData.flagsStolen).getItemStack());
        gui.setItem(19, new NamedItem(gamePlayer.getTeamColor().
                getBannerMaterial(), ChatColor.GOLD + "Number of Your " +
                "Team's Flags Stolen: " + playerData.yourFlagsStolen).
                getItemStack());
        gui.setItem(20, new NamedItem(Material.NETHER_STAR,
                ChatColor.GOLD + "Number of Nether Stars Cashed in: " +
                        playerData.netherStarCashedIn).getItemStack());
        gui.setItem(21, new NamedItem(Material.NAME_TAG,
                ChatColor.GOLD + "Number of Purchases Made: " +
                        playerData.purchasesMade).getItemStack());

        gui.setItem(23, new NamedItem(Material.STONE_SWORD,
                ChatColor.GOLD + "Record Kills in One Game: " +
                        playerData.recordKills).getItemStack());
        gui.setItem(24, new NamedItem(Material.DIAMOND_SWORD,
                ChatColor.GOLD + "Record Final Kills in One Game: " +
                        playerData.finalKills).getItemStack());
        gui.setItem(25, new NamedItem(Material.SKELETON_SKULL,
                ChatColor.GOLD + "Record Deaths in One Game: " +
                        playerData.recordDeaths).getItemStack());
        gui.setItem(26, new NamedItem(Material.NETHERITE_AXE,
                ChatColor.GOLD + "Record Kill/Death Ratio in One " +
                        "Game: " + playerData.getRecordKillDeathRatio()).
                getItemStack());

        gui.setItem(27, new NamedItem(Material.TNT,
                ChatColor.GOLD + "Teams Eliminated: " +
                        playerData.teamsEliminated).getItemStack());

        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, air);
            }
        }

        player.openInventory(gui);
    }
}
