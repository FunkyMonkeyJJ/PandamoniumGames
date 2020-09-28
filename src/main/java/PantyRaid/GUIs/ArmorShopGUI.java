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
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

import static org.bukkit.ChatColor.*;

public class ArmorShopGUI {
    private final GamePlayer gamePlayer;
    private final Inventory gui;

    public ArmorShopGUI(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        Player player = gamePlayer.getPlayer();
        this.gui = Bukkit.createInventory(player, 54,
                gamePlayer.getTeamColor().getTextColor() + "Armor Shop");
        reload();
        player.openInventory(gui);
    }

    void reload() {
        NamedItem air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ");
        ChatColor teamColor = gamePlayer.getTeamColor().getTextColor();

        ItemStack leatherHelmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta leatherHelmetMeta = (LeatherArmorMeta)
                leatherHelmet.getItemMeta();
        leatherHelmetMeta.setColor(gamePlayer.getTeamColor().getArmorColor());
        leatherHelmetMeta.setDisplayName(teamColor + "Leather Helmet");
        leatherHelmetMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Leather Helmet."));
        leatherHelmet.setItemMeta(leatherHelmetMeta);

        ItemStack leatherChestplate = new ItemStack(
                Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherChestplateMeta = (LeatherArmorMeta)
                leatherChestplate.getItemMeta();
        leatherChestplateMeta.setColor(
                gamePlayer.getTeamColor().getArmorColor());
        leatherChestplateMeta.setDisplayName(
                teamColor + "Leather Chestplate");
        leatherChestplateMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Leather Chestplate."));
        leatherChestplate.setItemMeta(leatherChestplateMeta);

        ItemStack leatherLeggings = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLeggingsMeta = (LeatherArmorMeta)
                leatherLeggings.getItemMeta();
        leatherLeggingsMeta.setColor(
                gamePlayer.getTeamColor().getArmorColor());
        leatherLeggingsMeta.setDisplayName(teamColor + "Leather Leggings");
        leatherLeggingsMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Leather Leggings."));
        leatherLeggings.setItemMeta(leatherLeggingsMeta);

        ItemStack leatherBoots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootsMeta = (LeatherArmorMeta)
                leatherBoots.getItemMeta();
        leatherBootsMeta.setColor(gamePlayer.getTeamColor().getArmorColor());
        leatherBootsMeta.setDisplayName(teamColor + "Leather Boots");
        leatherBootsMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Leather Boots."));
        leatherBoots.setItemMeta(leatherBootsMeta);

        ItemStack leatherArmor = new ItemStack(Material.LEATHER);
        ItemMeta leatherArmorMeta = leatherArmor.getItemMeta();
        leatherArmorMeta.setDisplayName(teamColor + "Leather Armor");
        leatherArmorMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip Leather Armor."));
        leatherArmor.setItemMeta(leatherArmorMeta);

        ItemStack chainHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
        ItemMeta chainHelmetMeta = chainHelmet.getItemMeta();
        chainHelmetMeta.setDisplayName(teamColor + "Chainmail Helmet");
        chainHelmetMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Iron Ingots", GREEN +
                "This will buy / equip a Chainmail Helmet."));
        chainHelmet.setItemMeta(chainHelmetMeta);

        ItemStack chainChestplate = new ItemStack(
                Material.CHAINMAIL_CHESTPLATE);
        ItemMeta chainChestplateMeta = chainChestplate.getItemMeta();
        chainChestplateMeta.setDisplayName(teamColor +
                "Chainmail Chestplate");
        chainChestplateMeta.setLore(General.Game.loreMaker(GOLD +
                "6 Iron Ingots", GREEN +
                "This will buy / equip a Chainmail Chestplate."));
        chainChestplate.setItemMeta(chainChestplateMeta);

