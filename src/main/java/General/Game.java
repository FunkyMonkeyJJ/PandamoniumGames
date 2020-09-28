package General;


import HideAndSeek.Utility;
import PantyRaid.PlayerData;
import Team.Team;
import Team.TeamColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.Entry;

import static org.bukkit.ChatColor.*;

public class Game extends JavaPlugin {
    public static final int MIN_GAME_SIZE = 1;
    public static final int MAX_GAME_SIZE = 8;

    public static Server server;
    public static Plugin plugin;
    public static BukkitScheduler scheduler;
    public static ScoreboardManager scoreboardManager;
    private static int startCountdownId;

    public static String lobbyWorldName = "Lobby";
    public static String gameWorldName = "pantyraid-classic";
    public static MiniGame minigame = MiniGame.PANTYRAID;
    public static PandamoniumGame pandamoniumGame;
    public static boolean gameGoingAlready = false;
    public static boolean gameStarting = false;

    public static LinkedList<GamePlayer> lobbyPlayers = new LinkedList<>();
    public static LinkedList<Team> lobbyTeams = new LinkedList<>();
    public static LinkedList<Team> gameTeams = new LinkedList<>();
    // TODO: Remove these teams and all instances of teams in Game.
    //  Move all methods that deal with PantyRaid/Team specific things
    //  to PantyRaid/Other Team-based MiniGames Utility Classes.

    public static HashMap<Player, MiniGame> gameVotes = new HashMap<>();

    @Override
    public void onEnable() {
        server = this.getServer();
        plugin = this;
        scheduler = server.getScheduler();
        scoreboardManager = server.getScoreboardManager();
        chooseMiniGame();
        new Listeners(this);
        resetTeams();
        for (Player player : server.getOnlinePlayers())
            if (!player.getWorld().getName().
                    equalsIgnoreCase(lobbyWorldName))
                changeWorld(player, lobbyWorldName);
    }

