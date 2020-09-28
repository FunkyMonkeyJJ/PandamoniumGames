package PantyRaid;

import General.Game;
import General.GamePlayer;
import General.PandamoniumGame;
import Team.Team;
import Team.TeamColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.RED;

public class Utility extends PandamoniumGame {
    public static LinkedList<Team> lobbyTeams = new LinkedList<>();
    public static LinkedList<Team> gameTeams = new LinkedList<>();

    public static LinkedList<Team> regenTeams = new LinkedList<>();
    public static LinkedList<Generator> generators = new LinkedList<>();
    public static LinkedList<Block> blocks = new LinkedList<>();

    public static Map map = Map.CLASSIC;
    public static HashMap<Player, Map> mapVotes = new HashMap<>();
    public static Gamemode gamemode = Gamemode.NORMAL;
    public static HashMap<Player, Gamemode> gamemodeVotes = new HashMap<>();

    private static final BukkitTask[] endTasks = new BukkitTask[5];

    public Utility(Collection<GamePlayer> gamePlayers) {
        super(gamePlayers);
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
     * Swaps the Teams from the given teamsToSwapFrom to the given
     * teamsToSwapTo, and then clears the given teamsToSwapFrom.
     *
     * @param teamsToSwapFrom are the Teams that are swapped over to
     *                        the given teamsToSwapTo.
     * @param teamsToSwapTo   are the Teams that have their contents
     *                        added to from the given teamsToSwapFrom.
     */
    public static void swapTeams(LinkedList<Team> teamsToSwapFrom,
                                 LinkedList<Team> teamsToSwapTo) {
        int minTeamSize = Math.min(teamsToSwapFrom.size(),
                teamsToSwapTo.size());
        for (int i = 0; i < minTeamSize - 1; i++) {
            Team teamToSwapFrom = teamsToSwapFrom.get(i);
            Team teamToSwapTo = teamsToSwapTo.get(i);
            if (teamToSwapFrom.getColor() == teamToSwapTo.getColor())
                teamToSwapTo.addAll(teamToSwapFrom);
            teamToSwapFrom.clear();
        }
    }

    /**
     * Searches the game World for any bedrock and armor stands.
     * The bedrock that are found are stored as locations for generators.
     * The blocks underneath of them decide what generators they are for:
     * Iron Ore = Iron Nugget, Gold Ore = Gold Nugget,
     * Iron Block = Iron Ingot, Gold Block = Gold Ingot,
     * Diamond Ore = Diamond, Ancient Debris = Netherite Scraps,
     * Beacon = Nether Star (This is an altar)
     */
    public static void searchGameWorld() {
        World gameWorld = Bukkit.getWorld(Game.gameWorldName);
        assert gameWorld != null;
        Game.server.dispatchCommand(Game.server.getConsoleSender(),
                "kill @e[type=!player]");
        Location altarLocation = null;
        // Find and Activate Generators and Spawn Team Armor Stands:
        for (int x = -50; x < 50; x++)
            for (int y = 50; y < 100; y++)
                for (int z = -50; z < 50; z++) {
                    Block block = gameWorld.getBlockAt(x, y, z);
                    Block blockBelow = gameWorld.getBlockAt(
                            x, y - 1, z);
                    Block blockAbove = gameWorld.getBlockAt(
                            x, y + 1, z);
                    Location loc = new Location(gameWorld, x, y, z);
                    switch (block.getType()) {
                        case BEDROCK:
                            switch (blockBelow.getType()) {
                                case IRON_ORE:
                                    new Generator(Material.IRON_NUGGET, loc,
                                            100, 1);
                                    break;
                                case GOLD_ORE:
                                    new Generator(Material.GOLD_NUGGET, loc,
                                            80, 4);
                                    break;
                                case IRON_BLOCK:
                                    new Generator(Material.IRON_INGOT, loc,
                                            25, 2);
                                    break;
                                case GOLD_BLOCK:
                                    new Generator(Material.GOLD_INGOT, loc,
                                            20, 8);
                                    break;
                                case DIAMOND_BLOCK:
                                    new Generator(Material.DIAMOND, loc,
                                            5, 30);
                                    break;
                                case ANCIENT_DEBRIS:
                                    new Generator(Material.NETHERITE_SCRAP,
                                            loc, 20, 30);
                                    break;
                            }
                            for (Team team : Game.gameTeams) {
                                if (team.isEmpty()) continue;
                                TeamColor teamColor = team.getColor();
                                if (blockAbove.getType() == teamColor.
                                        getNoTeammatesBannerMaterial()) {
                                    ArmorStand as = (ArmorStand)
                                            gameWorld.spawnEntity(loc.add(
                                                    0.5, 2, 0.5),
                                                    EntityType.ARMOR_STAND);
                                    as.setInvulnerable(true);
                                    team.setArmorStand(as);
                                    team.addFlag();
                                    System.out.println("Found the " +
                                            teamColor + " armor stand.");
                                }
                            }
                            break;
                        case BEACON:
                            if (blockBelow.getType() == Material.OBSIDIAN) {
                                new Generator(Material.NETHER_STAR, loc,
                                        3, 300,
                                        301);
                                altarLocation = loc.clone().add(
                                        0, 1, 0);
                            }
                    }
                }

//        assert altarLocation != null;
//        Vector dir = altarLocation.getDirection();
//        System.out.println("Altar Location: " + dir.getX() + " " +
//                dir.getY() + " " + dir.getZ());
        // TODO: Get direction of the altar.
    }

    /**
     * Checks to see if the given voter has already voted or not.
     * If they have, it will overwrite their current vote with the
     * given mapToVoteOn.
     *
     * @param voter       is the Player that voted on a Map.
     * @param mapToVoteOn is the Map that the given voter voted on.
     */
    public static void voteOnMap(Player voter, Map mapToVoteOn) {
        for (Entry hash : mapVotes.entrySet()) {
            if (hash.getKey().equals(voter)) {
                if (hash.getValue().equals(mapToVoteOn)) {
                    voter.sendMessage(ChatColor.RED + "You've already " +
                            "voted on the " + mapToVoteOn.getWorldName() +
                            " map.");
                    return;
                }
                voter.sendMessage(ChatColor.GOLD + "You changed your vote " +
                        "to the " + mapToVoteOn.getWorldName() + " map.");
                mapVotes.remove(voter);
                mapVotes.put(voter, mapToVoteOn);
                return;
            }
        }
        voter.sendMessage(ChatColor.GREEN + "You voted on the " +
                mapToVoteOn.getWorldName() + " map.");
        mapVotes.put(voter, mapToVoteOn);
        System.out.println(mapVotes.entrySet());
    }

    /**
     * Chooses a Map based upon the number of votes they have.
     * If there are multiple Gamemodes that share the highest number of
     * votes, one is chosen at random.
     */
    public static void chooseMap() {
        // Counts up all votes.
        HashMap<Map, Integer> votes = new HashMap<>();
        for (Entry vote : mapVotes.entrySet()) {
            boolean alreadyVotedOn = false;
            for (Entry mapToVoteOn : votes.entrySet())
                if (vote.getValue() == mapToVoteOn.getValue()) {
                    mapToVoteOn.setValue(
                            ((Integer) mapToVoteOn.getValue()) + 1);
                    alreadyVotedOn = true;
                    break;
                }
            if (!alreadyVotedOn) votes.put((Map) vote.getValue(), 1);
        }

        // Finds the highest amount of votes.
        int highestVote = 0;
        for (Entry map : votes.entrySet())
            if ((Integer) map.getValue() > highestVote)
                highestVote = (Integer) map.getValue();

        // Stores the ones have the highest amount of votes.
        HashMap<Map, Integer> topVotes = new HashMap<>();
        for (Entry map : votes.entrySet())
            if (((Integer) map.getValue()) == highestVote)
                topVotes.put((Map) map.getKey(), (Integer) map.getValue());

        // Randomly selects the map that had the highest amount of votes.
        int mapToSelect = (int) (Math.random() * topVotes.size());
        map = (Map) topVotes.keySet().toArray()[mapToSelect];
        mapVotes.clear();
    }

    /**
     * Checks to see if the given voter has already voted or not.
     * If they have, it will overwrite their current vote with the
     * given gamemodeToVoteOn.
     *
     * @param voter            is the Player that voted on a Gamemode.
     * @param gamemodeToVoteOn is the Gamemode that the given voter voted on.
     */
    public static void voteOnGamemode(Player voter,
                                      Gamemode gamemodeToVoteOn) {
        for (Entry hash : gamemodeVotes.entrySet()) {
            if (hash.getKey().equals(voter)) {
                if (hash.getValue().equals(gamemodeVotes)) {
                    voter.sendMessage(ChatColor.RED + "You've already " +
                            "voted on the " + gamemodeToVoteOn.
                            getGamemodeName() + " gamemode.");
                    return;
                }
                voter.sendMessage(ChatColor.GOLD + "You changed your vote " +
                        "to the " + gamemodeToVoteOn.getGamemodeName() +
                        " gamemode.");
                gamemodeVotes.remove(voter);
                gamemodeVotes.put(voter, gamemodeToVoteOn);
                return;
            }
        }
        voter.sendMessage(ChatColor.GREEN + "You voted on the " +
                gamemodeToVoteOn.getGamemodeName() + " gamemode.");
        gamemodeVotes.put(voter, gamemodeToVoteOn);
        System.out.println(gamemodeVotes.entrySet());
    }

    /**
     * Chooses a Gamemode based upon the number of votes they have.
     * If there are multiple Gamemodes that share the highest number of
     * votes, one is chosen at random.
     */
    public static void chooseGamemode() {
        // Counts up all votes.
        HashMap<Gamemode, Integer> votes = new HashMap<>();
        for (Entry vote : gamemodeVotes.entrySet()) {
            boolean alreadyVotedOn = false;
            for (Entry gamemodeToVoteOn : votes.entrySet())
                if (vote.getValue() == gamemodeToVoteOn.getValue()) {
                    gamemodeToVoteOn.setValue(
                            ((Integer) gamemodeToVoteOn.getValue()) + 1);
                    alreadyVotedOn = true;
                    break;
                }
            if (!alreadyVotedOn) votes.put((Gamemode) vote.getValue(), 1);
        }

        // Finds the highest amount of votes.
        int highestVote = 0;
        for (Entry gamemode : votes.entrySet())
            if ((Integer) gamemode.getValue() > highestVote)
                highestVote = (Integer) gamemode.getValue();

        // Stores the ones have the highest amount of votes.
        HashMap<Gamemode, Integer> topVotes = new HashMap<>();
        for (Entry gamemode : votes.entrySet())
            if (((Integer) gamemode.getValue()) == highestVote)
                topVotes.put((Gamemode) gamemode.getKey(),
                        (Integer) gamemode.getValue());

        // Randomly selects the gamemode that had the highest amount of votes.
        int gamemodeToSelect = (int) (Math.random() * topVotes.size());
        gamemode = (Gamemode) topVotes.keySet().toArray()[gamemodeToSelect];
        gamemodeVotes.clear();
    }

    private static void updatePlayerData(TeamColor winningTeamColor) {
        // Updates PlayerData that needs processed after each game.
        for (Player player : Game.server.getOnlinePlayers()) {
            PlayerData playerData = PlayerData.getPlayerData(player);
            if (playerData.getKills() > playerData.getRecordKills())
                playerData.setRecordKills(playerData.getKills());
            if (playerData.getFinalKills() >
                    playerData.getRecordFinalKills())
                playerData.setRecordFinalKills(
                        playerData.getFinalKills());
            if (playerData.getDeaths() >
                    playerData.getRecordDeaths())
                playerData.setRecordDeaths(playerData.getDeaths());

            GamePlayer gamePlayer = Game.getGamePlayer(player);
            System.out.println(gamePlayer + " | " + winningTeamColor);
            if (gamePlayer.getTeamColor() != TeamColor.NULL) {
                if (gamePlayer.getTeamColor() == winningTeamColor) {
                    playerData.win();
                    player.sendMessage(ChatColor.GOLD +
                            " Incremented your wins by 1.");
                } else {
                    playerData.lose();
                    player.sendMessage(RED +
                            " Incremented your losses by 1.");
                }
            }

            playerData.updatePlayerData();
        }
    }

    public void suddenDeath() {
        for (Team team : Game.gameTeams) {
            ArmorStand armorStand = team.getArmorStand();
            armorStand.getEquipment().clear();
            for (GamePlayer gamePlayer : team) {
                gamePlayer.setLives(0);
                ItemStack flagItem = gamePlayer.getPlayer().
                        getInventory().getItem(4);
            }
        }
        for (BukkitTask task : endTasks)
            if (!(task.isCancelled())) task.cancel();
        endTasks[0] = Game.scheduler.runTaskLater(Game.plugin, () ->
                Game.end(Game.gameTeams.get(0)), 12000L); // 10 Minutes
        endTasks[1] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "5 Minutes left until the game is over!"),
                6000L);
        endTasks[2] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "3 Minutes left until the game is over!"),
                8400L);
        endTasks[3] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "1 Minute left until the game is over!"),
                10800L);
    }

    @Override
    public void start(Collection<GamePlayer> collection) {
        Game.gameWorldName = "pantyraid-classic";
        searchGameWorld();
        for (GamePlayer gamePlayer : Game.whichPlayersToPlayWith()) {
            gamePlayer.getPlayer().getEnderChest().clear();
            gamePlayer.getPlayerData().addGame();
        }
        endTasks[0] = Game.scheduler.runTaskLater(Game.plugin,
                this::suddenDeath, 24000L); // 20 Minutes
        endTasks[1] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "10 Minutes left until sudden death!"),
                12000L); // 10 Minutes
        endTasks[2] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "5 Minutes left until sudden death!"),
                18000L); // 5 Minutes
        endTasks[3] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(GOLD +
                        "1 Minute left until sudden death!"),
                22600L); // 1 Minutes
    }

    @Override
    public void end(Collection<GamePlayer> winningPlayers) {
        for (Generator gen : generators) gen.stop();
        generators.clear();
        for (Block block : blocks) block.breakNaturally();
        blocks.clear();
        for (GamePlayer gamePlayer : Game.whichPlayersToPlayWith())
            if (gamePlayer.getPlayer().getWorld().getName().equalsIgnoreCase(
                    Game.gameWorldName)) gamePlayer.setReady(false);
        for (BukkitTask task : endTasks)
            if (!(task.isCancelled())) task.cancel();
        updatePlayerData((
                (GamePlayer) winningPlayers.toArray()[0]).getTeamColor());
    }

    @Override
    public boolean checkGamePlayers() {
        if (Game.gameGoingAlready || Game.lobbyPlayers.size() == 0)
            return false;
        for (GamePlayer gamePlayer : Game.lobbyPlayers) {
            if (gamePlayer.getTeamColor() == TeamColor.NULL) return false;
            if (gamePlayer.isNotReady()) return false;
        }
        return true;
    }
}