        ItemStack chainLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        ItemMeta chainLeggingsMeta = chainLeggings.getItemMeta();
        chainLeggingsMeta.setDisplayName(teamColor + "Chainmail Leggings");
        chainLeggingsMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Iron Ingots", GREEN +
                "This will buy / equip Chainmail Leggings."));
        chainLeggings.setItemMeta(chainLeggingsMeta);

        ItemStack chainBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        ItemMeta chainBootsMeta = chainBoots.getItemMeta();
        chainBootsMeta.setDisplayName(teamColor + "Chainmail Chestplate");
        chainBootsMeta.setLore(General.Game.loreMaker(GOLD +
                "2 Iron Ingots", GREEN +
                "This will buy / equip Chainmail Boots."));
        chainBoots.setItemMeta(chainBootsMeta);

        ItemStack chainArmor = new ItemStack(Material.CHAIN);
        ItemMeta chainArmorMeta = chainArmor.getItemMeta();
        chainArmorMeta.setDisplayName(teamColor + "Chainmail Armor");
        chainArmorMeta.setLore(General.Game.loreMaker(GOLD +
                "16 Iron Ingots", GREEN +
                "This will buy / equip all Chainmail Armor."));
        chainArmor.setItemMeta(chainArmorMeta);

        ItemStack ironHelmet = new ItemStack(Material.IRON_HELMET);
        ItemMeta ironHelmetMeta = ironHelmet.getItemMeta();
        ironHelmetMeta.setDisplayName(teamColor + "Iron Helmet");
        ironHelmetMeta.setLore(General.Game.loreMaker(GOLD +
                "6 Iron Ingots", GREEN +
                "This will buy / equip an Iron Helmet."));
        ironHelmet.setItemMeta(ironHelmetMeta);

        ItemStack ironChestplate = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta ironChestplateMeta = ironChestplate.getItemMeta();
        ironChestplateMeta.setDisplayName(teamColor + "Iron Chestplate");
        ironChestplateMeta.setLore(General.Game.loreMaker(GOLD +
                "12 Iron Ingots", GREEN +
                "This will buy / equip an Iron Chestplate."));
        ironChestplate.setItemMeta(ironChestplateMeta);

        ItemStack ironLeggings = new ItemStack(Material.IRON_LEGGINGS);
        ItemMeta ironLeggingsMeta = ironLeggings.getItemMeta();
        ironLeggingsMeta.setDisplayName(teamColor + "Iron Leggings");
        ironLeggingsMeta.setLore(General.Game.loreMaker(GOLD +
                "10 Iron Ingots", GREEN +
                "This will buy / equip Iron Leggings."));
        ironLeggings.setItemMeta(ironLeggingsMeta);

        ItemStack ironBoots = new ItemStack(Material.IRON_BOOTS);
        ItemMeta ironBootsMeta = ironBoots.getItemMeta();
        ironBootsMeta.setDisplayName(teamColor + "Iron Boots");
        ironBootsMeta.setLore(General.Game.loreMaker(GOLD +
                "4 Iron Ingots", GREEN +
                "This will buy / equip Iron Boots."));
        ironBoots.setItemMeta(ironBootsMeta);

        ItemStack ironArmor = new ItemStack(Material.IRON_BLOCK);
        ItemMeta ironArmorMeta = ironArmor.getItemMeta();
        ironArmorMeta.setDisplayName(teamColor + "Iron Armor");
        ironArmorMeta.setLore(General.Game.loreMaker(GOLD +
                "32 Iron Ingots", GREEN +
                "This will buy / equip all Iron Armor."));
        ironArmor.setItemMeta(ironArmorMeta);

        ItemStack diamondHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemMeta diamondHelmetMeta = diamondHelmet.getItemMeta();
        diamondHelmetMeta.setDisplayName(teamColor + "Diamond Helmet");
        diamondHelmetMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Diamonds", GREEN +
                "This will buy / equip a Diamond Helmet."));
        diamondHelmet.setItemMeta(diamondHelmetMeta);

        ItemStack diamondChestplate = new ItemStack(
                Material.DIAMOND_CHESTPLATE);
        ItemMeta diamondChestplateMeta = diamondChestplate.getItemMeta();
        diamondChestplateMeta.setDisplayName(teamColor +
                "Diamond Chestplate");
        diamondChestplateMeta.setLore(General.Game.loreMaker(GOLD +
                "6 Diamonds", GREEN +
                "This will buy / equip a Diamond Chestplate."));
        diamondChestplate.setItemMeta(diamondChestplateMeta);

        ItemStack diamondLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemMeta diamondLeggingsMeta = diamondLeggings.getItemMeta();
        diamondLeggingsMeta.setDisplayName(teamColor + "Diamond Leggings");
        diamondLeggingsMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Diamonds", GREEN +
                "This will buy / equip Diamond Leggings."));
        diamondLeggings.setItemMeta(diamondLeggingsMeta);

        ItemStack diamondBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta diamondBootsMeta = diamondBoots.getItemMeta();
        diamondBootsMeta.setDisplayName(teamColor + "Diamond Boots");
        diamondBootsMeta.setLore(General.Game.loreMaker(GOLD +
                "2 Diamonds", GREEN +
                "This will buy / equip Diamond Boots."));
        diamondBoots.setItemMeta(diamondBootsMeta);

        ItemStack diamondArmor = new ItemStack(Material.DIAMOND_BLOCK);
        ItemMeta diamondArmorMeta = diamondArmor.getItemMeta();
        diamondArmorMeta.setDisplayName(teamColor + "Diamond Armor");
        diamondArmorMeta.setLore(General.Game.loreMaker(GOLD +
                "16 Diamonds", GREEN +
                "This will buy / equip all Diamond Armor."));
        diamondArmor.setItemMeta(diamondArmorMeta);

        ItemStack netheriteHelmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta netheriteHelmetMeta = netheriteHelmet.getItemMeta();
        netheriteHelmetMeta.setDisplayName(teamColor + "Netherite Helmet");
        netheriteHelmetMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Netherite Ingots", GREEN +
                "This will buy / equip a Netherite Helmet."));
        netheriteHelmet.setItemMeta(netheriteHelmetMeta);

        ItemStack netheriteChestplate = new ItemStack(
                Material.NETHERITE_CHESTPLATE);
        ItemMeta netheriteChestplateMeta = netheriteChestplate.getItemMeta();
        netheriteChestplateMeta.setDisplayName(GOLD + "Netherite Chestplate");
        netheriteChestplateMeta.setLore(General.Game.loreMaker(GOLD +
                "6 Netherite Ingots", GREEN +
                "This will buy / equip a Netherite Chestplate."));
        netheriteChestplate.setItemMeta(netheriteChestplateMeta);

        ItemStack netheriteLeggings = new ItemStack(
                Material.NETHERITE_LEGGINGS);
        ItemMeta netheriteLeggingsMeta = netheriteLeggings.getItemMeta();
        netheriteLeggingsMeta.setDisplayName(teamColor +
                "Netherite Leggings");
        netheriteLeggingsMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Netherite Ingots", GREEN +
                "This will buy / equip Netherite Leggings."));
        netheriteLeggings.setItemMeta(netheriteLeggingsMeta);

        ItemStack netheriteBoots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta netheriteBootsMeta = netheriteBoots.getItemMeta();
        netheriteBootsMeta.setDisplayName(teamColor + "Netherite Boots");
        netheriteBootsMeta.setLore(General.Game.loreMaker(GOLD +
                "2 Netherite Ingots", GREEN +
                "This will buy / equip Netherite Boots."));
        netheriteBoots.setItemMeta(netheriteBootsMeta);

        ItemStack netheriteArmor = new ItemStack(Material.NETHERITE_BLOCK);
        ItemMeta netheriteArmorMeta = netheriteArmor.getItemMeta();
        netheriteArmorMeta.setDisplayName(teamColor + "Netherite Armor");
        netheriteArmorMeta.setLore(General.Game.loreMaker(GOLD +
                "16 Netherite Ingots", GREEN +
                "This will buy / equip all Netherite Armor."));
        netheriteArmor.setItemMeta(netheriteArmorMeta);

        ItemStack[] items = new ItemStack[]{leatherHelmet, chainHelmet,
                ironHelmet, diamondHelmet, netheriteHelmet, leatherChestplate,
                chainChestplate, ironChestplate, diamondChestplate,
                netheriteChestplate, leatherLeggings, chainLeggings,
                ironLeggings, diamondLeggings, netheriteLeggings,
                leatherBoots, chainBoots, ironBoots, diamondBoots,
                netheriteBoots, leatherArmor, chainArmor, ironArmor,
                diamondArmor, netheriteArmor};
        gui.setItem(40, new NamedItem(Material.BARRIER, teamColor +
                "Go Back To Shop Selection").getItemStack());

        int i = 0;
        int nextRow = 0;
        int skipRow = 0;
        int x = 0, y = 0;
        for (ItemStack item : items) {
            if (y == 5) {
                y = 0;
                x++;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(true);
            List<String> lore = meta.getLore();
            if (item.getType() == Material.LEATHER ||
                    item.getType() == Material.CHAIN ||
                    item.getType().name().endsWith("BLOCK")) {
                boolean armorUnlocked = true;
                for (int j = 0; j < 4; j++) {
                    if (!gamePlayer.isArmorUnlocked(j, y)) {
                        armorUnlocked = false;
                        break;
                    }
                }
                if (armorUnlocked) {
                    lore.remove(0);
                    meta.setLore(lore);
                }
            } else if (lore.size() > 1 && gamePlayer.isArmorUnlocked(x, y)) {
                lore.remove(0);
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            gui.setItem(i, item);
            if (nextRow++ == 4) {
                nextRow = 0;
                i--;
                skipRow++;
            }
            if (skipRow == 4) {
                skipRow = 0;
                i += 9;
            }
            i += 2;
            y++;
        }

        for (i = 0; i < gui.getSize(); i++)
            if (gui.getItem(i) == null) gui.setItem(i, air.getItemStack());

//        for (int i = 0; i < gui.getContents().length; i++) {
//            String itemName = "";
//            int cost = 0;
//            int multiplier = 1;
//            String materialCostName = "";
//
//            int x = i / 9;
//            int y = i % 9;
//            switch (y) {
//                case 2:
//                    y = 1;
//                    break;
//                case 4:
//                    y = 2;
//                    break;
//                case 6:
//                    y = 3;
//                    break;
//                case 8:
//                    y = 4;
//            }
//
//            // Handles the Columns
//            switch (i % 9) {
//                case 0:
//                    itemName += "LEATHER_";
//                    break;
//                case 2:
//                    itemName += "CHAINMAIL_";
//                    materialCostName = "IRON_INGOT";
//                    break;
//                case 4:
//                    itemName += "IRON_";
//                    materialCostName = "IRON_INGOT";
//                    multiplier = 2;
//                    break;
//                case 6:
//                    itemName += "DIAMOND_";
//                    materialCostName = "DIAMONDS";
//                    break;
//                case 8:
//                    itemName += "NETHERITE_";
//                    materialCostName = "NETHERITE_INGOT";
//                    break;
//                default:
//                    gui.setItem(i, air.getItemStack());
//                    continue;
//            }
//
//            // Handles the Rows
//            switch (i / 9) {
//                case 0:
//                    itemName += "HELMET";
//                    cost = 3;
//                    break;
//                case 1:
//                    itemName += "CHESTPLATE";
//                    cost = 6;
//                    break;
//                case 2:
//                    itemName += "LEGGINGS";
//                    cost = 5;
//                    break;
//                case 3:
//                    itemName += "BOOTS";
//                    cost = 2;
//                    break;
//                case 5:
//                    if (i % 9 == 0) {
//                        itemName = "LEATHER";
//                    } else if (i % 9 == 2) {
//                        itemName = "CHAIN";
//                    } else {
//                        itemName += "BLOCK";
//                    }
//
//                    for (int j = 0; j < 4; j++) {
//                        if (!gamePlayer.isArmorUnlocked(j, y)) {
//                            switch (j) {
//                                case 0:
//                                    cost += 3;
//                                    break;
//                                case 1:
//                                    cost += 6;
//                                    break;
//                                case 2:
//                                    cost += 5;
//                                    break;
//                                case 3:
//                                    cost += 2;
//                            }
//                        }
//                    }
//                    break;
//                default:
//                    gui.setItem(i, air.getItemStack());
//                    continue;
//            }
//
//            cost *= multiplier;
//
//            char charBefore = itemName.charAt(0);
//            StringBuilder realItemName = new StringBuilder(charBefore + "");
//            for (int j = 1; j < itemName.toCharArray().length; j++) {
//                char c = itemName.charAt(j);
//                if (c == '_') {
//                    realItemName.append(" ");
//                } else if (charBefore != '_') {
//                    realItemName.append(Character.toLowerCase(c));
//                } else {
//                    realItemName.append(c);
//                }
//                charBefore = c;
//            }
//
//            StringBuilder realMaterialCostName = new StringBuilder();
//            if (!materialCostName.equals("")) {
//                char charBefore1 = materialCostName.charAt(0);
//                realMaterialCostName = new StringBuilder(charBefore1 + "");
//                for (int j = 1; j < materialCostName.
//                        toCharArray().length; j++) {
//                    char c = materialCostName.charAt(j);
//                    if (c == '_') {
//                        realMaterialCostName.append(" ");
//                    } else if (charBefore1 != '_') {
//                        realMaterialCostName.append(Character.toLowerCase(c));
//                    } else {
//                        realMaterialCostName.append(c);
//                    }
//                    charBefore1 = c;
//                }
//                if (!realMaterialCostName.toString().equals("Diamonds")) {
//                    realMaterialCostName.append("s");
//                }
//            }
//
//            if (itemName == null || Material.getMaterial(itemName) == null) {
//                continue;
//            }
//
//            ItemStack item;
//            String copyRealItemName = realItemName.toString();
//            if (copyRealItemName.endsWith("Block")) {
//                realItemName = new StringBuilder(realItemName.substring(
//                        0, realItemName.toString().length() - 6));
//                item = new NamedItem(Material.getMaterial(itemName),
//                        teamColor + realItemName.toString() +
//                                " Armor", true).getItemStack();
//            } else {
//                item = new NamedItem(Material.getMaterial(itemName),
//                        teamColor + realItemName.toString(),
//                        true).getItemStack();
//            }
//
//            ItemMeta itemMeta = item.getItemMeta();
//
//            if (item.getType().name().contains("LEATHER_")) {
//                LeatherArmorMeta meta = (LeatherArmorMeta) itemMeta;
//                meta.setColor(gamePlayer.getTeamColor().getArmorColor());
//                meta.setLore(Game.Game.loreMaker(ChatColor.GREEN +
//                        "This will equip " + realItemName + "."));
//                item.setItemMeta(meta);
//            } else if (item.getType() == Material.LEATHER) {
//                itemMeta.setDisplayName(teamColor + "Leather Armor");
//                itemMeta.setLore(Game.Game.loreMaker(
//                        ChatColor.GREEN +
//                                "This will equip all Leather Armor."));
//                item.setItemMeta(itemMeta);
//            }
//
//            if (cost == 0) {
//                gui.setItem(i, item);
//                continue;
//            } else if (x != 5) {
//                if (gamePlayer.isArmorUnlocked(x, y)) {
//                    gui.setItem(i, item);
//                    continue;
//                }
//            }
//
//            if (copyRealItemName.endsWith("Block")) {
//                itemMeta.setLore(Game.Game.loreMaker(ChatColor.GOLD +
//                                "" + cost + " " + realMaterialCostName,
//                        ChatColor.GREEN + "This will buy/equip all " +
//                                realItemName + " Armor."));
//            } else if (copyRealItemName.endsWith("Chain")) {
//                itemMeta.setDisplayName(teamColor + "Chainmail Armor");
//                itemMeta.setLore(Game.Game.loreMaker(ChatColor.GOLD +
//                                "" + cost + " " + realMaterialCostName,
//                        ChatColor.GREEN +
//                                "This will buy/equip all Chainmail Armor."));
//            } else {
//                itemMeta.setLore(Game.Game.loreMaker(ChatColor.GOLD +
//                                "" + cost + " " + realMaterialCostName,
//                        ChatColor.GREEN + "This will buy/equip " +
//                                realItemName + "."));
//            }
//            item.setItemMeta(itemMeta);
//            gui.setItem(i, item);
//        }
    }
}
