package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import Team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlockShopGUI {
    public BlockShopGUI(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Inventory gui = Bukkit.createInventory(player, 36,
                gamePlayer.getTeamColor().getTextColor() + "Block Shop");

        ItemStack air = new NamedItem(Material.GRAY_STAINED_GLASS_PANE,
                " ").getItemStack();

        ItemStack stone = new ItemStack(Material.STONE, 8);
        ItemMeta stoneMeta = stone.getItemMeta();
        stoneMeta.setDisplayName(ChatColor.GRAY + "Stone");
        stoneMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "2 Iron Ingots"));
        stone.setItemMeta(stoneMeta);

        ItemStack planks = new ItemStack(Material.SPRUCE_PLANKS, 4);
        ItemMeta planksMeta = planks.getItemMeta();
        planksMeta.setDisplayName(ChatColor.YELLOW + "Wooden Planks");
        planksMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "2 Iron Ingots"));
        planks.setItemMeta(planksMeta);

        Team team = gamePlayer.getTeam();
        Material teamGlassPane = team.getColor().getFlagMaterial();
        Material teamGlass = Material.getMaterial(teamGlassPane.name().
                substring(0, teamGlassPane.name().length() - 5));
        ItemStack glass = new ItemStack(teamGlass, 16);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(team.getColor().getTextColor() + "Glass");
        glassMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "3 Iron Ingots"));
        glass.setItemMeta(glassMeta);

        Material teamConcrete = Material.getMaterial(teamGlassPane.name().
                substring(0, teamGlassPane.name().length() - 19) +
                "_CONCRETE");
        ItemStack concrete = new ItemStack(teamConcrete, 16);
        ItemMeta concreteMeta = concrete.getItemMeta();
        concreteMeta.setDisplayName(team.getColor().getTextColor() +
                "Concrete");
        concreteMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "5 Iron Ingots"));
        concrete.setItemMeta(concreteMeta);

        Material teamConcretePowder = Material.getMaterial(
                teamConcrete.name() + "_POWDER");
        ItemStack concretePowder = new ItemStack(teamConcretePowder,
                16);
        ItemMeta concretePowderMeta = concretePowder.getItemMeta();
        concretePowderMeta.setDisplayName(team.getColor().getTextColor() +
                "Kinetic Sand!!");
        concretePowderMeta.setLore(General.Game.loreMaker(
                ChatColor.GOLD + "3 Iron Ingots"));
        concretePowder.setItemMeta(concretePowderMeta);

        ItemStack endstone = new ItemStack(Material.END_STONE, 8);
        ItemMeta endstoneMeta = endstone.getItemMeta();
        endstoneMeta.setDisplayName(ChatColor.YELLOW + "Endstone");
        endstoneMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "10 Iron Ingots"));
        endstone.setItemMeta(endstoneMeta);

        ItemStack cobweb = new ItemStack(Material.COBWEB, 4);
        ItemMeta cobwebMeta = cobweb.getItemMeta();
        cobwebMeta.setDisplayName(ChatColor.WHITE + "Cobweb");
        cobwebMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "5 Gold Ingots"));
        cobweb.setItemMeta(cobwebMeta);

        ItemStack scaffolding = new ItemStack(Material.SCAFFOLDING, 8);
        ItemMeta scaffoldingMeta = scaffolding.getItemMeta();
        scaffoldingMeta.setDisplayName(ChatColor.YELLOW + "Scaffolding");
        scaffoldingMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "2 Gold Ingots"));
        scaffolding.setItemMeta(scaffoldingMeta);

        ItemStack packedIce = new ItemStack(Material.PACKED_ICE, 12);
        ItemMeta packedIceMeta = packedIce.getItemMeta();
        packedIceMeta.setDisplayName(ChatColor.AQUA + "Packed Ice");
        packedIceMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "6 Gold Ingots"));
        packedIce.setItemMeta(packedIceMeta);

        ItemStack blueIce = new ItemStack(Material.BLUE_ICE, 6);
        ItemMeta blueIceMeta = blueIce.getItemMeta();
        blueIceMeta.setDisplayName(ChatColor.AQUA + "Blue Ice");
        blueIceMeta.setLore(General.Game.loreMaker(ChatColor.GOLD +
                "10 Gold Ingots"));
        blueIce.setItemMeta(blueIceMeta);

        gui.setItem(0, new NamedItem(Material.BARRIER,
                gamePlayer.getTeamColor().getTextColor() +
                        "Go Back To Shop Selection").getItemStack());
        gui.setItem(10, stone);
        gui.setItem(11, planks);

        gui.setItem(14, glass);
        gui.setItem(15, concrete);
        gui.setItem(16, concretePowder);

        gui.setItem(19, cobweb);
        gui.setItem(20, scaffolding);

        gui.setItem(23, endstone);
        gui.setItem(24, packedIce);
        gui.setItem(25, blueIce);

        for (int i = 0; i < gui.getSize(); i++) {
            if (gui.getItem(i) == null) {
                gui.setItem(i, air);
            }
        }

        player.openInventory(gui);
    }
}
