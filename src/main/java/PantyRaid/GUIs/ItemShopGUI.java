package PantyRaid.GUIs;

import General.GamePlayer;
import Items.NamedItem;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static org.bukkit.ChatColor.*;

public class ItemShopGUI {
    public ItemShopGUI(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        Inventory gui = Bukkit.createInventory(player, 45,
                gamePlayer.getTeamColor().getTextColor() + "Item Shop");

        ItemStack air = new NamedItem(Material.GRAY_STAINED_GLASS_PANE,
                " ").getItemStack();

        ItemStack arrow = new ItemStack(Material.ARROW, 8);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(YELLOW + "Arrow");
        arrowMeta.setLore(General.Game.loreMaker(GOLD +
                "1 Gold Ingot"));
        arrow.setItemMeta(arrowMeta);

        ItemStack incenArrow = new ItemStack(Material.TIPPED_ARROW, 2);
        PotionMeta incenArrowMeta = ((PotionMeta) incenArrow.getItemMeta());
        incenArrowMeta.setColor(Color.RED);
        incenArrowMeta.addCustomEffect(new PotionEffect(PotionEffectType.
                WEAKNESS, 1, 1), false);
        incenArrowMeta.setDisplayName(RED + "Incendiary Arrow");
        incenArrowMeta.setLore(General.Game.loreMaker(GOLD +
                "1 Gold Ingot", RED +
                "Burn your enemies to the ground.", GREEN +
                "(Note: Safe for use in heavily forested areas.)"));
        incenArrowMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        incenArrow.setItemMeta(incenArrowMeta);

        ItemStack shockArrow = new ItemStack(Material.TIPPED_ARROW, 2);
        PotionMeta shockArrowMeta = ((PotionMeta) shockArrow.getItemMeta());
        shockArrowMeta.setColor(Color.YELLOW);
        shockArrowMeta.addCustomEffect(new PotionEffect(PotionEffectType.
                SLOW, 1, 1), false);
        shockArrowMeta.setDisplayName(RED + "Shock Arrow");
        shockArrowMeta.setLore(General.Game.loreMaker(GOLD +
                "5 Gold Ingots", RED + "Stops enemies in their tracks!"));
        shockArrowMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        shockArrow.setItemMeta(shockArrowMeta);

        ItemStack explosiveArrow = new ItemStack(Material.SPECTRAL_ARROW);
        ItemMeta explosiveArrowMeta = explosiveArrow.getItemMeta();
        explosiveArrowMeta.setDisplayName(DARK_RED + "Explosive Arrow");
        explosiveArrowMeta.setLore(General.Game.loreMaker(
                GOLD + "15 Gold Ingots", RED +
                        "Blow your enemies to smithereens!",
                GREEN + "(Note: Not destroy block; only make big BOOM!)"));
        explosiveArrow.setItemMeta(explosiveArrowMeta);

        ItemStack shield = new ItemStack(Material.SHIELD);
        ItemMeta shieldMeta = shield.getItemMeta();
        shieldMeta.setDisplayName(YELLOW + "Shield");
        shieldMeta.setLore(General.Game.loreMaker(GOLD +
                        "4 Gold Ingots",
                GREEN + "Need to block some arrows or an axe?",
                GREEN + "This is the product for you! :)"));
        shield.setItemMeta(shieldMeta);

        ItemStack elytra = new ItemStack(Material.ELYTRA);
        ItemMeta elytraMeta = elytra.getItemMeta();
        elytraMeta.setDisplayName(YELLOW + "Elytra");
        elytraMeta.setUnbreakable(true);
        elytraMeta.setLore(General.Game.loreMaker(GOLD +
                        "10 Netherite Ingots",
                GREEN + "Save yourself from falling into the void,",
                GREEN + "or fly across the map to steal a flag."));
        elytra.setItemMeta(elytraMeta);

        ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
        FireworkMeta fireworkMeta = (FireworkMeta) firework.getItemMeta();
        fireworkMeta.setPower(5);
        fireworkMeta.setDisplayName(DARK_RED + "Firework Rocket");
        fireworkMeta.setLore(General.Game.loreMaker(GOLD +
                "1 Gold Ingot", GREEN + "#ElytraFuel"));
        firework.setItemMeta(fireworkMeta);

        ItemStack creeperEgg = new ItemStack(Material.EGG);
        ItemMeta creeperEggMeta = creeperEgg.getItemMeta();
        creeperEggMeta.setDisplayName(DARK_GREEN + "Creeper Egg");
        creeperEggMeta.setLore(General.Game.loreMaker(GOLD +
                        "1 Iron Ingots", GREEN + "Which came first, " +
                        "the Chicken or the Creeper?", GREEN + "This " +
                        "egg hatches Creepers instead of Chickens,",
                GREEN + "and has a 1% of spawning a Charged Creeper."));
        creeperEgg.setItemMeta(creeperEggMeta);

        ItemStack beeHive = new ItemStack(Material.BEE_NEST);
        ItemMeta beeHiveMeta = beeHive.getItemMeta();
        beeHiveMeta.setDisplayName(YELLOW + "Bee Hive");
        beeHiveMeta.setLore(General.Game.loreMaker(
                GOLD + "5 Gold Ingots", GREEN +
                        "Bee's don't like it when their hives are",
                GREEN + "thrown... Right-Click to throw a Bee Hive",
                GREEN + "that spawns 2 - 6 Angry Bees upon breaking."));
        beeHive.setItemMeta(beeHiveMeta);

        ItemStack oneUp = new ItemStack(Material.WARPED_FUNGUS);
        ItemMeta oneUpMeta = oneUp.getItemMeta();
        oneUpMeta.setDisplayName(GREEN + "1-UP");
        oneUpMeta.setLore(General.Game.loreMaker(
                GOLD + "10 Netherite Ingots", GREEN +
                        "Gives you an extra life (Max: 3) after all",
                GREEN + "of your Team's flags have been stolen."));
        oneUp.setItemMeta(oneUpMeta);

        ItemStack softTurtleShell = new ItemStack(Material.TURTLE_HELMET);
        ItemMeta softTurtleShellMeta = softTurtleShell.getItemMeta();
        softTurtleShellMeta.setDisplayName(DARK_GREEN +
                "Soft-Shell Turtle Shell");
        softTurtleShellMeta.setLore(General.Game.loreMaker(
                GOLD + "8 Iron Ingots", GREEN +
                        "Right-Click this item to give you slight",
                GREEN + "bonus protection against attacks."));
        softTurtleShell.setItemMeta(softTurtleShellMeta);

        ItemStack turtleShell = new ItemStack(Material.TURTLE_HELMET);
        ItemMeta turtleShellMeta = turtleShell.getItemMeta();
        turtleShellMeta.setDisplayName(DARK_GREEN + "Turtle Shell");
        turtleShellMeta.setLore(General.Game.loreMaker(
                GOLD + "8 Gold Ingots", GREEN +
                        "Right-Click this item to give you sufficient",
                GREEN + "bonus protection against attacks."));
        turtleShell.setItemMeta(turtleShellMeta);

        ItemStack bearTrap = new ItemStack(Material.SPRUCE_TRAPDOOR);
        ItemMeta bearTrapMeta = bearTrap.getItemMeta();
        bearTrapMeta.setDisplayName(DARK_GREEN + "Bear Trap");
        bearTrapMeta.setLore(General.Game.loreMaker(
                GOLD + "4 Iron Ingots", GREEN +
                        "When an opponent steps on this, they will",
                GREEN + "be stuck in place for a little while."));
        bearTrap.setItemMeta(bearTrapMeta);

        ItemStack reinforcedBearTrap = new ItemStack(Material.IRON_TRAPDOOR);
        ItemMeta reinforcedBearTrapMeta = reinforcedBearTrap.getItemMeta();
        reinforcedBearTrapMeta.setDisplayName(DARK_GREEN +
                "Reinforced Bear Trap");
        reinforcedBearTrapMeta.setLore(General.Game.loreMaker(
                GOLD + "4 Gold Ingots", GREEN +
                        "When an opponent steps on this, they",
                GREEN + "will be stuck in place for a while."));
        reinforcedBearTrap.setItemMeta(reinforcedBearTrapMeta);

        ItemStack smokeBomb = new ItemStack(Material.CHARCOAL);
        ItemMeta smokeBombMeta = smokeBomb.getItemMeta();
        smokeBombMeta.setDisplayName(DARK_PURPLE + "Smoke Bomb");
        smokeBombMeta.setLore(General.Game.loreMaker(
                GOLD + "2 Iron Ingots", GREEN +
                        "Throw this at your opponent to",
                GREEN + "cover the area in smoke."));
        smokeBomb.setItemMeta(smokeBombMeta);

        ItemStack flashBang = new ItemStack(Material.INK_SAC);
        ItemMeta flashBangMeta = flashBang.getItemMeta();
        flashBangMeta.setDisplayName(DARK_BLUE + "Flash Bang");
        flashBangMeta.setLore(General.Game.loreMaker(
                GOLD + "2 Gold Ingots", GREEN +
                        "Throw this at your opponent to disorient them."));
        flashBang.setItemMeta(flashBangMeta);

        ItemStack tomatoSoup = new ItemStack(Material.BEETROOT_SOUP);
        ItemMeta tomatoSoupMeta = tomatoSoup.getItemMeta();
        tomatoSoupMeta.setDisplayName(RED + "Tomato Soup");
        tomatoSoupMeta.setLore(General.Game.loreMaker(
                GOLD + "10 Iron Ingots", GREEN +
                        "Right-Click to chug this scolding soup",
                GREEN + "to get Instant Health I."));
        tomatoSoup.setItemMeta(tomatoSoupMeta);

        ItemStack misoSoup = new ItemStack(Material.MUSHROOM_STEW);
        ItemMeta misoSoupMeta = misoSoup.getItemMeta();
        misoSoupMeta.setDisplayName(GRAY + "Miso Soup");
        misoSoupMeta.setLore(General.Game.loreMaker(
                GOLD + "3 Gold Ingots", GREEN +
                        "Right-Click to graciously ladle this soup",
                GREEN + "to get Instant Health II."));
        misoSoup.setItemMeta(misoSoupMeta);

        ItemStack rabbitStroganoff = new ItemStack(Material.RABBIT_STEW);
        ItemMeta rabbitStroganoffMeta = rabbitStroganoff.getItemMeta();
        rabbitStroganoffMeta.setDisplayName(DARK_BLUE + "Rabbit Stroganoff");
        rabbitStroganoffMeta.setLore(General.Game.loreMaker(
                GOLD + "5 Gold Ingots", GREEN +
                        "Right-Click to scarf down this delicious",
                GREEN + "stroganoff to get Instant Health II and",
                GREEN + "Regeneration II"));
        rabbitStroganoff.setItemMeta(rabbitStroganoffMeta);

        ItemStack mine = new ItemStack(Material.SPRUCE_PRESSURE_PLATE);
        ItemMeta mineMeta = mine.getItemMeta();
        mineMeta.setDisplayName(RED + "Mine");
        mineMeta.setLore(General.Game.loreMaker(
                GOLD + "6 Iron Ingots", GREEN +
                        "When an opponent steps on this, BOOM!"));
        mine.setItemMeta(mineMeta);

        ItemStack reinforcedMine = new ItemStack(
                Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
        ItemMeta reinforcedMineMeta = reinforcedMine.getItemMeta();
        reinforcedMineMeta.setDisplayName(RED + "Reinforced Mine");
        reinforcedMineMeta.setLore(General.Game.loreMaker(
                GOLD + "6 Gold Ingots", GREEN +
                        "When an opponent steps on this, BIG BOOM!"));
        reinforcedMine.setItemMeta(reinforcedMineMeta);

        gui.setItem(0, new NamedItem(Material.BARRIER,
                gamePlayer.getTeamColor().getTextColor() +
                        "Go Back To Shop Selection").getItemStack());
        gui.setItem(10, arrow);
        gui.setItem(11, incenArrow);
        gui.setItem(12, shockArrow);
        gui.setItem(13, explosiveArrow);
        gui.setItem(14, shield);
        gui.setItem(15, elytra);
        gui.setItem(16, firework);

        gui.setItem(19, creeperEgg);
        gui.setItem(20, beeHive);
        gui.setItem(21, oneUp);
        gui.setItem(22, softTurtleShell);
        gui.setItem(23, turtleShell);
        gui.setItem(24, bearTrap);
        gui.setItem(25, reinforcedBearTrap);

        gui.setItem(28, smokeBomb);
        gui.setItem(29, flashBang);
        gui.setItem(30, tomatoSoup);
        gui.setItem(31, misoSoup);
        gui.setItem(32, rabbitStroganoff);
        gui.setItem(33, mine);
        gui.setItem(34, reinforcedMine);

        for (int i = 0; i < gui.getSize(); i++)
            if (gui.getItem(i) == null) gui.setItem(i, air);

        player.openInventory(gui);
    }
}
