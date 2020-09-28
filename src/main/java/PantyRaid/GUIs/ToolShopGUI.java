package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public class ToolShopGUI {
    private final GamePlayer gamePlayer;
    private final Inventory gui;

    public ToolShopGUI(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
        Player player = gamePlayer.getPlayer();
        this.gui = Bukkit.createInventory(player, 54,
                gamePlayer.getTeamColor().getTextColor() +
                        "Tool / Weapon Shop");
        reload();
        player.openInventory(gui);
    }

    void reload() {
        NamedItem air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ");
        ChatColor teamColor = gamePlayer.getTeamColor().getTextColor();

        ItemStack woodenSword = new ItemStack(Material.WOODEN_SWORD);
        ItemMeta woodenSwordMeta = woodenSword.getItemMeta();
        woodenSwordMeta.setDisplayName(teamColor + "Wooden Sword");
        woodenSwordMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Wooden Sword."));
        woodenSword.setItemMeta(woodenSwordMeta);

        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        ItemMeta stoneSwordMeta = stoneSword.getItemMeta();
        stoneSwordMeta.setDisplayName(teamColor + "Stone Sword");
        stoneSwordMeta.setLore(General.Game.loreMaker(GOLD +
                "10 Iron Ingots", GREEN +
                "This will buy / equip a Stone Sword."));
        stoneSword.setItemMeta(stoneSwordMeta);

        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemMeta ironSwordMeta = ironSword.getItemMeta();
        ironSwordMeta.setDisplayName(teamColor + "Iron Sword");
        ironSwordMeta.setLore(General.Game.loreMaker(GOLD +
                "32 Iron Ingots", GREEN +
                "This will buy / equip an Iron Sword."));
        ironSword.setItemMeta(ironSwordMeta);

        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta diamondSwordMeta = diamondSword.getItemMeta();
        diamondSwordMeta.setDisplayName(teamColor + "Diamond Sword");
        diamondSwordMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Diamonds", GREEN +
                "This will buy / equip a Diamond Sword."));
        diamondSword.setItemMeta(diamondSwordMeta);

        ItemStack netheriteSword = new ItemStack(Material.NETHERITE_SWORD);
        ItemMeta netheriteSwordMeta = netheriteSword.getItemMeta();
        netheriteSwordMeta.setDisplayName(teamColor + "Netherite Sword");
        netheriteSwordMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Netherite Ingots", GREEN +
                "This will buy / equip a Netherite Sword."));
        netheriteSword.setItemMeta(netheriteSwordMeta);

        ItemStack woodenPickaxe = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta woodenPickaxeMeta = woodenPickaxe.getItemMeta();
        woodenPickaxeMeta.setDisplayName(teamColor + "Wooden Pickaxe");
        woodenPickaxeMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Wooden Pickaxe."));
        woodenPickaxe.setItemMeta(woodenPickaxeMeta);

        ItemStack stonePickaxe = new ItemStack(Material.STONE_PICKAXE);
        ItemMeta stonePickaxeMeta = stonePickaxe.getItemMeta();
        stonePickaxeMeta.setDisplayName(teamColor + "Stone Pickaxe");
        stonePickaxeMeta.setLore(General.Game.loreMaker(GOLD
                + "8 Iron Ingots", GREEN +
                "This will buy / equip a Stone Pickaxe."));
        stonePickaxe.setItemMeta(stonePickaxeMeta);

        ItemStack ironPickaxe = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta ironPickaxeMeta = ironPickaxe.getItemMeta();
        ironPickaxeMeta.setDisplayName(teamColor + "Iron Pickaxe");
        ironPickaxeMeta.setLore(General.Game.loreMaker(GOLD +
                "16 Iron Ingots", GREEN +
                "This will buy / equip an Iron Pickaxe."));
        ironPickaxe.setItemMeta(ironPickaxeMeta);

        ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta diamondPickaxeMeta = diamondPickaxe.getItemMeta();
        diamondPickaxeMeta.setDisplayName(teamColor + "Diamond Pickaxe");
        diamondPickaxeMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Diamonds", GREEN +
                "This will buy / equip a Diamond Pickaxe."));
        diamondPickaxe.setItemMeta(diamondPickaxeMeta);

        ItemStack netheritePickaxe = new ItemStack(
                Material.NETHERITE_PICKAXE);
        ItemMeta netheritePickaxeMeta = netheritePickaxe.getItemMeta();
        netheritePickaxeMeta.setDisplayName(teamColor + "Netherite Pickaxe");
        netheritePickaxeMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Netherite Ingots", GREEN +
                "This will buy / equip a Netherite Pickaxe."));
        netheritePickaxe.setItemMeta(netheritePickaxeMeta);

        ItemStack woodenAxe = new ItemStack(Material.WOODEN_AXE);
        ItemMeta woodenAxeMeta = woodenAxe.getItemMeta();
        woodenAxeMeta.setDisplayName(teamColor + "Wooden Axe");
        woodenAxeMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a Wooden Axe."));
        woodenAxe.setItemMeta(woodenAxeMeta);

        ItemStack stoneAxe = new ItemStack(Material.STONE_AXE);
        ItemMeta stoneAxeMeta = stoneAxe.getItemMeta();
        stoneAxeMeta.setDisplayName(teamColor + "Stone Axe");
        stoneAxeMeta.setLore(General.Game.loreMaker(GOLD +
                "10 Iron Ingots", GREEN +
                "This will buy / equip a Stone Axe."));
        stoneAxe.setItemMeta(stoneAxeMeta);

        ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
        ItemMeta ironAxeMeta = ironAxe.getItemMeta();
        ironAxeMeta.setDisplayName(teamColor + "Iron Axe");
        ironAxeMeta.setLore(General.Game.loreMaker(GOLD +
                "32 Iron Ingots", GREEN +
                "This will buy / equip an Iron Axe."));
        ironAxe.setItemMeta(ironAxeMeta);

        ItemStack diamondAxe = new ItemStack(Material.DIAMOND_AXE);
        ItemMeta diamondAxeMeta = diamondAxe.getItemMeta();
        diamondAxeMeta.setDisplayName(teamColor + "Diamond Axe");
        diamondAxeMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Diamonds", GREEN +
                "This will buy / equip a Diamond Axe."));
        diamondAxe.setItemMeta(diamondAxeMeta);

        ItemStack netheriteAxe = new ItemStack(Material.NETHERITE_AXE);
        ItemMeta netheriteAxeMeta = netheriteAxe.getItemMeta();
        netheriteAxeMeta.setDisplayName(teamColor + "Netherite Axe");
        netheriteAxeMeta.setLore(General.Game.loreMaker(GOLD +
                "3 Netherite Ingots", GREEN +
                "This will buy / equip a Netherite Axe."));
        netheriteAxe.setItemMeta(netheriteAxeMeta);

        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setDisplayName(teamColor + "Bow");
        bowMeta.setLore(General.Game.loreMaker(GREEN +
                "This will equip a standard Bow."));
        bow.setItemMeta(bowMeta);

        ItemStack p1Bow = new ItemStack(Material.BOW);
        ItemMeta p1BowMeta = p1Bow.getItemMeta();
        p1BowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1,
                false);
        p1BowMeta.setDisplayName(teamColor + "Power I Bow");
        p1BowMeta.setLore(General.Game.loreMaker(GOLD +
                "10 Iron Ingots", GREEN +
                "This will buy / equip a Power I Bow."));
        p1Bow.setItemMeta(p1BowMeta);

        ItemStack p2Bow = new ItemStack(Material.BOW);
        ItemMeta p2BowMeta = p2Bow.getItemMeta();
        p2BowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 2,
                false);
        p2BowMeta.setDisplayName(teamColor + "Power II Bow");
        p2BowMeta.setLore(General.Game.loreMaker(GOLD +
                "32 Iron Ingots", GREEN +
                "This will buy / equip a Power II Bow."));
        p2Bow.setItemMeta(p2BowMeta);

        ItemStack p3Bow = new ItemStack(Material.BOW);
        ItemMeta p3BowMeta = p3Bow.getItemMeta();
        p3BowMeta.addEnchant(Enchantment.ARROW_DAMAGE, 3,
                false);
        p3BowMeta.setDisplayName(teamColor + "Power III Bow");
        p3BowMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Diamonds", GREEN +
                "This will buy / equip a Power III Bow."));
        p3Bow.setItemMeta(p3BowMeta);

        ItemStack k1Bow = new ItemStack(Material.BOW);
        ItemMeta k1BowMeta = k1Bow.getItemMeta();
        k1BowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1,
                false);
        k1BowMeta.setDisplayName(teamColor + "Punch I Bow");
        k1BowMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Diamonds", GREEN +
                "This will buy / equip a Punch I Bow."));
        k1Bow.setItemMeta(k1BowMeta);

        ItemStack[] items = new ItemStack[]{woodenSword, woodenPickaxe,
                woodenAxe, bow, stoneSword, stonePickaxe,
                stoneAxe, p1Bow, ironSword, ironPickaxe,
                ironAxe, p2Bow, diamondSword, diamondPickaxe, diamondAxe,
                p3Bow, netheriteSword, netheritePickaxe, netheriteAxe, k1Bow};
        gui.setItem(0, new NamedItem(Material.BARRIER, teamColor +
                "Go Back To Shop Selection").getItemStack());

        int i = 11;
        boolean skipColumn = false;
        int skipRow = 0;
        int x = 0, y = 0;
        for (ItemStack item : items) {
            if (y == 4) {
                y = 0;
                x++;
            }
            ItemMeta meta = item.getItemMeta();
            meta.setUnbreakable(true);
            List<String> lore = meta.getLore();
            if (lore.size() > 1 && gamePlayer.isToolUnlocked(x, y)) {
                lore.remove(0);
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
            gui.setItem(i++, item);
            if (skipColumn) i++;
            skipColumn = !skipColumn;
            if (++skipRow == 4) {
                skipRow = 0;
                i += 3;
            }
            y++;
        }

        for (i = 0; i < gui.getSize(); i++)
            if (gui.getItem(i) == null) gui.setItem(i, air.getItemStack());
    }
}