    @Override
    public void onDisable() {
        if (!gameTeams.isEmpty()) end(gameTeams.get(0));
        else if (!lobbyTeams.isEmpty()) end(lobbyTeams.get(0));
        System.out.println("\u001B[36m Have a good day! :) \u001B[0m");
        System.out.println("\u001B[31m (-) Disabling Panty Raid. \u001B[0m");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
                             String label, String[] args) {
        switch (label) {
            case "nickname":
                if (!(sender instanceof Player)) return false;
                Player player = (Player) sender;
                GamePlayer gamePlayer = getGamePlayer(player);
                if (args.length == 0) {
                    player.setDisplayName(player.getName());
                    player.sendMessage(RED + "You use the " +
                            "/nickname command by doing /nickname " +
                            "<Nickname> (without the brackets). " +
                            "All spaces will be omitted and combined " +
                            "into one long nickname.");
                    return false;
                }
                String nickname = "";
                if (args.length > 1) {
                    String stringToAdd;
                    for (String arg : args) {
                        stringToAdd = nickname + arg;
                        nickname = stringToAdd;
                    }
                } else nickname = args[0];
                if (nickname.length() > 25) {
                    player.sendMessage(RED + " (!!!) Please " +
                            "choose a nickname that is shorter than 25 " +
                            "characters (including color codes).");
                    return false;
                }

                nickname = nickname.replaceAll("&0",
                        ChatColor.BLACK.toString());
                nickname = nickname.replaceAll("&1",
                        ChatColor.DARK_BLUE.toString());
                nickname = nickname.replaceAll("&2",
                        ChatColor.DARK_GREEN.toString());
                nickname = nickname.replaceAll("&3",
                        ChatColor.DARK_AQUA.toString());
                nickname = nickname.replaceAll("&4",
                        ChatColor.DARK_RED.toString());
                nickname = nickname.replaceAll("&5",
                        ChatColor.DARK_PURPLE.toString());
                nickname = nickname.replaceAll("&6",
                        ChatColor.GOLD.toString());
                nickname = nickname.replaceAll("&7",
                        ChatColor.GRAY.toString());
                nickname = nickname.replaceAll("&8",
                        ChatColor.DARK_GRAY.toString());
                nickname = nickname.replaceAll("&9",
                        ChatColor.BLUE.toString());
                nickname = nickname.replaceAll("&a",
                        ChatColor.GREEN.toString());
                nickname = nickname.replaceAll("&b",
                        ChatColor.AQUA.toString());
                nickname = nickname.replaceAll("&c",
                        RED.toString());
                nickname = nickname.replaceAll("&d",
                        ChatColor.LIGHT_PURPLE.toString());
                nickname = nickname.replaceAll("&e",
                        ChatColor.YELLOW.toString());
                nickname = nickname.replaceAll("&f",
                        ChatColor.WHITE.toString());
                nickname = nickname.replaceAll("&l",
                        BOLD.toString());

                player.setDisplayName(nickname);
                getGamePlayer(player).getPlayerData().setNickname(nickname);
                player.sendMessage(ChatColor.GOLD +
                        "Your nickname has been changed to " + nickname);
                return true;
            case "rules":
                sender.sendMessage(ChatColor.AQUA +
                        BOLD.toString() + "Rules:\n" +
                        RED + " 1) Be kind and courteous. " +
                        "If something you're about to say seems " +
                        "offensive in any way, to anyone, even if " +
                        "they're not there, don't say it.\n" +
                        ChatColor.GOLD + " 2) Teaming IS allowed and " +
                        "encouraged. Use your opponents to your " +
                        "advantage, and be as cutthroat as you like.\n" +
                        ChatColor.YELLOW + " 3) No hacking, cheating, " +
                        "etc. Obviously everyone wants the game to be " +
                        "fair, so please don't ruin it for everyone else." +
                        "\n" + ChatColor.GREEN + " 4) Swearing is " +
                        "allowed, but please be semi-tame with it. Don't " +
                        "be swearing every other word, unless it's with " +
                        "your friends in a chat, then go right on ahead.");
                return true;
            case "instructions":
                switch (minigame) {
                    case PANTYRAID:
                        sender.sendMessage(AQUA + BOLD.toString() +
                                "PantyRaid Instructions:\n" +
                                RED + " 1) The goal of the game is to " +
                                "capture the other teams' flags (the " +
                                "armor on their Armor Stands) and bring " +
                                "them back to your base.\n" +
                                GOLD + " 2) To 'capture' a flag, you " +
                                "must right-click them on your Armor " +
                                "Stand. This will, one, add a flag to " +
                                "your team's Armor Stand, and two, if it " +
                                "was the last flag of the team you stole " +
                                "it from, it will stop letting the other " +
                                "team respawn after they die.\n" +
                                YELLOW + " 3) You can only have one flag " +
                                "OR one Nether Star in your inventory at " +
                                "a time. When either are in your " +
                                "inventory, you cannot drop or store " +
                                "them in anything, such as chests.\n" +
                                GREEN + " 4) There is an altar (Beacon) " +
                                "in the middle of the map. Every five " +
                                "minutes from the beginning of the game, " +
                                "a Nether Star will spawn there. You can " +
                                "choose to pick this up and carry it back " +
                                "to your base. Doing so and right-" +
                                "clicking it on your Armor Stand will " +
                                "give you 10 Netherite Ingots (the best " +
                                "resource in the game).\n" +
                                AQUA + " 5) Right-click your Armor Stand " +
                                "with anything else will open the shop. " +
                                "This is where you can upgrade your " +
                                "armor, tools, and give your team buffs " +
                                "against your enemies. You can also find " +
                                "blocks, food, and other helpful items " +
                                "there.\n" +
                                DARK_AQUA + " 6) There are many item " +
                                "generators around the maps. The " +
                                "generator on your island drops Iron/Gold " +
                                "Nuggets. The generators along the " +
                                "outskirts have Iron/Gold Ingots, and the " +
                                "generators in the middle have Diamonds " +
                                "and Netherite Scraps.\n" +
                                DARK_PURPLE + " 7) The Iron/Gold Nuggets " +
                                "and Netherite Scraps have no value in " +
                                "the store, but when you pick up four of " +
                                "them, they will condense themselves down " +
                                "into ingots automatically.");
                        break;
                    case HIDEANDSEEK:
                        sender.sendMessage(AQUA + BOLD.toString() +
                                "HideAndSeek Instructions:\n" +
                                RED + " 1) The goal of the hiders is to " +
                                "hide and avoid the seekers until time " +
                                "runs out. The goal of the seekers is to " +
                                "catch all of the hiders before then.\n" +
                                GOLD + " 2) The hiders get 1 minute to " +
                                "hide before the seekers are released! " +
                                "While you can run around after this " +
                                "minute, the seekers have a speed boost " +
                                "to ensure that they can catch everyone " +
                                "in 5 minutes. Pick your hiding spots " +
                                "well!\n" +
                                YELLOW + " 3) In order to catch a hider, " +
                                "simply right-click them. Punching them " +
                                "will not work!\n");
                        break;
                }
                return true;
            case "help":
                switch (minigame) {
                    case PANTYRAID:
                        sender.sendMessage(AQUA + BOLD.toString() +
                                "PantyRaid Help:\n" +
                                RED + " 1) Having problems understanding " +
                                "the game? /instructions will tell you " +
                                "how the game works. If you are still " +
                                "confused about something, please ask " +
                                "around, or even message the staff on " +
                                "the Discord. They are here to help!\n" +
                                GOLD + " 2) If you ever get tired of " +
                                "dying as soon as your last flag has been " +
                                "captured, try buying a 1-UP. They are in " +
                                "the shop, located under the 'Items' " +
                                "section. You can buy up to three extra " +
                                "lives that are used once your team would " +
                                "normally be eliminated.\n" +
                                YELLOW + " 3) Sometimes going in and " +
                                "stealing flags is a lot better than " +
                                "trying to kill someone. Sneak in while " +
                                "they're not looking, and you could give " +
                                "your team another flag when you " +
                                "could've gotten yourself killed trying " +
                                "to fight someone.\n" +
                                GREEN + " 4) The game isn't always about " +
                                "taking flags and killing people. Spend " +
                                "the time and upgrade your team, as well " +
                                "as yourself, before heading into battle. " +
                                "Do everything you can to give yourself " +
                                "the edge that you need to win, without " +
                                "breaking the rules, of course.\n" +
                                AQUA + " 5) Getting the Nether Star from " +
                                "the middle can be a total game changer. " +
                                "If you have a phone nearby or are great " +
                                "at counting to 300 seconds in your head, " +
                                "you could perfectly time a pickup at the " +
                                "altar and score yourself 10 Netherite " +
                                "Ingots.\n" +
                                DARK_AQUA + " 6) Stuck getting killed " +
                                "over and over again? Try using blocks to " +
                                "shield yourself. Your Armor Stand can " +
                                "also be used to shield yourself from " +
                                "incoming enemy projectiles. Be weary " +
                                "though, explosive arrows will still " +
                                "damage you through both blocks and your " +
                                "Armor Stand.");
                        break;
                    case HIDEANDSEEK:
                        sender.sendMessage(AQUA + BOLD.toString() +
                                "HideAndSeek Help:\n" +
                                RED + " 1) Having problems understanding " +
                                "the game? /instructions will tell you " +
                                "how the game works. If you are still " +
                                "confused about something, please ask " +
                                "around, or even message the staff on " +
                                "the Discord. They are here to help!\n");
                        break;
                }
                return true;
            case "endgame":
                if (sender.isOp()) end(gameTeams.get(0));
                else sender.sendMessage(DARK_RED +
                        "You don't have access to that!");
                return true;
        }
        return false;
    }

