package PantyRaid;

import GUIs.TeamGUI;
import General.Countdown;
import General.Game;
import General.GamePlayer;
import Items.NamedItem;
import PantyRaid.GUIs.*;
import Team.Team;
import Team.TeamColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Listeners implements Listener {
    public Listeners(Plugin plugin) {
        System.out.println("\u001B[32mEnabling \u001B[35mPanty" +
                "\u001B[36mRaid \u001B[32mListeners! \u001B[0m");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        Game.getGamePlayer(player);
        PlayerData.getPlayerData(player).loadPlayerData();
        // TODO: If something goes wrong in terms of GamePlayer/PlayerData
        //  stuff, it is most likely here.
        if (!Game.gameGoingAlready) {
            Game.changeWorld(player, Game.lobbyWorldName);
            player.sendMessage(ChatColor.AQUA + "In order to begin the " +
                    "game, you must select a team and then fire your " +
                    "Firework Rocket. If you do not select a team, one " +
                    "will be selected for you when the game starts.");
        } else {
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    void onLeave(PlayerQuitEvent event) {
        System.out.println("Game Team:");
        for (Team team : Game.gameTeams)
            for (GamePlayer gamePlayer : team)
                System.out.println(gamePlayer);

        System.out.println("Lobby Players:");
        for (GamePlayer gamePlayer : Game.whichPlayersToPlayWith())
            System.out.println(gamePlayer);
        // TODO: Ensure that that Player is still connected to their Team
        //  and that nothing explodes about NPE's and Teams/GamePlayers.
        //  This needs majorly fixed before it is officially released.
    }

    @EventHandler
    void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getName().equalsIgnoreCase(Game.gameWorldName))
            player.setGameMode(GameMode.SURVIVAL);
        else player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        Material material = item.getType();
        String itemName = material.name();

        if (player.getWorld().getName().
                equalsIgnoreCase(Game.lobbyWorldName)) {
            event.setCancelled(true);
            return;
        }

        if (itemName.endsWith("SWORD") || itemName.endsWith("PICKAXE") ||
                itemName.endsWith("AXE") || itemName.endsWith("BOW") ||
                itemName.endsWith("HELMET") ||
                itemName.endsWith("CHESTPLATE") ||
                itemName.endsWith("LEGGINGS") ||
                itemName.endsWith("BOOTS") || itemName.endsWith("PANE")) {
            event.setCancelled(true);
            return;
        }

        switch (material) {
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS:
                assert item.getItemMeta() != null;
                if (item.getItemMeta().getDisplayName().contains("Flag")) {
                    player.sendMessage(ChatColor.RED + "You can't drop " +
                            "the flag! Get it back to your base!");
                    event.setCancelled(true);
                    return;
                }
            case NETHER_STAR:
                player.sendMessage(ChatColor.RED + "You can't drop " +
                        "your nether star! Get it back to your base for " +
                        "a handsome reward!");
                event.setCancelled(true);
                return;
        }
    }

    /**
     * Checks the given player's Team. If the Player is on a Team,
     * return the banner for the Team's color, otherwise just return
     * a white banner.
     *
     * @param player is the Player that is checked.
     * @return the banner of the Team that the Player is on, if it is on one.
     */
    private Material chooseTeamItem(Player player) {
        return Game.getGamePlayer(player).getTeamColor().getBannerMaterial();
    }

    /**
     * Sets the inventory of the given player for the lobby world.
     *
     * @param player is the Player that has their inventory set.
     */
    void setLobbyInv(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setItem(1, new NamedItem(chooseTeamItem(player),
                ChatColor.GREEN + "Choose Team").getItemStack());
        inv.setItem(3, new NamedItem(Material.BOOK,
                ChatColor.RED + "Vote on a Gamemode/Map").getItemStack());
        inv.setItem(4, new NamedItem(Material.FIREWORK_ROCKET,
                ChatColor.GOLD + "Fire when Ready!").getItemStack());
        inv.setItem(5, new NamedItem(Material.ENCHANTED_BOOK,
                ChatColor.DARK_GREEN + "View your Player Data").
                getItemStack());
        inv.setItem(7, new NamedItem(Material.NAME_TAG,
                ChatColor.DARK_AQUA + "Tags").getItemStack());
    }

    /**
     * Sets the inventory of the given player for the game world.
     * The given gamemode is used to decide what inventory should
     * be set.
     *
     * @param player   is the Player that has their inventory set.
     * @param gamemode is the Gamemode that decides what the given
     *                 player's inventory should have in it.
     */
    void setGameInv(Player player, Gamemode gamemode) {
        // TODO: When you add Gamemodes, insert a
        //  switch between Gamemodes here.

        PlayerInventory inv = player.getInventory();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        ChatColor color = gamePlayer.getTeamColor().getTextColor();
        inv.clear();
        ItemStack[] armor = new ItemStack[]{
                new NamedItem(Material.LEATHER_BOOTS, color +
                        "Leather Boots", true).getItemStack(),
                new NamedItem(Material.LEATHER_LEGGINGS, color +
                        "Leather Leggings", true).getItemStack(),
                new NamedItem(Material.LEATHER_CHESTPLATE, color +
                        "Leather Chestplate", true).getItemStack(),
                new NamedItem(Material.LEATHER_HELMET, color +
                        "Leather Helmet", true).getItemStack(),};
        for (ItemStack item : armor) {
            LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
            meta.setColor(gamePlayer.getTeamColor().getArmorColor());
            item.setItemMeta(meta);
        }
        inv.setArmorContents(armor);
        inv.setItem(0, new
                NamedItem(Material.WOODEN_SWORD, color +
                "Wooden Sword", true).getItemStack());
        inv.setItem(1, new
                NamedItem(Material.WOODEN_PICKAXE, color +
                "Wooden Pickaxe", true).getItemStack());
        inv.setItem(2, new
                NamedItem(Material.WOODEN_AXE, color +
                "Wooden Axe", true).getItemStack());
        inv.setItem(3, new
                NamedItem(Material.BOW, color +
                "Bow", true).getItemStack());
        inv.setItem(4, getFlagPlaceholder(gamePlayer.getTeam()));

        ItemStack starterArrows = new ItemStack(Material.ARROW, 4);
        ItemMeta arrowMeta = starterArrows.getItemMeta();
        arrowMeta.setDisplayName(ChatColor.YELLOW + "Arrow");
        starterArrows.setItemMeta(arrowMeta);
        inv.setItem(9, starterArrows);
    }

    /**
     * Creates and returns the stained glass pane flag
     * placeholder ItemStack for the given Team.
     *
     * @param team is the Team used to make the flag placeholder ItemStack.
     * @return the flag placeholder ItemStack.
     */
    public ItemStack getFlagPlaceholder(Team team) {
        TeamColor color = team == null ? TeamColor.NULL : team.getColor();
        // TODO: This is the problem. Make sure to go back and look at
        //  the randomizeUnteamedPlayers method later.
        ItemStack flagPlaceholder = new ItemStack(color.getFlagMaterial());
        ItemMeta flagMeta = flagPlaceholder.getItemMeta();
        assert flagMeta != null;
        flagMeta.setDisplayName(color.getTextColor() + "Flag goes here.");
        flagMeta.setLore(Game.loreMaker(ChatColor.RED +
                        "This cannot be dropped, and, ", ChatColor.RED +
                        "upon picking up a team's flag from their armor",
                ChatColor.RED + "stand, this will be " +
                        "temporarily replaced."));
        flagPlaceholder.setItemMeta(flagMeta);
        return flagPlaceholder;
    }

    @EventHandler
    void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        PlayerInventory inv = player.getInventory();
        Material material = event.getMaterial();
        String worldName = player.getWorld().getName();

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (player.getWorld().getName().equalsIgnoreCase(
                Game.lobbyWorldName)) {
            setLobbyInv(player);
            event.setCancelled(true);
        }

        if (event.getAction() == Action.PHYSICAL) {
            World world = Game.server.getWorld(Game.gameWorldName);
            for (double i = -1; i < 0.5; i += 0.5) {
                Block blockBelow = world.getBlockAt(event.getPlayer().
                        getLocation().add(0, i, 0));
                switch (blockBelow.getType()) {
                    case SPRUCE_PRESSURE_PLATE:
                        blockBelow.breakNaturally();
                        world.createExplosion(blockBelow.getLocation(),
                                1.0f, false, false);
                        return;
                    case HEAVY_WEIGHTED_PRESSURE_PLATE:
                        blockBelow.breakNaturally();
                        world.createExplosion(blockBelow.getLocation(),
                                2.0f, false, false);
                        return;
                }
            }
        }

        switch (material) {
            case TURTLE_HELMET:
                if (inv.getItemInMainHand().equals(event.getItem())) {
                    if (inv.getItemInMainHand().getItemMeta().
                            getDisplayName().contains("Soft-Shell")) {
                        Game.addPotionEffect(player, new PotionEffect(
                                PotionEffectType.ABSORPTION, 1000,
                                1));
                    } else {
                        Game.addPotionEffect(player, new PotionEffect(
                                PotionEffectType.ABSORPTION, 2000,
                                2));
                    }
                    inv.setItemInMainHand(new ItemStack(Material.AIR));
                } else if (inv.getItemInOffHand().equals(event.getItem())) {
                    if (inv.getItemInOffHand().getItemMeta().
                            getDisplayName().contains("Soft-Shell")) {
                        Game.addPotionEffect(player, new PotionEffect(
                                PotionEffectType.ABSORPTION, 1000,
                                1));
                    } else {
                        Game.addPotionEffect(player, new PotionEffect(
                                PotionEffectType.ABSORPTION, 2000,
                                2));
                    }
                    inv.setItemInOffHand(new ItemStack(Material.AIR));
                }
                break;
            case BEE_NEST:
                Snowball snowball = player.launchProjectile(Snowball.class,
                        player.getEyeLocation().getDirection().multiply(2));
                if (inv.getItemInMainHand().getType() ==
                        Material.BEE_NEST) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                return;
            case BEETROOT_SOUP:
                if (inv.getItemInMainHand().getType() ==
                        Material.BEETROOT_SOUP) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                Game.addPotionEffect(player, new PotionEffect(
                        PotionEffectType.HEAL, 1, 0));
                player.setFoodLevel(Math.min(
                        player.getFoodLevel() + 3, 20));
                event.setCancelled(true);
                return;
            case MUSHROOM_STEW:
                if (inv.getItemInMainHand().getType() ==
                        Material.MUSHROOM_STEW) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                Game.addPotionEffect(player, new PotionEffect(
                        PotionEffectType.HEAL, 1, 1));
                player.setFoodLevel(Math.min(
                        player.getFoodLevel() + 6, 20));
                event.setCancelled(true);
                return;
            case RABBIT_STEW:
                if (inv.getItemInMainHand().getType() ==
                        Material.RABBIT_STEW) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                Game.addPotionEffect(player, new PotionEffect(
                        PotionEffectType.HEAL, 1, 1));
                Game.addPotionEffect(player, new PotionEffect(
                        PotionEffectType.REGENERATION,
                        120, 1));
                player.setFoodLevel(Math.min(
                        player.getFoodLevel() + 9, 20));
                event.setCancelled(true);
                return;
            case CHARCOAL:
                ThrownPotion smokeBomb = player.launchProjectile(
                        ThrownPotion.class,
                        player.getEyeLocation().getDirection().multiply(2));
                if (inv.getItemInMainHand().getType() ==
                        Material.CHARCOAL) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                ItemStack potion = new ItemStack(
                        Material.SPLASH_POTION, 1);
                PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(
                        PotionEffectType.BLINDNESS,
                        250, 1), false);
                potionMeta.addCustomEffect(new PotionEffect(
                        PotionEffectType.CONFUSION,
                        250, 1), false);
                potionMeta.setColor(Color.PURPLE);
                potion.setItemMeta(potionMeta);
                smokeBomb.setItem(potion);
                return;
            case INK_SAC:
                ThrownPotion flashBang = player.launchProjectile(
                        ThrownPotion.class,
                        player.getEyeLocation().getDirection().multiply(2));
                if (inv.getItemInMainHand().getType() == Material.INK_SAC) {
                    inv.getItemInMainHand().setAmount(
                            inv.getItemInMainHand().getAmount() - 1);
                } else {
                    inv.getItemInOffHand().setAmount(
                            inv.getItemInOffHand().getAmount() - 1);
                }
                potion = new ItemStack(
                        Material.SPLASH_POTION, 1);
                potionMeta = (PotionMeta) potion.getItemMeta();
                potionMeta.addCustomEffect(new PotionEffect(
                        PotionEffectType.BLINDNESS,
                        500, 1), false);
                potionMeta.addCustomEffect(new PotionEffect(
                        PotionEffectType.CONFUSION,
                        500, 1), false);
                potionMeta.addCustomEffect(new PotionEffect(
                        PotionEffectType.SLOW_DIGGING,
                        500, 1), false);
                potionMeta.setColor(Color.BLACK);
                potion.setItemMeta(potionMeta);
                flashBang.setItem(potion);
                return;
            default:
                for (TeamColor color : TeamColor.values()) {
                    if (material == color.getBannerMaterial()) {
                        new TeamGUI(player);
                        return;
                    }
                    if (material == color.getFlagMaterial()) {
                        event.setCancelled(true);
                        return;
                    }
                }
        }

        if (event.getClickedBlock() == null ||
                event.getClickedBlock().getType() == Material.AIR) {
            return;
        }
        switch (event.getClickedBlock().getType()) {
            case BEACON:
                event.setCancelled(true);
                break;
            case CAKE:
                if (player.getFoodLevel() < 20) {
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.REGENERATION,
                            100, 1));
                }
        }
    }

    @EventHandler
    void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        PlayerInventory inv = player.getInventory();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || player.getGameMode() ==
                GameMode.CREATIVE) {
            return;
        }
        Material material = itemStack.getType();
        String worldName = player.getWorld().getName();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        TeamColor playerColor = gamePlayer.getTeamColor();
        InventoryView openInv = player.getOpenInventory();

        if (itemStack.getType() == Material.BARRIER && itemStack.
                getItemMeta().getDisplayName().endsWith("Shop Selection")) {
            player.closeInventory();
            new ShopGUI(gamePlayer);
            return;
        }

        if (itemStack.getItemMeta() == null) {
            return;
        }

        // TODO: Fix not allowing people to put armor or tools in a chest.
