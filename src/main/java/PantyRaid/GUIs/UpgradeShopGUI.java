package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import Team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.List;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public class UpgradeShopGUI {
    private final GamePlayer gamePlayer;
    private final Inventory gui;

    public UpgradeShopGUI(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        Player player = gamePlayer.getPlayer();
        this.gui = Bukkit.createInventory(player, 45,
                gamePlayer.getTeamColor().getTextColor() +
                        "Team Upgrade Shop");
        reload();
        player.openInventory(gui);
    }

    void reload() {
        NamedItem air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ");
        ChatColor teamColor = gamePlayer.getTeamColor().getTextColor();

        ItemStack respawnUpgrade1 = new ItemStack(Material.CLOCK);
        ItemMeta respawnUpgrade1Meta = respawnUpgrade1.getItemMeta();
        respawnUpgrade1Meta.setDisplayName(teamColor + "Respawn Upgrade I");
        respawnUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "10 Iron Ingots",
                GREEN + "Lowers your teams respawn time to 12 Seconds."));
        respawnUpgrade1.setItemMeta(respawnUpgrade1Meta);

        ItemStack respawnUpgrade2 = new ItemStack(Material.CLOCK, 2);
        ItemMeta respawnUpgrade2Meta = respawnUpgrade2.getItemMeta();
        respawnUpgrade2Meta.setDisplayName(teamColor + "Respawn Upgrade II");
        respawnUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "10 Gold Ingots",
                GREEN + "Lowers your teams respawn time to 8 Seconds."));
        respawnUpgrade2.setItemMeta(respawnUpgrade2Meta);

        ItemStack respawnUpgrade3 = new ItemStack(Material.CLOCK, 3);
        ItemMeta respawnUpgrade3Meta = respawnUpgrade3.getItemMeta();
        respawnUpgrade3Meta.setDisplayName(teamColor + "Respawn Upgrade III");
        respawnUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "5 Diamonds",
                GREEN + "Lowers your teams respawn time to 5 Seconds."));
        respawnUpgrade3.setItemMeta(respawnUpgrade3Meta);

        ItemStack regenUpgrade1 = new ItemStack(Material.LINGERING_POTION);
        PotionMeta regenUpgrade1Meta = (PotionMeta)
                regenUpgrade1.getItemMeta();
        regenUpgrade1Meta.setColor(Color.FUCHSIA);
        regenUpgrade1Meta.setDisplayName(teamColor +
                "Regeneration Upgrade I");
        regenUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "16 Gold Ingots",
                GREEN + "Gives you constant Regeneration I while ",
                GREEN + "you're near your team's Armor Stand."));
        regenUpgrade1.setItemMeta(regenUpgrade1Meta);

        ItemStack regenUpgrade2 = new ItemStack(
                Material.LINGERING_POTION, 2);
        PotionMeta regenUpgrade2Meta = (PotionMeta)
                regenUpgrade2.getItemMeta();
        regenUpgrade2Meta.setColor(Color.FUCHSIA);
        regenUpgrade2Meta.setDisplayName(teamColor +
                "Regeneration Upgrade II");
        regenUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "10 Diamonds",
                GREEN + "Gives you constant Regeneration II while ",
                GREEN + "you're near your team's Armor Stand."));
        regenUpgrade2.setItemMeta(regenUpgrade2Meta);

        ItemStack regenUpgrade3 = new ItemStack(
                Material.LINGERING_POTION, 3);
        PotionMeta regenUpgrade3Meta = (PotionMeta)
                regenUpgrade3.getItemMeta();
        regenUpgrade3Meta.setColor(Color.FUCHSIA);
        regenUpgrade3Meta.setDisplayName(teamColor +
                "Regeneration Upgrade III");
        regenUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "10 Netherite Ingots",
                GREEN + "Gives you constant Regeneration III while ",
                GREEN + "you're near your team's Armor Stand."));
        regenUpgrade3.setItemMeta(regenUpgrade3Meta);

        ItemStack armorWeightUpgrade1 = new ItemStack(Material.ANVIL);
        ItemMeta armorWeightUpgrade1Meta = armorWeightUpgrade1.getItemMeta();
        armorWeightUpgrade1Meta.setDisplayName(teamColor +
                "Armor Weight Upgrade I");
        armorWeightUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "16 Iron Ingots",
                GREEN + "Upgrades your team's flag(s) on your ",
                GREEN + "Armor Stand to Chainmail Armor to slow down ",
                GREEN + "whoever tries to steal them."));
        armorWeightUpgrade1.setItemMeta(armorWeightUpgrade1Meta);

        ItemStack armorWeightUpgrade2 = new ItemStack(
                Material.ANVIL, 2);
        ItemMeta armorWeightUpgrade2Meta = armorWeightUpgrade2.getItemMeta();
        armorWeightUpgrade2Meta.setDisplayName(teamColor +
                "Armor Weight Upgrade II");
        armorWeightUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "32 Iron Ingots",
                GREEN + "Upgrades your team's flag(s) on your ",
                GREEN + "Armor Stand to Iron Armor to slow down ",
                GREEN + "whoever tries to steal them."));
        armorWeightUpgrade2.setItemMeta(armorWeightUpgrade2Meta);

        ItemStack armorWeightUpgrade3 = new ItemStack(
                Material.ANVIL, 3);
        ItemMeta armorWeightUpgrade3Meta = armorWeightUpgrade3.getItemMeta();
        armorWeightUpgrade3Meta.setDisplayName(teamColor +
                "Armor Weight Upgrade III");
        armorWeightUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "32 Gold Ingots",
                GREEN + "Upgrades your team's flag(s) on your ",
                GREEN + "Armor Stand to Gold Armor to slow down ",
                GREEN + "whoever tries to steal them."));
        armorWeightUpgrade3.setItemMeta(armorWeightUpgrade3Meta);

        ItemStack pressureUpgrade1 = new ItemStack(Material.OBSIDIAN);
        ItemMeta pressureUpgrade1Meta = pressureUpgrade1.getItemMeta();
        pressureUpgrade1Meta.setDisplayName(teamColor + "Pressure Upgrade I");
        pressureUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "3 Gold Ingots",
                GREEN + "The enemy will be inflicted with Weakness I ",
                GREEN + "as they feel the pressure of stealing your ",
                GREEN + "team's flag."));
        pressureUpgrade1.setItemMeta(pressureUpgrade1Meta);

        ItemStack pressureUpgrade2 = new ItemStack(
                Material.OBSIDIAN, 2);
        ItemMeta pressureUpgrade2Meta = pressureUpgrade2.getItemMeta();
        pressureUpgrade2Meta.setDisplayName(teamColor +
                "Pressure Upgrade II");
        pressureUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "3 Diamonds",
                GREEN + "The enemy will be inflicted with Weakness II ",
                GREEN + "as they feel the pressure of stealing your ",
                GREEN + "team's flag."));
        pressureUpgrade2.setItemMeta(pressureUpgrade2Meta);

        ItemStack pressureUpgrade3 = new ItemStack(
                Material.OBSIDIAN, 3);
        ItemMeta pressureUpgrade3Meta = pressureUpgrade3.getItemMeta();
        pressureUpgrade3Meta.setDisplayName(teamColor +
                "Pressure Upgrade III");
        pressureUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "3 Netherite Ingots",
                GREEN + "The enemy will be inflicted with Weakness III ",
                GREEN + "as they feel the pressure of stealing your ",
                GREEN + "team's flag."));
        pressureUpgrade3.setItemMeta(pressureUpgrade3Meta);

        ItemStack killRewardsUpgrade1 = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta killRewardsUpgrade1Meta = killRewardsUpgrade1.getItemMeta();
        killRewardsUpgrade1Meta.setDisplayName(teamColor +
                "Kill Rewards Upgrade I");
        killRewardsUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "5 Gold Ingots",
                GREEN + "Gives you Speed I whenever you get a kill."));
        killRewardsUpgrade1.setItemMeta(killRewardsUpgrade1Meta);

        ItemStack killRewardsUpgrade2 = new ItemStack(
                Material.DIAMOND_SWORD, 2);
        ItemMeta killRewardsUpgrade2Meta = killRewardsUpgrade2.getItemMeta();
        killRewardsUpgrade2Meta.setDisplayName(teamColor +
                "Kill Rewards Upgrade II");
        killRewardsUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "4 Diamonds",
                GREEN + "Gives you Strength I whenever you get a kill."));
        killRewardsUpgrade2.setItemMeta(killRewardsUpgrade2Meta);

        ItemStack killRewardsUpgrade3 = new ItemStack(
                Material.DIAMOND_SWORD, 3);
        ItemMeta killRewardsUpgrade3Meta = killRewardsUpgrade3.getItemMeta();
        killRewardsUpgrade3Meta.setDisplayName(teamColor +
                "Kill Rewards Upgrade III");
        killRewardsUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "3 Netherite Ingots",
                GREEN + "Gives you Regeneration I whenever you ",
                GREEN + "get a kill."));
        killRewardsUpgrade3.setItemMeta(killRewardsUpgrade3Meta);

        ItemStack runForItUpgrade1 = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta runForItUpgrade1Meta = runForItUpgrade1.getItemMeta();
        runForItUpgrade1Meta.setDisplayName(teamColor +
                "Run For It! Upgrade I");
        runForItUpgrade1Meta.setLore(General.Game.loreMaker(
                GOLD + "5 Gold Ingots",
                GREEN + "Gives you or your teammates Speed I to ",
                GREEN + "quickly take every flag that your team ",
                GREEN + "steals back to your base."));
        runForItUpgrade1.setItemMeta(runForItUpgrade1Meta);

        ItemStack runForItUpgrade2 = new ItemStack(
                Material.CHAINMAIL_BOOTS, 2);
        ItemMeta runForItUpgrade2Meta = runForItUpgrade2.getItemMeta();
        runForItUpgrade2Meta.setDisplayName(teamColor +
                "Run For It! Upgrade II");
        runForItUpgrade2Meta.setLore(General.Game.loreMaker(
                GOLD + "4 Diamonds",
                GREEN + "Gives you or your teammates Speed II to ",
                GREEN + "quickly take every flag that your team ",
                GREEN + "steals back to your base."));
        runForItUpgrade2.setItemMeta(runForItUpgrade2Meta);


        ItemStack runForItUpgrade3 = new ItemStack(
                Material.CHAINMAIL_BOOTS, 3);
        ItemMeta runForItUpgrade3Meta = runForItUpgrade3.getItemMeta();
        runForItUpgrade3Meta.setDisplayName(teamColor +
                "Run For It! Upgrade III");
        runForItUpgrade3Meta.setLore(General.Game.loreMaker(
                GOLD + "3 Netherite Ingots",
                GREEN + "Gives you or your teammates Speed III to ",
                GREEN + "quickly take every flag that your team ",
                GREEN + "steals back to your base."));
        runForItUpgrade3.setItemMeta(runForItUpgrade3Meta);

        ItemStack[] items = new ItemStack[]{
                respawnUpgrade1, respawnUpgrade2, respawnUpgrade3,
                regenUpgrade1, regenUpgrade2, regenUpgrade3,
                armorWeightUpgrade1, armorWeightUpgrade2, armorWeightUpgrade3,
                pressureUpgrade1, pressureUpgrade2, pressureUpgrade3,
                killRewardsUpgrade1, killRewardsUpgrade2, killRewardsUpgrade3,
                runForItUpgrade1, runForItUpgrade2, runForItUpgrade3};
        gui.setItem(0, new NamedItem(Material.BARRIER,
                gamePlayer.getTeamColor().getTextColor() +
                        "Go Back To Shop Selection").getItemStack());

        int i = 10;
        int skipColumn = 0;
        boolean skipRow = true;
        for (ItemStack item : items) {
            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            Team team = gamePlayer.getTeam();
            boolean removeLore = false;
            if (lore.size() > 1) {
                if (i <= 12) {
                    if (team.getMaxRespawnUpgrade() >= item.getAmount())
                        removeLore = true;
                } else if (i <= 16) {
                    if (team.getMaxRegenerationUpgrade() >= item.getAmount())
                        removeLore = true;
                } else if (i <= 21) {
                    if (team.getMaxArmorWeightUpgrade() >= item.getAmount())
                        removeLore = true;
                } else if (i <= 25) {
                    if (team.getMaxPressureUpgrade() >= item.getAmount())
                        removeLore = true;
                } else if (i <= 30) {
                    if (team.getMaxKillsRewardsUpgrade() >= item.getAmount())
                        removeLore = true;
                } else if (i <= 34) {
                    if (team.getMaxRunForItUpgrade() >= item.getAmount())
                        removeLore = true;
                }
                if (removeLore) {
                    lore.remove(0);
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
            }
            gui.setItem(i++, item);
            if (++skipColumn == 3) {
                skipColumn = 0;
                skipRow = !skipRow;
                if (skipRow) i += 2;
                else i++;
            }
        }

        for (i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, air.getItemStack());
            }
        }
    }
}