    /**
     * Searches through the PandamoniumGames folder for the
     * minigame.txt file. If there is one, then it will search for the
     * first word that isn't a comment and try and find the minigame
     * that associates to it. If there isn't one, the file will be made,
     * and the minigame will default to PantyRaid.
     */
    public void chooseMiniGame() {
        if (getDataFolder().exists() && getDataFolder().isDirectory()
                && getDataFolder().listFiles().length > 0) {
            for (File file : getDataFolder().listFiles()) {
                if (file.getName().equalsIgnoreCase(
                        "minigame.txt")) {
                    try {
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNext()) {
                            String line = scanner.nextLine();
                            if (line.startsWith("//") ||
                                    line.startsWith("#")) continue;
                            boolean stopSearch = false;
                            for (MiniGame miniGame : MiniGame.values())
                                if (miniGame.getDisplayName().
                                        endsWith(line)) {
                                    minigame = miniGame;
                                    stopSearch = true;
                                    break;
                                }
                            if (stopSearch) break;
                        }
                    } catch (IOException ignored) {
                    }

                    // TODO: Add Listeners for new Minigames.
                    switch (minigame) {
                        case HIDEANDSEEK:
                            System.out.println("\u001B[32m (+) Enabling " +
                                    "Hide and Seek. \u001B[0m");
                            new HideAndSeek.Listeners(plugin);
                            break;
                        default:
                            // Defaults to PantyRaid.
                            new PantyRaid.Listeners(plugin);
                            new PantyRaid.PlayerData().mySqlSetup();
                            for (Player player : server.getOnlinePlayers())
                                PlayerData.getPlayerData(player).
                                        loadPlayerData();
                            System.out.println("\u001B[32m (+) Enabling " +
                                    "Panty Raid. \u001B[0m");
                    }
                    attemptToStart();
                    if (minigame.isTeamBased()) attemptToEnd();
                    break;
                }
            }
        } else {
            getDataFolder().mkdir();
            File file = new File(getDataFolder().getPath() +
                    "/minigame.txt");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append("PantyRaid");
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Changes the MiniGame and then reloads the server to load the
     * given miniGameToChangeTo into the server so that it can be played.
     *
     * @param miniGameToChangeTo is the MiniGame that will be loaded
     *                           and played after the server is reloaded.
     */
    public static void changeMiniGame(MiniGame miniGameToChangeTo) {
        File dataFolder = plugin.getDataFolder();
        if (dataFolder.exists() && dataFolder.isDirectory()
                && dataFolder.listFiles().length > 0) {
            for (File file : dataFolder.listFiles()) {
                if (file.getName().equalsIgnoreCase(
                        "minigame.txt")) {
                    try {
                        LinkedList<String> fileContents = new LinkedList<>();
                        Scanner scanner = new Scanner(file);
                        while (scanner.hasNext()) {
                            String line = scanner.nextLine();
                            if (line.startsWith("//") ||
                                    line.startsWith("#")) {
                                fileContents.add(line);
                                continue;
                            }
                            boolean stopSearch = false;
                            for (MiniGame miniGame : MiniGame.values()) {
                                String miniGameName = miniGame.
                                        getDisplayName().substring(2);
                                if (miniGameName.length() !=
                                        line.length()) continue;
                                miniGameName = miniGameName.toLowerCase();
                                line = line.toLowerCase();
                                boolean sameWord = true;
                                for (int i = 0; i < line.length() - 1; i++)
                                    if (line.toCharArray()[i] !=
                                            miniGameName.toCharArray()[i]) {
                                        sameWord = false;
                                        break;
                                    }
                                if (sameWord) {
                                    line = miniGameToChangeTo.
                                            getDisplayName().substring(2);
                                    stopSearch = true;
                                    break;
                                }
                            }
                            fileContents.add(line);
                            if (stopSearch) {
                                PrintWriter pw = new PrintWriter(file);
                                pw.print("");
                                for (String s : fileContents)
                                    pw.append(s).append("\n");
                                pw.close();
                                return;
                            }
                        }
                    } catch (IOException ignored) {
                    }
                }
            }
        } else {
            dataFolder.mkdir();
            File file = new File(dataFolder.getPath() +
                    "/minigame.txt");
            try {
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.append("PantyRaid");
                writer.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * Loads all GamePlayers who are in the lobby World into the
     * lobbyPlayers LinkedList.
     */
    public static void getLobbyPlayers() {
        World lobbyWorld = server.getWorld(lobbyWorldName);
        if (lobbyWorld == null) {
            System.out.println("There isn't a world called \"lobby\".");
            return;
        }
        for (LivingEntity entity : lobbyWorld.getLivingEntities())
            if (entity instanceof Player) {
                Player player = (Player) entity;
                getGamePlayer(player);
                if (!player.getInventory().contains(Material.
                        FIREWORK_ROCKET)) Listeners.setLobbyInv(player);
            }
    }

    /**
     * Starts the game.
     */
    private static void start() {
        if (gameGoingAlready) return;
        gameGoingAlready = true;
        gameTeams.addAll(lobbyTeams);
        LinkedList<GamePlayer> playersToPlayWith = whichPlayersToPlayWith();
        for (GamePlayer gamePlayer : playersToPlayWith)
            for (Team team : gameTeams)
                if (team.getColor() == gamePlayer.getTeamColor()) {
                    gamePlayer.setTeam(team);
                    if (!team.contains(gamePlayer)) team.add(gamePlayer);
                    break;
                }
        resetTeams();
//        minigame.getPandamoniumGame().start(whichPlayersToPlayWith());
        if (playersToPlayWith.size() >= MIN_GAME_SIZE)
            for (GamePlayer gamePlayer : playersToPlayWith)
                changeWorld(gamePlayer.getPlayer(), gameWorldName);
        playersToPlayWith.clear();
    }

    /**
     * Runs the checkPlayers method every 10 seconds.
     */
    private void attemptToStart() {
        scheduler.scheduleSyncRepeatingTask(this,
                () -> checkPlayers(true),
                0, 200); // 200 = 10 Seconds
    }

    /**
     * Checks all of the Players in the lobby World. Each Player
     * doesn't have to choose a Team, Gamemode, or Map, but it is
     * optional. Whatever Players don't will have their Gamemode
     * and Map vote randomly cast and their Team strategically
     * (and partially randomly) cast.
     */
    public static void checkPlayers(boolean autoStart) {
        if (gameGoingAlready) return;
        getLobbyPlayers();

        LinkedList<GamePlayer> playersToPlayWith = whichPlayersToPlayWith();
        if (playersToPlayWith.size() == 0) return;
        if (playersToPlayWith.size() < MIN_GAME_SIZE) {
            playersToPlayWith.forEach(lobbyPlayer ->
                    lobbyPlayer.getPlayer().sendMessage(RED +
                            "There must be at least " + MIN_GAME_SIZE +
                            " players in the lobby to begin the game."));
            return;
        }

        boolean allPlayersReady = true;
        for (GamePlayer lobbyPlayer : playersToPlayWith)
            if (lobbyPlayer.isNotReady() &&
                    lobbyPlayer.getPlayer().isOnline()) {
                if (!autoStart) lobbyPlayer.getPlayer().sendMessage(
                        RED + "The game cannot start until " +
                                "everyone is ready. Fire your firework " +
                                "to become ready!");
                allPlayersReady = false;
            }

        if (allPlayersReady) {
            if (minigame.isTeamBased()) randomizeUnteamedPlayers();
            countdownToGame();
        }
    }

    /**
     * Runs the checkGamePlayers method every 10 seconds.
     */
    private void attemptToEnd() {
        scheduler.scheduleSyncRepeatingTask(this,
                Game::checkGamePlayers, 0,
                200); // 200 = 10 Seconds
    }

    /**
     * Checks all of the Teams in the gameTeams. If there is only
     * one remaining Team that is alive, end the game with that Team
     * as the victor. If there are no Teams that are alive, end the
     * game with the first Team in gameTeams as the victor.
     */
    public static void checkGamePlayers() {
        if (!gameGoingAlready) return;
        int numGameTeamsAlive = 0;
        Team lastTeamAlive = gameTeams.get(0);
        for (Team gameTeam : gameTeams) {
            if (gameTeam.size() == 0) continue;
            if (gameTeam.isAlive()) {
                numGameTeamsAlive++;
                lastTeamAlive = gameTeam;
            }
        }
        if (lastTeamAlive.getColor() == TeamColor.NULL) return;
        if (numGameTeamsAlive == 1) end(lastTeamAlive);
        else if (numGameTeamsAlive == 0) end(gameTeams.get(0));
    }

    /**
     * Counts down from 10 until the game starts.
     */
    private static void countdownToGame() {
        if (gameGoingAlready) return;
        if (scheduler.isCurrentlyRunning(startCountdownId)) return;
        if (gameStarting) return;
        gameStarting = true;
        int[] secondsToGame = new int[]{10};
        startCountdownId = scheduler.scheduleSyncRepeatingTask(plugin,
                () -> {
                    if (secondsToGame[0] == 0) {
                        scheduler.cancelTask(startCountdownId);
                        // TODO: Add minigame Start methods here.
                        switch (minigame) {
                            case HIDEANDSEEK:
//                            case TAG:
//                                HideAndSeek.Utility.start();
                                break;
                            default:
//                                new PantyRaid.Utility().start(whichPlayersToPlayWith());
//                                PantyRaid.Utility.start();
                                Game.start();
                        }
                        gameStarting = false;
                        return;
                    }
                    Bukkit.broadcastMessage(String.format(
                            "%s%d seconds until the game starts...",
                            ChatColor.DARK_AQUA, secondsToGame[0]--));
                }, 0, 20);
    }

    /**
     * Sends all players on a team back to the lobby World.
     *
     * @param winningTeam is the Team that won.
     */
    public static void end(Team winningTeam) {
        TeamColor teamColor = winningTeam.getColor();
        if (gameGoingAlready) Bukkit.broadcastMessage(teamColor.
                getTextColor() + "Game Over! " + teamColor + " Team wins!");
        for (Player player : server.getOnlinePlayers())
            if (player.getWorld().getName().equalsIgnoreCase(
                    Game.gameWorldName)) changeWorld(player, lobbyWorldName);

        switch (minigame) {
            default:
//                PantyRaid.Utility.end(winningTeam);
        }

        for (Team team : gameTeams) {
            for (GamePlayer player : team)
                player.setReady(false);
            team.clear();
        }
        gameGoingAlready = false;
    }

    /**
     * Finds the lobbyPlayers that is associated to the Minigame that
     * is being played.
     *
     * @return the correct lobbyPlayers for the Minigame to be played.
     */
    public static LinkedList<GamePlayer> whichPlayersToPlayWith() {
        // TODO: Add lobbyPlayers for new Minigames.
        LinkedList<GamePlayer> playersToPlayWith;
        switch (minigame) {
            case HIDEANDSEEK:
//            case TAG:
                return Utility.lobbyPlayers;
            default:
                return lobbyPlayers;
        }
    }

    /**
     * Searches through all of the GamePlayers in lobbyPlayers.
     * If the given player doesn't have a GamePlayer counterpart,
     * one will be made, added to lobbyPlayers, and then returned.
     *
     * @param player is the Player whose GamePlayer counterpart is
     *               returned.
     * @return the GamePlayer counterpart of the given player.
     */
    public static GamePlayer getGamePlayer(Player player) {
        if (player.getWorld().getName().equalsIgnoreCase(lobbyWorldName))
            for (GamePlayer gamePlayer : whichPlayersToPlayWith()) {
                if (gamePlayer.getPlayer().getUniqueId().
                        equals(player.getUniqueId())) return gamePlayer;
            }
        else
            for (Team team : gameTeams)
                for (GamePlayer gamePlayer : team)
                    if (gamePlayer.getPlayer().getUniqueId().equals(
                            player.getUniqueId())) return gamePlayer;
        GamePlayer newGamePlayer = new GamePlayer(player);
        whichPlayersToPlayWith().add(newGamePlayer);
        return newGamePlayer;
    }

    /**
     * Searches through all of the Teams in the teams LinkedList.
     * If there is a Team that is associated with the given
     * teamColor, return the Team, otherwise return null.
     *
     * @param teamColor is the TeamColor that is associated with
     *                  the Team that is going to be returned.
     * @return the Team that is associated with the given teamColor.
     */
    public static Team getTeam(TeamColor teamColor) {
        for (Team team : lobbyTeams) {
            if (team.getColor() == teamColor) {
                return team;
            }
        }
        return null;
    }

    /**
     * Searches through all of the Teams in the lobbyTeams
     * LinkedList. If there is a Team that is associated with a
     * TeamColor that has the given material in it's list of values,
     * return the Team, otherwise return null.
     *
     * @param teamMaterial is the Material that is matched up to a
     *                     TeamColor that is matched up to a Team.
     * @return the Team that is associated with the TeamColor that has
     * the given material in it's list of values.
     */
    public static Team getTeam(Material teamMaterial) {
        for (TeamColor teamColor : TeamColor.values()) {
            if (teamColor.getNoTeammatesBannerMaterial() == teamMaterial) {
                return getTeam(teamColor);
            }
        }
        return null;
    }

    /**
     * Searches through all of the Teams in the lobbyTeams
     * LinkedList. If there is a Team that is associated with a
     * TeamColor that has the given color in it's list of values,
     * return the Team, otherwise return null.
     *
     * @param color is the Color that is matched up to a TeamColor that
     *              is matched up to a Team.
     * @return the Team that is associated with the TeamColor that has
     * the given color in it's list of values.
     */
    public static Team getTeam(Color color) {
        for (TeamColor teamColor : TeamColor.values()) {
            if (teamColor.getArmorColor() == color) {
                return getTeam(teamColor);
            }
        }
        return null;
    }

    /**
     * Teleports the given player to the given world.
     *
     * @param player    is the Player that is teleported to the given world.
     * @param worldName is the name of the World that the given player is
     *                  teleported to.
     */
    public static void changeWorld(Player player, String worldName) {
        try {
            World world = Bukkit.getWorld(worldName);
            assert world != null;
            player.teleport(world.getSpawnLocation());
        } catch (Exception ignored) {
            System.out.println(
                    "Something went wrong while teleporting someone.");
        }
    }

    /**
     * Resets the teams LinkedList with all of the values in the
     * TeamColor Enum.
     */
    public static void resetTeams() {
        lobbyTeams.clear();
        Arrays.stream(TeamColor.values()).forEach(color ->
                lobbyTeams.add(new Team(color)));
    }

    /**
     * Counts the number of Teams with no GamePlayers assigned to
     * them. If there aren't at least two Teams started, then up to two
     * unteamed players will be added to a random Team, and the following
     * algorithm will do its best to ensure that Teams are as evenly
     * spread out as possible.
     */
    private static void randomizeUnteamedPlayers() {
        getLobbyPlayers();
        int count = 0;
        for (Team team : lobbyTeams)
            if (team.size() > 0)
                count++;
        for (int i = 0; i < whichPlayersToPlayWith().size() &&
                count < 2; i++) {
            GamePlayer player = whichPlayersToPlayWith().get(i);
            if (player.getTeam() == null) {
                Team randomTeam = lobbyTeams.get((int)
                        ((Math.random() * (lobbyTeams.size() - 1)) + 1));
                while (randomTeam.size() > 0) {
                    randomTeam = lobbyTeams.get((int)
                            ((Math.random() * (lobbyTeams.size() - 1)) + 1));
                }
                player.setTeam(randomTeam);
                count--;
            }
        }

        for (GamePlayer gamePlayer : whichPlayersToPlayWith()) {
            if (gamePlayer.getTeam() != null) continue;
            Team smallestTeam = lobbyTeams.get(1);
            for (Team team : lobbyTeams) {
                if (team.getColor() == TeamColor.NULL) continue;
                if (team.size() < smallestTeam.size()) smallestTeam = team;
            }
            smallestTeam.add(gamePlayer);
            gamePlayer.setTeam(smallestTeam);
        }
    }

    /**
     * Simply makes a List<String> to use as lore for an ItemStack.
     *
     * @param loreLines is the lines of lore that are put into
     *                  the List<String>.
     * @return the List<String> that is made.
     */
    public static List<String> loreMaker(String... loreLines) {
        // This is just here for my IDE to auto condense this method.
        return new LinkedList<>(Arrays.asList(loreLines));
    }

    /**
     * Searches through the current PotionEffects that the given
     * player has. If they have the given effect and the effect
     * has a lower duration or amplifier, then overwrite the current
     * effect with the given effect. If they don't already have the
     * given effect, then add it to the given player's list of PotionEffects.
     *
     * @param player is the Player that has it's effects searched through.
     * @param effect is the effect that is to be added to the given player's
     *               list of PotionEffects.
     */
    public static void addPotionEffect(Player player,
                                       PotionEffect effect) {
        for (PotionEffect potionEffect : player.getActivePotionEffects()) {
            if (potionEffect.getType() == effect.getType()) {
                if ((potionEffect.getAmplifier() < effect.getAmplifier() ||
                        potionEffect.getDuration() < effect.getDuration())) {
                    player.removePotionEffect(potionEffect.getType());
                    player.addPotionEffect(effect);
                }
                return;
            }
        }
        player.addPotionEffect(effect);
    }

    /**
     * Checks to see if the given voter has already voted or not.
     * If they have, it will overwrite their current vote with the
     * given gameToVoteOn.
     *
     * @param voter        is the Player that voted on a MiniGame.
     * @param gameToVoteOn is the MiniGame that the given voter voted on.
     */
    public static void voteOnMiniGame(Player voter,
                                      MiniGame gameToVoteOn) {
        for (Entry hash : gameVotes.entrySet()) {
            if (hash.getKey().equals(voter)) {
                if (hash.getValue().equals(gameToVoteOn)) {
                    voter.sendMessage(RED + "You've already " +
                            "voted on " + gameToVoteOn.getDisplayName() +
                            RED + ".");
                    return;
                }
                voter.sendMessage(ChatColor.GOLD + "You changed your " +
                        "vote to " + gameToVoteOn.getDisplayName() +
                        ChatColor.GOLD + ".");
                gameVotes.remove(voter);
                gameVotes.put(voter, gameToVoteOn);
                return;
            }
        }
        voter.sendMessage(ChatColor.GREEN + "You voted on " +
                gameToVoteOn.getDisplayName() + ChatColor.GREEN + ".");
        gameVotes.put(voter, gameToVoteOn);
        System.out.println(gameVotes.entrySet());
    }

    /**
     * Chooses a MiniGame based upon the number of votes they have.
     * If there are multiple MiniGames that share the highest number of
     * votes, one is chosen at random.
     */
    public static void chooseGame() {
        // Counts up all votes.
        HashMap<MiniGame, Integer> votes = new HashMap<>();
        for (Entry vote : gameVotes.entrySet()) {
            boolean alreadyVotedOn = false;
            for (Entry gameToVoteOn : votes.entrySet())
                if (vote.getValue() == gameToVoteOn.getValue()) {
                    gameToVoteOn.setValue(((Integer)
                            gameToVoteOn.getValue()) + 1);
                    alreadyVotedOn = true;
                    break;
                }
            if (!alreadyVotedOn) votes.put((MiniGame) vote.getValue(), 1);
        }

        // Finds the highest amount of votes.
        int highestVote = 0;
        for (Entry miniGame : votes.entrySet())
            if ((Integer) miniGame.getValue() > highestVote)
                highestVote = (Integer) miniGame.getValue();

        // Stores the ones have the highest amount of votes.
        HashMap<MiniGame, Integer> topVotes = new HashMap<>();
        for (Entry miniGame : votes.entrySet())
            if (((Integer) miniGame.getValue()) == highestVote)
                topVotes.put((MiniGame) miniGame.getKey(),
                        (Integer) miniGame.getValue());

        // Randomly selects the map that had the highest amount of votes.
        int mapToSelect = (int) (Math.random() * topVotes.size());
        minigame = (MiniGame) topVotes.keySet().toArray()[mapToSelect];
        gameVotes.clear();
        changeMiniGame(minigame);
        Game.server.broadcastMessage(ChatColor.GOLD + "Loading " +
                minigame.getDisplayName() + ChatColor.GOLD +
                "... This may take around 20 seconds.");
        Game.server.reload();
    }
}