//        String itemName = itemStack.getType().name();
//        if ((itemName.endsWith("HELMET") ||
//                itemName.endsWith("CHESTPLATE") ||
//                itemName.endsWith("LEGGINGS") ||
//                itemName.endsWith("BOOTS") ||
//                itemName.endsWith("SWORD") ||
//                itemName.endsWith("PICKAXE") ||
//                itemName.endsWith("AXE") || itemName.endsWith("BOW")) &&
//                event.getAction() ==
//                        InventoryAction.MOVE_TO_OTHER_INVENTORY) {
//            event.setCancelled(true);
//        }

        if (openInv.getTitle().endsWith("Armor Shop")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            String itemName = item.getType().name();
            int slot = event.getSlot();
            int x = slot / 9;
            int y = slot % 9;
            switch (y) {
                case 2:
                    y = 1;
                    break;
                case 4:
                    y = 2;
                    break;
                case 6:
                    y = 3;
                    break;
                case 8:
                    y = 4;
            }

            if (item == null || item.getItemMeta() == null) return;

            if (!meta.hasLore()) {
                equipArmor(item, inv, openInv, slot);
                return;
            }

            List<String> lore = meta.getLore();
            String line = lore.get(0).substring(3, lore.get(0).length() - 1);
            while (!line.startsWith(" ")) {
                line = line.substring(1);
            }

            // If the player has already purchased this.
            if (!(lore.get(0).endsWith("Ingots") ||
                    lore.get(0).endsWith("Diamonds"))) {
                equipArmor(item, inv, openInv, slot);
                return;
            }

            if (playerCanBuy(inv, itemStack)) {
                if (x == 5) {
                    gamePlayer.setArmorUnlocked(0, y);
                    gamePlayer.setArmorUnlocked(1, y);
                    gamePlayer.setArmorUnlocked(2, y);
                    gamePlayer.setArmorUnlocked(3, y);
                } else {
                    gamePlayer.setArmorUnlocked(x, y);
                }
                equipArmor(item, inv, openInv, slot);
            } else {
                player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
                return;

            }
            player.closeInventory();
            new ArmorShopGUI(gamePlayer);
        } else if (openInv.getTitle().endsWith("Tool / Weapon Shop")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            ItemMeta meta = item.getItemMeta();
            String itemName = item.getType().name();
            String endItemName = itemName.substring(itemName.length() - 3);
            int slot = event.getSlot();
            int x = (slot / 9) - 1;
            int y = slot % 9;
            switch (y) {
                case 2:
                    y = 0;
                    break;
                case 3:
                    y = 1;
                    break;
                case 5:
                    y = 2;
                    break;
                case 6:
                    y = 3;
            }

            if (!item.hasItemMeta() || !meta.hasLore()) {
                return;
            }

            List<String> lore = meta.getLore();
            String line = lore.get(0).substring(3, lore.get(0).length() - 1);
            while (!line.startsWith(" ")) {
                line = line.substring(1);
            }

            // If the player has already purchased this.
            if (!(lore.get(0).contains("Ingots") ||
                    lore.get(0).contains("Diamonds"))) {
                replaceItem(inv, item);
                player.closeInventory();
                new ToolShopGUI(gamePlayer);
                return;
            }
            // If the player hasn't already purchased this.
            if (playerCanBuy(inv, itemStack)) {
                gamePlayer.setToolUnlocked(x, y);
                replaceItem(inv, item);
            } else {
                player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
                return;
            }
            player.closeInventory();
            new ToolShopGUI(gamePlayer);
        } else if (openInv.getTitle().endsWith("Team Upgrade Shop")) {
            event.setCancelled(true);
            ItemMeta meta = itemStack.getItemMeta();
            if (itemStack == null || meta == null || !meta.hasLore()) return;
            List<String> lore = meta.getLore();
            String line = lore.get(0).substring(4, lore.get(0).length() - 1);
            Team team = gamePlayer.getTeam();
            int i = event.getSlot();
            boolean alreadyPurchased = line.contains("Diamonds") ||
                    line.contains("Ingots");
            if (i <= 12) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradeRespawnTime(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            } else if (i <= 16) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradeRegeneration(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            } else if (i <= 21) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradeArmorWeight(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            } else if (i <= 25) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradePressure(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            } else if (i <= 30) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradeKillRewards(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            } else if (i <= 34) {
                if (playerCanBuy(inv, itemStack) || alreadyPurchased)
                    team.upgradeRunForIt(itemStack.getAmount());
                else player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            }
            player.closeInventory();
            new UpgradeShopGUI(gamePlayer);
        } else if (openInv.getTitle().endsWith("Food Shop") ||
                openInv.getTitle().contains("Block Shop") ||
                openInv.getTitle().contains("Item Shop")) {
            event.setCancelled(true);
            ItemMeta meta = itemStack.getItemMeta();
            if (!meta.hasLore()) {
                return;
            }

            List<String> lore = meta.getLore();
            String line = lore.get(0).substring(3, lore.get(0).length() - 1);
            while (!line.startsWith(" ")) {
                line = line.substring(1);
            }

            if (itemStack.getType() == Material.WARPED_FUNGUS) {
                if (gamePlayer.getOneUpsBought() < 3) {
                    gamePlayer.oneUp();
                    player.sendMessage(ChatColor.GREEN + "1-UP! - " +
                            "You now have " + (gamePlayer.getLives() - 1) +
                            " extra live(s)!");
                } else {
                    player.sendMessage(ChatColor.RED +
                            " (!!!) You've already reached the maximum " +
                            "number of 1-UP's you can buy.");
                    return;
                }
            }

            if (playerCanBuy(inv, itemStack)) {
                if (itemStack.getType() == Material.WARPED_FUNGUS) {
                    return;
                }
                ItemStack newItem = itemStack.clone();
                ItemMeta newMeta = itemStack.getItemMeta();
                newMeta.setLore(Game.loreMaker());
                newItem.setItemMeta(newMeta);
                inv.addItem(newItem);
            } else {
                player.sendMessage(ChatColor.YELLOW +
                        "You cannot purchase that. " +
                        "You don't have enough" + line + "s.");
            }
        } else if (openInv.getTitle().endsWith("Selection")) {
            event.setCancelled(true);
            player.closeInventory();
            switch (itemStack.getType()) {
                case IRON_HELMET:
                    new ArmorShopGUI(gamePlayer);
                    break;
                case DIAMOND_SWORD:
                    new ToolShopGUI(gamePlayer);
                    break;
                case BEACON:
                    new UpgradeShopGUI(gamePlayer);
                    break;
                case COOKED_BEEF:
                    new FoodShopGUI(gamePlayer);
                    break;
                case COBBLESTONE:
                    new BlockShopGUI(gamePlayer);
                    break;
                case TURTLE_HELMET:
                    new ItemShopGUI(gamePlayer);
            }
        } else {
            // Checks if the player is trying to move the glass pane /
            // nether star / flag from their inventory.
            if (event.getSlot() == 4 && event.getClickedInventory()
                    instanceof PlayerInventory) {
                event.setCancelled(true);
                player.sendMessage(ChatColor.DARK_RED +
                        " (!!!) You cannot move this item!");
            }
        }
    }

    /**
     * Searches through the first line of lore in the given
     * itemToBuy. If it contains a phrase that fits the form
     * "<Number> <Material Name>", then that will be considered the
     * cost of the given itemToBuy. If the Player designated from the
     * given inv has the cost of the given itemToBuy, then return true.
     *
     * @param inv       is the PlayerInventory to search for the cost of
     *                  the given itemToBuy.
     * @param itemToBuy is the ItemStack that will be decided if the
     *                  Player designated from the given inv can buy.
     * @return whether or not the Player designated from the given inv
     * can buy the given itemToBuy or not.
     */
    boolean playerCanBuy(PlayerInventory inv, ItemStack itemToBuy) {
        String line = itemToBuy.getItemMeta().getLore().get(0).substring(2);
        Pattern p = Pattern.compile("(\\d*)");
        Matcher m = p.matcher(line);
        if (m.find()) {
            if (m.group().equals("")) return true;
            int cost = Integer.parseInt(m.group());
            char[] charArray = line.toCharArray();
            int i = 0;
            for (; i < charArray.length; i++)
                if (charArray[i] == ' ') break;
            if (line.endsWith("s")) {
                line = line.substring(i, line.length() - 1);
            } else {
                line = line.substring(i);
            }
            StringBuilder materialCostName = new StringBuilder();
            for (int j = 1; j < line.toCharArray().length; j++) {
                char c = line.charAt(j);
                if (c == ' ') {
                    materialCostName.append("_");
                } else {
                    materialCostName.append(
                            Character.toUpperCase(c));
                }
            }
            Material costMaterial = Material.getMaterial(
                    materialCostName.toString());
            if (!inv.contains(costMaterial, cost)) return false;
            int totalMaterial = 0;
            for (ItemStack inventoryItem : inv.getContents()) {
                if (inventoryItem != null &&
                        inventoryItem.getType() ==
                                costMaterial) {
                    totalMaterial += inventoryItem.getAmount();
                }
            }
            inv.remove(costMaterial);
            inv.addItem(new ItemStack(costMaterial,
                    totalMaterial - cost));
            PlayerData.getPlayerData(
                    (Player) inv.getHolder()).purchasesMade++;
            return true;
        }
        return false;
    }

    /**
     * Searches through the given inv and finds the ItemStack
     * that is similar (shares the last 3 letters of the Material
     * name) with the given itemToAdd and replaces it with the
     * given itemToAdd.
     *
     * @param inv       is the PlayerInventory that is searched.
     * @param itemToAdd is the ItemStack that will replace the ItemStack
     *                  similar (shares the last 3 letters of the
     *                  Material name) with the given endItemName.
     */
    void replaceItem(PlayerInventory inv, ItemStack itemToAdd) {
        String itemName = itemToAdd.getType().name();
        String endItemName = itemName.substring(itemName.length() - 3);
        if (itemToAdd.getType() != Material.BOW) endItemName =
                itemName.substring(itemName.length() - 4);
        if (inv.firstEmpty() != -1) {
            removeLore(itemToAdd);
            ItemStack[] armorContents = inv.getArmorContents();
            for (int i = 0; i < armorContents.length; i++) {
                if (armorContents[i] == null) continue;
                if (armorContents[i].getType().name().endsWith(
                        endItemName)) {
                    armorContents[i] = itemToAdd;
                    inv.setArmorContents(armorContents);
                    return;
                }
            }
            ItemStack[] extraContents = inv.getExtraContents();
            for (int i = 0; i < extraContents.length; i++) {
                if (extraContents[i] == null) continue;
                if (extraContents[i].getType().name().endsWith(
                        endItemName)) {
                    extraContents[i] = itemToAdd;
                    inv.setArmorContents(extraContents);
                    return;
                }
            }
            for (int i = 0; i < inv.getContents().length; i++) {
                if (inv.getContents()[i] == null) continue;
                if (inv.getItem(i).getType().name().endsWith(endItemName)) {
                    inv.remove(inv.getItem(i));
                    inv.addItem(itemToAdd);
                    return;
                }
            }
        }
    }

    /**
     * Removes all lore from the given item.
     *
     * @param item is the ItemStack that has it's lore stripped from it.
     */
    void removeLore(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(Game.loreMaker());
        item.setItemMeta(itemMeta);
    }

    /**
     * Equips armor from the armor shop, based upon what was
     * clicked.
     *
     * @param inv     is the PlayerInventory that the armor is
     *                equipped to.
     * @param openInv is the armor shop Inventory that the Player
     *                has opened.
     * @param slot    is the slot that the Player clicked.
     */
    void equipArmor(ItemStack item, PlayerInventory inv,
                    InventoryView openInv, int slot) {
        if ((slot / 9) == 5) {
            ItemStack[] armor = new ItemStack[]{
                    openInv.getItem(slot - 18),
                    openInv.getItem(slot - 27),
                    openInv.getItem(slot - 36),
                    openInv.getItem(slot - 45)};
            Arrays.stream(armor).forEach(this::removeLore);
            replaceItem(inv, armor[3]);
            replaceItem(inv, armor[2]);
            replaceItem(inv, armor[1]);
            replaceItem(inv, armor[0]);
        } else {
            removeLore(item);
            replaceItem(inv, item);
        }
    }

    @EventHandler
    void onShootProjectile(ProjectileLaunchEvent event) {
        // TODO: Make it so that when a projectile is fired,
        //  the projectile stores metadata of the shooter's name.
        //  This will be used when another player takes damage, so
        //  that the plugin will know when someone is killed by
        //  someone else.
        event.getEntity().setMetadata("",
                new FixedMetadataValue(Game.plugin,
                        event.getEntity().getShooter()));
    }

    @EventHandler
    void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        switch (projectile.getType()) {
            case SPECTRAL_ARROW:
                if (event.getHitBlock() != null) {
                    Bukkit.getWorld(Game.gameWorldName).createExplosion(
                            event.getHitBlock().getLocation(), 2,
                            false, false);
                    projectile.remove();
                } else if (event.getHitEntity() != null) {
                    Bukkit.getWorld(Game.gameWorldName).createExplosion(
                            event.getHitEntity().getLocation(), 2,
                            false, false);
                    projectile.remove();
                }
                break;
            case ARROW:
                if (event.getHitEntity() == null) return;
                if ((((Arrow) (projectile)).hasCustomEffect(
                        PotionEffectType.WEAKNESS))) {
                    event.getHitEntity().setFireTicks(60);
                    projectile.remove();
                } else if (((Arrow) (projectile)).hasCustomEffect(
                        PotionEffectType.SLOW)) {
                    Game.addPotionEffect((Player) event.getHitEntity(),
                            new PotionEffect(PotionEffectType.JUMP,
                                    60, 128));
                    Game.addPotionEffect((Player) event.getHitEntity(),
                            new PotionEffect(PotionEffectType.SLOW,
                                    60, 128));
                    projectile.remove();
                }
                break;
            case SNOWBALL:
                Location loc;
                if (event.getHitBlock() != null)
                    loc = event.getHitBlock().getLocation();
                else if (event.getHitEntity() != null)
                    loc = event.getHitEntity().getLocation();
                else return;
                loc = loc.add(0, 2, 0);
                int numBees = ((int) (Math.random() * 4) + 1) + 2;
                Bee bee;
                for (int i = 0; i < numBees; i++) {
                    bee = Game.server.getWorld(Game.gameWorldName).
                            spawn(loc, Bee.class);
                    bee.setAnger(10000);
                }
                break;
            case SPLASH_POTION:
                World world = Game.server.getWorld(Game.gameWorldName);
                if (event.getHitBlock() != null)
                    loc = event.getHitBlock().getLocation();
                else if (event.getHitEntity() != null)
                    loc = event.getHitEntity().getLocation();
                else return;
                boolean isFlashBang = false;
                for (PotionEffect effect : ((ThrownPotion)
                        event.getEntity()).getEffects()) {
                    if (effect.getType() == PotionEffectType.SLOW_DIGGING) {
                        isFlashBang = true;
                        break;
                    }
                }
                if (isFlashBang) {
                    world.createExplosion(loc, 2.0f,
                            false, false);
                    world.spawnParticle(
                            Particle.SMOKE_LARGE,
                            loc, 25);
                } else {
                    world.spawnParticle(
                            Particle.CAMPFIRE_COSY_SMOKE,
                            loc, 100);
                }
        }
    }

    @EventHandler
    void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.VOID) {
            GamePlayer gamePlayer = Game.getGamePlayer(player);
            Team playersTeam = gamePlayer.getTeam();
            checkTeams(gamePlayer);
            if (Game.gameGoingAlready && playersTeam.isArmorStandAlive()) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                Game.changeWorld(player, Game.lobbyWorldName);
            }
        } else if (cause == EntityDamageEvent.DamageCause.LIGHTNING) {
            switch (Utility.gamemode) {
                case NORMAL:
                    event.setCancelled(true);
                    break;
                case SMITE:
                    // TODO: Add the Smite Gamemode here later.
                    break;
            }
        }
    }

    @EventHandler
    void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        GamePlayer gamePlayer = Game.getGamePlayer(player);

        if (damager instanceof Player) {
            // Stops Friendly Fire
            GamePlayer gamePlayerDamager = Game.getGamePlayer(
                    (Player) damager);
            if (gamePlayer.getTeam() == gamePlayerDamager.getTeam()) {
                event.setCancelled(true);
                return;
            }
            gamePlayer.setLastDamager((Player) damager);
        } else if (damager.hasMetadata("damage")) {
            String metadataPlayerName = String.valueOf(damager.getMetadata(
                    "damage").get(0));
            System.out.println("Damage Metadata: " + metadataPlayerName);
            for (Team team : Game.gameTeams)
                for (GamePlayer teamGamePlayer : team)
                    if (teamGamePlayer.getPlayer().getName().
                            equals(metadataPlayerName)) {
                        gamePlayer.setLastDamager(
                                teamGamePlayer.getPlayer());
                        return;
                    }
        }
    }

    @EventHandler
    void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        PlayerInventory inv = player.getInventory();
        Team playersTeam = gamePlayer.getTeam();

        if (playersTeam == null) {
            Game.server.broadcastMessage(ChatColor.DARK_RED +
                    "Someone wasn't on a team for some reason. " +
                    "Contact the creator of the plugin.");
            return;
        }

        checkTeams(gamePlayer);
        if (Game.gameGoingAlready && playersTeam.isArmorStandAlive()) {
            player.setGameMode(GameMode.SPECTATOR);
        } else if (Game.gameGoingAlready) {
            player.setGameMode(GameMode.SPECTATOR);
        }

        List<ItemStack> drops = event.getDrops();
        for (ItemStack item : inv.getContents()) {
            if (item == null) continue;
            String itemName = item.getType().name();
            if (itemName.endsWith("SWORD") || itemName.endsWith("PICKAXE") ||
                    itemName.endsWith("AXE") || itemName.endsWith("BOW") ||
                    itemName.endsWith("HELMET") ||
                    itemName.endsWith("CHESTPLATE") ||
                    itemName.endsWith("LEGGINGS") ||
                    itemName.endsWith("BOOTS")) {
                drops.remove(item);
            }
        }
        drops.remove(inv.getItem(4));

        if (player.getKiller() != null) {
            Player killer = player.getKiller();
            gamePlayer.setLastDamager(killer);
            GamePlayer gamePlayerKiller = Game.getGamePlayer(killer);
            Team team = gamePlayerKiller.getTeam();
            switch (team.getKillRewardsUpgrade()) {
                case 1:
                    Game.addPotionEffect(killer, new PotionEffect(
                            PotionEffectType.SPEED, 200, 0));
                    break;
                case 2:
                    Game.addPotionEffect(killer, new PotionEffect(
                            PotionEffectType.INCREASE_DAMAGE,
                            200, 0));
                    break;
                case 3:
                    Game.addPotionEffect(killer, new PotionEffect(
                            PotionEffectType.REGENERATION,
                            200, 0));
            }
        }
    }

    @EventHandler
    void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        Team team = gamePlayer.getTeam();
        switch (event.getNewGameMode()) {
            case SURVIVAL:
                if (Game.gameGoingAlready) {
                    player.teleport(team.getSpawnPoint());
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    setGameInv(player, Utility.gamemode);
                }
                break;
            case ADVENTURE:
                player.setHealth(20);
                player.setFoodLevel(20);
                setLobbyInv(player);
                break;
            case SPECTATOR:
                World world = player.getWorld();
                if (!world.getName().equalsIgnoreCase(Game.gameWorldName))
                    Game.changeWorld(player, Game.gameWorldName);
                PlayerInventory inv = player.getInventory();
                gamePlayer.resetArmorUnlocked();
                gamePlayer.resetToolsUnlocked();


                if (!Game.gameGoingAlready || !team.isArmorStandAlive())
                    player.setGameMode(GameMode.ADVENTURE);

                if (gamePlayer.getFlagOrigin() != null) {
                    EntityEquipment armorStand = gamePlayer.
                            getFlagOrigin().getEquipment();
                    ItemStack flag = inv.getItem(4);
                    if (flag != null) {
                        String flagName = flag.getType().name();
                        switch (flagName.substring(flagName.length() - 2)) {
                            case "ET":
                                armorStand.setHelmet(flag);
                                break;
                            case "TE":
                                armorStand.setChestplate(flag);
                                break;
                            case "GS":
                                armorStand.setLeggings(flag);
                                break;
                            case "TS":
                                armorStand.setBoots(flag);
                        }
                    }
                    inv.remove(flag);
                }

                // TODO: Check if this works.
                System.out.println((System.currentTimeMillis() -
                        gamePlayer.getTimeLastDamaged()) +
                        " ms since " + player + " died.");
                if (System.currentTimeMillis() -
                        gamePlayer.getTimeLastDamaged() < 10000) {
                    Player damager = gamePlayer.getLastDamager();
                    GamePlayer gamePlayerDamager =
                            Game.getGamePlayer(damager);
                    Team damagerTeam = gamePlayerDamager.getTeam();
                    if (gamePlayer.getTeamColor() ==
                            damagerTeam.getColor()) {
                        player.teleport(world.getSpawnLocation());
                        return;
                    }
                    PlayerData.getPlayerData(damager).addKill();
                    PlayerData.getPlayerData(player).addDeath();
                    switch (team.getKillRewardsUpgrade()) {
                        case 1:
                            Game.addPotionEffect(damager, new PotionEffect(
                                    PotionEffectType.SPEED,
                                    200, 0));
                            break;
                        case 2:
                            Game.addPotionEffect(damager, new PotionEffect(
                                    PotionEffectType.INCREASE_DAMAGE,
                                    200, 0));
                            break;
                        case 3:
                            Game.addPotionEffect(damager, new PotionEffect(
                                    PotionEffectType.REGENERATION,
                                    200, 0));
                    }
                }

                player.teleport(world.getSpawnLocation());
        }
    }

    /**
     * Checks all of the Teams, and sees which are alive. If there
     * are more than one, continue the game, otherwise, end the game.
     *
     * @param gamePlayer is the GamePlayer that has been killed,
     *                   thus provoking the update.
     */
    void checkTeams(GamePlayer gamePlayer) {
        Player player = gamePlayer.getPlayer();
        gamePlayer.die();
        System.out.println(gamePlayer.getTeam().getArmorStand() +
                " | " + gamePlayer.getLives());
        if (player.getWorld().getName().equalsIgnoreCase(
                Game.gameWorldName)) {
            Team lastTeamAlive = null;
            int teamsAlive = 0;
            for (Team gameTeam : Game.gameTeams) {
                if (gameTeam.isAlive()) {
                    lastTeamAlive = gameTeam;
                    teamsAlive++;
                }
            }

            if (lastTeamAlive != null && teamsAlive == 1) {
                Game.end(lastTeamAlive);
                return;
            }

            if (gamePlayer.getLives() != 0) {
                BukkitTask task = new Countdown(player,
                        gamePlayer.getTeam().getRespawnTime()).
                        runTaskTimer(Game.plugin, 0, 20);
            }
        }

    }

    @EventHandler
    void onItemMerge(ItemMergeEvent event) {
        ItemStack item = event.getTarget().getItemStack();
        switch (item.getType()) {
            case IRON_NUGGET:
                if (item.getAmount() >= 100) {
                    item.setAmount(99);
                }
                break;
            case GOLD_NUGGET:
                if (item.getAmount() >= 80) {
                    item.setAmount(79);
                }
                break;
            case IRON_INGOT:
                if (item.getAmount() >= 25) {
                    item.setAmount(24);
                }
                break;
            case GOLD_INGOT:
                if (item.getAmount() >= 20) {
                    item.setAmount(19);
                }
                break;
            case QUARTZ:
                if (item.getAmount() >= 12) {
                    item.setAmount(11);
                }
        }
    }

    @EventHandler
    void onItemPickup(EntityPickupItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();
        Material material = item.getItemStack().getType();
        Player player = (Player) entity;
        PlayerInventory inv = player.getInventory();
        switch (material) {
            case IRON_NUGGET:
                condense(inv, item, Material.IRON_INGOT, event);
                break;
            case GOLD_NUGGET:
                condense(inv, item, Material.GOLD_INGOT, event);
                break;
            case NETHERITE_SCRAP:
                condense(inv, item, Material.NETHERITE_INGOT, event);
                break;
            case NETHER_STAR:
                // Check if player has a banner in their inventory. If it is,
                // return; else replace the glass_pane with the netherstar.
                for (TeamColor color : TeamColor.values()) {
                    if (inv.contains(color.getFlagMaterial())) {
                        inv.remove(color.getFlagMaterial());
                        inv.setItem(4,
                                new ItemStack(Material.NETHER_STAR));
                        if (item.getItemStack().getAmount() == 1) {
                            item.remove();
                        } else {
                            item.setItemStack(new ItemStack(
                                    itemStack.getType(),
                                    itemStack.getAmount() - 1));
                        }
                        event.setCancelled(true);
                        return;
                    }
                }
                event.setCancelled(true);
        }
    }

    /**
     * Condenses the given originalMaterial into the given
     * condensedMaterial using a 4:1 ratio.
     *
     * @param inv               is the PlayerInventory that is searched
     *                          for the originalMaterial.
     * @param originalItem      is the Item that is used to get the
     *                          ItemStack / Material to condense.
     * @param condensedMaterial is the Material that the originalMaterial
     *                          is condensed into.
     */
    void condense(PlayerInventory inv, Item originalItem, Material
            condensedMaterial, EntityPickupItemEvent event) {
        ItemStack originalItemStack = originalItem.getItemStack();
        Material originalMaterial = originalItemStack.getType();
        int numOldMaterial = originalItemStack.getAmount();
        int numCondensedMaterial;
        for (int i = 0; i < inv.getContents().length; i++) {
            ItemStack itemStack = inv.getItem(i);
            if (itemStack != null &&
                    itemStack.getType() == originalMaterial) {
                numOldMaterial += itemStack.getAmount();
            }
        }
        inv.remove(originalMaterial);
        numCondensedMaterial = numOldMaterial / 4;
        numOldMaterial %= 4;
        inv.addItem(new ItemStack(originalMaterial, numOldMaterial),
                new ItemStack(condensedMaterial, numCondensedMaterial));
        event.setCancelled(true);
        originalItem.remove();
    }

    @EventHandler
    void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        PlayerInventory inv = player.getInventory();
        ItemStack item = event.getArmorStandItem();

        for (Team team : Game.gameTeams)
            if (inv.contains(team.getColor().getFlagMaterial())) {
                inv.setItem(4, item);
                item.setAmount(0);
                gamePlayer.setFlagOrigin(event.getRightClicked());
                team.copyArmorStand();
                if (team.getRunForItUpgrade() != 0)
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.SPEED,
                            400,
                            team.getRunForItUpgrade() - 1));
                PlayerData.getPlayerData(player).flagsStolen++;
                break;
            }
        World world = Game.server.getWorld(Game.gameWorldName);
        Block blockBelow = world.getBlockAt(event.getRightClicked().
                getLocation().add(0, -1, 0));
        for (Team team : Game.gameTeams)
            if (blockBelow.getType() ==
                    team.getColor().getNoTeammatesBannerMaterial()) {
                for (GamePlayer teamPlayer : team)
                    PlayerData.getPlayerData(
                            teamPlayer.getPlayer()).yourFlagsStolen++;
                if (team.getPressureUpgrade() != 0)
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.WEAKNESS,
                            400,
                            team.getPressureUpgrade() - 1));
                if (team.getArmorWeightUpgrade() != 0)
                    gamePlayer.setArmorWeightTask(
                            team.getArmorWeightUpgrade());
                return;
            }
        event.setCancelled(true);
    }

    @EventHandler
    void onClickAtEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inv = player.getInventory();
        Entity entity = event.getRightClicked();
        if (entity instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) entity;
            GamePlayer gamePlayer = Game.getGamePlayer(player);

            Team team = gamePlayer.getTeam();
            if (team == null) return;
            ArmorStand teamArmorStand = team.getArmorStand();
            if (teamArmorStand == null) return;
            ItemStack itemInHand = inv.getItemInMainHand();
            // If the clicked Armor Stand is your team's Armor Stand
            if (teamArmorStand.equals(armorStand)) {
                if (itemInHand.getType() == Material.NETHER_STAR) {
                    inv.remove(Material.NETHER_STAR);
                    inv.setItem(4, getFlagPlaceholder(team));
                    if (inv.firstEmpty() == -1) {
                        player.getWorld().dropItem(player.getLocation(),
                                new ItemStack(Material.NETHERITE_INGOT,
                                        10));
                    } else {
                        inv.addItem(new ItemStack(
                                Material.NETHERITE_INGOT, 10));
                    }
                    PlayerData.getPlayerData(player).netherStarCashedIn++;
                } else if (itemInHand.getType().name().
                        endsWith("HELMET") || itemInHand.getType().name().
                        endsWith("CHESTPLATE") || itemInHand.getType().
                        name().endsWith("LEGGINGS") || itemInHand.getType()
                        .name().endsWith("BOOTS")) {
                    if (!inv.getItem(4).equals(itemInHand)) return;
                    inv.remove(itemInHand);
                    inv.setItem(4, getFlagPlaceholder(team));
                    if (armorStand.getEquipment() != null) {
                        ItemStack[] items = armorStand.getEquipment().
                                getArmorContents();
                        for (ItemStack item : items) {
                            Material material = item.getType();
                            if (material != Material.AIR &&
                                    material != itemInHand.getType()) {
                                return;
                            }
                        }
                        LeatherArmorMeta meta = (LeatherArmorMeta)
                                itemInHand.getItemMeta();
                        assert meta != null;
                        TeamColor teamColor = null;
                        Team flagTeam = null;
                        for (Team gameTeam : Game.gameTeams) {
                            if (gameTeam.getColor() == TeamColor.NULL)
                                continue;
                            if (meta.getColor().toString().equals(
                                    gameTeam.getColor().getArmorColor().
                                            toString())) {
                                teamColor = gameTeam.getColor();
                                flagTeam = gameTeam;
                                break;
                            }
                        }
                        if (teamColor == null) return;
                        ArmorStand opposingArmorStand =
                                flagTeam.getArmorStand();
                        ItemStack[] stand = flagTeam.copyArmorStand();
                        boolean hasAFlag = false;
                        for (ItemStack item : stand)
                            if (item.getType() != Material.AIR) {
                                hasAFlag = true;
                                break;
                            }
                        if (!hasAFlag) {
                            Game.server.broadcastMessage(
                                    ChatColor.DARK_RED + "(!!!) " +
                                            teamColor +
                                            " team was eliminated!");
                            Location loc = opposingArmorStand.getLocation();
                            Objects.requireNonNull(Game.server.getWorld(
                                    Game.gameWorldName)).createExplosion(
                                    loc, 3.0f,
                                    false, false);
                            flagTeam.setArmorStand(null);
                            PlayerData.getPlayerData(player).
                                    teamsEliminated++;
                        }
                        if (team.isArmorStandAlive()) team.addFlag();
                    }
                } else {
                    new ShopGUI(gamePlayer);
                    event.setCancelled(true);
                }
            } else if (itemInHand.getType() != Material.AIR) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getItemInHand().getType() == Material.BEE_NEST) {
            event.setCancelled(false);
            return;
        }
        if (player.getGameMode() != GameMode.CREATIVE) {
            Location blockLocation = event.getBlockPlaced().getLocation();
            for (Entity entity : player.getNearbyEntities(
                    3, 3, 3)) {
                if (!(entity instanceof ArmorStand)) continue;
                if (entity.getLocation().distance(blockLocation) < 2) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.DARK_RED + " (!!!) " +
                            "You cannot place blocks that close to a " +
                            "team's Armor Stand.");
                    return;
                }
            }
            for (int i = -1; i < 1; i++)
                for (int j = -1; j < 1; j++)
                    for (int k = -1; k < 1; k++) {
                        Block blockPlacedNear = Bukkit.getWorld(
                                Game.gameWorldName).getBlockAt(
                                blockLocation.getBlockX() + i,
                                blockLocation.getBlockY() + j,
                                blockLocation.getBlockZ() + k);
                        if (blockPlacedNear.getType() == Material.BEDROCK ||
                                blockPlacedNear.getType() ==
                                        Material.BEACON) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.DARK_RED +
                                    " (!!!) You cannot place blocks that " +
                                    "close to a generator.");
                            return;
                        }
                    }
            event.getBlockPlaced().setMetadata("block",
                    new FixedMetadataValue(Game.plugin, ""));
            PantyRaid.Utility.blocks.add(event.getBlockPlaced());
        }
    }

    @EventHandler
    void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() != GameMode.CREATIVE &&
                !(event.getBlock().hasMetadata("block"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    void onExplosionBlockBreak(BlockExplodeEvent event) {
        event.setCancelled(true);
        for (Block block : event.blockList())
            if (block.hasMetadata("block"))
                block.breakNaturally();
    }

    @EventHandler
    void onCraft(CraftItemEvent event) {
        event.setCancelled(true);
        event.getWhoClicked().sendMessage(ChatColor.RED +
                " (!!!) Crafting is not allowed in this game!");
    }

    @EventHandler
    void onMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        TeamColor teamColor = gamePlayer.getTeamColor();
        // TODO: Add Tags/Ranks in here later.
        if (teamColor == TeamColor.NULL) {
            event.setFormat(" [No Team] " + player.getDisplayName()
                    + " " + ChatColor.WHITE + event.getMessage());
        } else {
            event.setFormat(teamColor.getTextColor() + " [" +
                    teamColor.getTeamName() + "] " + player.getDisplayName()
                    + " " + ChatColor.WHITE + event.getMessage());
        }
        // TODO: Replace all instances of % signs in the message with
        //  the word 'percent'. %1$s and %2$s might still break this
        //  and might need to be caught separately as well.
    }

    @EventHandler
    void onEggThrow(PlayerEggThrowEvent event) {
        event.setHatchingType(EntityType.CREEPER);
        if (event.isHatching() && Math.random() * 100 > 99) {
            Bukkit.getWorld(Game.gameWorldName).strikeLightning(
                    event.getEgg().getLocation());
        }
    }

    @EventHandler
    void onEat(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        if (event.getItem().getType() == Material.GOLDEN_CARROT) {
            Game.addPotionEffect(player, new PotionEffect(
                    PotionEffectType.NIGHT_VISION, 400, 1));
        }
    }

    @EventHandler
    void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        World world = Game.server.getWorld(Game.gameWorldName);
        for (double i = -1; i < 0.5; i += 0.5) {
            Block blockBelow = world.getBlockAt(event.getPlayer().
                    getLocation().add(0, i, 0));
            switch (blockBelow.getType()) {
                case SPRUCE_TRAPDOOR:
                    player.damage(2);
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.JUMP, 60, 128));
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.SLOW, 60, 128));
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.BLINDNESS,
                            20, 1));
                    blockBelow.setType(Material.AIR);
                    return;
                case IRON_TRAPDOOR:
                    player.damage(4);
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.JUMP, 120, 128));
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.SLOW, 120, 128));
                    Game.addPotionEffect(player, new PotionEffect(
                            PotionEffectType.BLINDNESS,
                            40, 1));
                    blockBelow.setType(Material.AIR);
                    return;
            }
        }
    }
}
