package General;

import GUIs.GameGUI;
import GUIs.TeamGUI;
import GUIs.VoteGUI;
import Items.NamedItem;
import Team.Team;
import Team.TeamColor;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Listeners implements Listener {
    Listeners(Plugin plugin) {
        System.out.println("\u001B[32mEnabling \u001B[35mPandamonium" +
                "\u001B[36mGames \u001B[32mListeners! \u001B[0m");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        boolean playerIsInGame = false;
        for (Team team : Game.gameTeams)
            if (team.contains(gamePlayer)) {
                playerIsInGame = true;
                break;
            }
        if (!world.getName().equalsIgnoreCase(Game.lobbyWorldName) &&
                !playerIsInGame) {
            Game.changeWorld(player, Game.lobbyWorldName);
            setLobbyInv(player);
            if (Game.minigame.isTeamBased()) player.sendMessage(
                    ChatColor.GOLD + "When you are ready to begin the " +
                            "game, fire your Firework Rocket. You may " +
                            "select a team with the banner. If you do not " +
                            "select a team, one will be selected for you.");
            else player.sendMessage(ChatColor.GOLD + "When you are ready " +
                    "to begin the game, fire your Firework Rocket.");
        }
    }

    @EventHandler
    void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        for (Team team : Game.gameTeams)
            if (!Game.gameGoingAlready) team.remove(gamePlayer);
        Game.whichPlayersToPlayWith().remove(gamePlayer);
    }

    @EventHandler
    void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (player.getWorld().getName().equalsIgnoreCase(
                Game.lobbyWorldName)) event.setCancelled(true);
    }

    /**
     * Sets the inventory of the given player for the lobby world.
     *
     * @param player is the Player that has their inventory set.
     */
    public static void setLobbyInv(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        if (Game.minigame.isTeamBased()) inv.setItem(1,
                new NamedItem(chooseTeamItem(player), ChatColor.GREEN +
                        "Choose Team").getItemStack());
        else inv.setItem(1, new NamedItem(Material.NETHERITE_HOE,
                ChatColor.GREEN + "Not a Useless Item").getItemStack());
        inv.setItem(3, new NamedItem(Material.BOOK, ChatColor.
                RED + "Vote on the Game, Gamemode, or Map").getItemStack());
        inv.setItem(4, new NamedItem(Material.FIREWORK_ROCKET,
                ChatColor.GOLD + "Fire when Ready!").getItemStack());
        inv.setItem(5, new NamedItem(Material.ENCHANTED_BOOK,
                ChatColor.DARK_GREEN + "View your Player Data").
                getItemStack());
        inv.setItem(7, new NamedItem(Material.NAME_TAG,
                ChatColor.DARK_AQUA + "Tags").getItemStack());
    }

    /**
     * Checks the given player's Team. If the Player is on a Team,
     * return the banner for the Team's color, otherwise just return
     * a white banner.
     *
     * @param player is the Player that is checked.
     * @return the banner of the Team that the Player is on, if it is on one.
     */
    private static Material chooseTeamItem(Player player) {
        return Game.getGamePlayer(player).getTeamColor().getBannerMaterial();
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
        if (player.getGameMode() == GameMode.CREATIVE) return;
        if (player.getWorld().getName().equalsIgnoreCase(
                Game.lobbyWorldName)) {
            setLobbyInv(player);
            event.setCancelled(true);
        }

        GamePlayer gamePlayer = Game.getGamePlayer(player);
        PlayerInventory inv = player.getInventory();
        Material material = event.getMaterial();
        String worldName = player.getWorld().getName();

        switch (material) {
            case BOOK:
                new VoteGUI(player);
                break;
            case ENCHANTED_BOOK:
                // TODO: Add PlayerData GUI's for other Minigames.
                switch (Game.minigame) {
                    case HIDEANDSEEK:
                        break;
                    default:
                        new PantyRaid.PlayerDataGUI(gamePlayer);
                        break;
                }
                break;
            case NAME_TAG:
                // TODO: Equip a Tag method
                player.sendMessage(ChatColor.AQUA + "Tags - WIP");
                break;
            case FIREWORK_ROCKET:
                if (gamePlayer.isNotReady()) {
                    gamePlayer.setReady(true);
                    Bukkit.broadcastMessage(ChatColor.AQUA + " (*) " +
                            player.getName() + " is ready to begin!");
                    Game.checkPlayers(false);
                }
                break;
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
    }

    @EventHandler
    void onInvClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null ||
                player.getGameMode() == GameMode.CREATIVE) return;
        Material material = itemStack.getType();
        String worldName = player.getWorld().getName();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        TeamColor playerColor = gamePlayer.getTeamColor();

        if (itemStack.getItemMeta() == null) return;

        if (worldName.equalsIgnoreCase(Game.lobbyWorldName)) {
            event.setCancelled(true);
            if (player.getOpenInventory().getTitle().endsWith("Team GUI")) {
                // Checks if the Player clicked on their current Team.
                if ((material == playerColor.getBannerMaterial() ||
                        material == playerColor.
                                getNoTeammatesBannerMaterial())) {
                    player.sendMessage(ChatColor.RED + " (!!!) " +
                            "You're already on the " + playerColor +
                            ChatColor.RED + " Team.");
                    return;
                }

                for (Team team : Game.lobbyTeams) {
                    TeamColor color = team.getColor();
                    if ((material == color.getBannerMaterial() ||
                            material == color.
                                    getNoTeammatesBannerMaterial())) {
                        // Removes the Player from their previous Team.
                        if (gamePlayer.getTeam() != null) {
                            Team currentTeam = gamePlayer.getTeam();
                            if (currentTeam.remove(gamePlayer)) {
                                player.sendMessage(ChatColor.DARK_RED +
                                        " (-) Removed you from the " +
                                        playerColor + ChatColor.DARK_RED +
                                        " Team.");
                            }
                        }
                        // Adds the Player to the Team of the clicked banner.
                        team.add(gamePlayer);
                        gamePlayer.setTeam(team);
                        player.closeInventory();
                        new TeamGUI(player);
                        break;
                    }
                }
            } else if (player.getOpenInventory().getTitle().
                    endsWith("Vote GUI")) {
                switch (itemStack.getType()) {
                    case ARMOR_STAND:
                        player.closeInventory();
                        new GameGUI(player);
                        break;
                    case ZOMBIE_HEAD:
                        // TODO: Add the GameMode GUI here.
                        break;
                    case MAP:
                        // TODO: Add the Map GUI here.
                }
            } else if (player.getOpenInventory().getTitle().
                    endsWith("Game GUI")) {
                player.closeInventory();
                for (MiniGame miniGame : MiniGame.values()) {
                    if (miniGame.getDisplayName().equals(
                            itemStack.getItemMeta().getDisplayName())) {
                        Game.voteOnMiniGame(player, miniGame);
                        int voters = Game.gameVotes.size();
                        int totalPlayers = Game.server.
                                getOnlinePlayers().size();
                        if (voters == totalPlayers) {
                            Game.chooseGame();
                            return;
                        }
                        Game.server.broadcastMessage(ChatColor.GOLD +
                                " (!!!) " + voters + " / " + totalPlayers +
                                " have voted. All players must vote for " +
                                "the game to change.");
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase(
                Game.lobbyWorldName)) event.setCancelled(true);
    }

    @EventHandler
    void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (!(entity instanceof Player)) {
            return;
        }
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
            for (Team team : Game.gameTeams) {
                for (GamePlayer teamGamePlayer : team) {
                    if (teamGamePlayer.getPlayer().getName().
                            equals(metadataPlayerName)) {
                        gamePlayer.setLastDamager(
                                teamGamePlayer.getPlayer());
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    void onMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) {
            event.setCancelled(true);
            return;
        }
        if (!Game.minigame.isTeamBased()) return;

        GamePlayer gamePlayer = Game.getGamePlayer(player);
        TeamColor teamColor = gamePlayer.getTeamColor();
        // TODO: Add Tags/Ranks in here later.
        if (teamColor == TeamColor.NULL) event.setFormat(" [No Team] " +
                player.getDisplayName() + " " + ChatColor.WHITE +
                event.getMessage());
        else event.setFormat(teamColor.getTextColor() + " [" + teamColor.
                getTeamName() + "] " + player.getDisplayName() + " " +
                ChatColor.WHITE + event.getMessage());
        // TODO: Replace all instances of % signs in the message with
        //  the word 'percent'. %1$s and %2$s might still break this
        //  and might need to be caught separately as well.
    }
}
