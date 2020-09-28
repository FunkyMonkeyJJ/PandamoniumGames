package HideAndSeek;

import General.Game;
import General.GamePlayer;
import General.PandamoniumGame;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class Utility extends PandamoniumGame {
    public static LinkedList<GamePlayer> lobbyPlayers = new LinkedList<>();
    public static LinkedList<GamePlayer> hiders = new LinkedList<>();
    public static LinkedList<GamePlayer> hidersToFind = new LinkedList<>();
    public static LinkedList<GamePlayer> seekers = new LinkedList<>();

    public static HashMap<Block, Openable> doors = new HashMap<>();

    public static Scoreboard hideAndSeekBoard;

    private static Location seekerSpawnPoint;
    private static final BukkitTask[] seekTask = new BukkitTask[4];

    public Utility(Collection<GamePlayer> gamePlayers) {
        super(gamePlayers);
    }

    public static void setRoles() {
        hiders.addAll(lobbyPlayers);
        int numSeekers = (int) IntStream.range(0,
                hiders.size()).filter(i -> i % 6 == 0).count();
        for (int i = 0; i < numSeekers; i++)
            seekers.add(hiders.remove((int) (Math.random() * hiders.size())));
        for (GamePlayer seeker : seekers) {
            Player player = seeker.getPlayer();
            ItemStack elytra = new ItemStack(Material.ELYTRA);
            ItemMeta elytraMeta = elytra.getItemMeta();
            elytraMeta.setUnbreakable(true);
            elytraMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED,
                    new AttributeModifier(player.getUniqueId(),
                            "Fast Move", 1.5,
                            AttributeModifier.Operation.ADD_SCALAR,
                            EquipmentSlot.CHEST));
            elytraMeta.addEnchant(Enchantment.BINDING_CURSE, 1,
                    false);
            elytra.setItemMeta(elytraMeta);
            player.getInventory().clear();
            player.getInventory().setChestplate(elytra);
            seeker.setIt(true);
        }
        hidersToFind.addAll(hiders);
    }

    public static void searchGameWorld() {
        World gameWorld = Game.server.getWorld(Game.gameWorldName);
        for (int x = -100; x < 100; x++)
            for (int y = 0; y < 100; y++)
                for (int z = -100; z < 100; z++) {
                    Block block = gameWorld.getBlockAt(x, y, z);
                    Block blockAbove = gameWorld.getBlockAt(x, y + 1, z);
                    if (block.getType() == Material.BEDROCK &&
                            blockAbove.getType() == Material.BEDROCK) {
                        seekerSpawnPoint = block.getLocation().clone().
                                add(0, 1, 0);
                        return;
                    }
                }
    }

    public static void startHiding() {
        for (GamePlayer seeker : seekers) {
            seeker.getPlayer().teleport(seekerSpawnPoint);
            seeker.getPlayer().sendMessage(ChatColor.GOLD +
                    "You are a seeker! Your goal: Find all hiders.");
        }

        for (GamePlayer hider : hiders) {
            Player player = hider.getPlayer();
            player.teleport(seekerSpawnPoint.clone().add(0, 10, 0));
            Game.addPotionEffect(player, new PotionEffect(
                    PotionEffectType.INVISIBILITY, 1200, 1));
            Game.addPotionEffect(player, new PotionEffect(
                    PotionEffectType.SPEED, 1200, 1));
            player.getInventory().clear();
            player.sendMessage(ChatColor.GOLD + "You are a hider! Your " +
                    "goal: Hide and evade the seekers at all costs!");
        }

        Game.server.broadcastMessage(ChatColor.GREEN +
                "The hiders have one minute to hide! Hurry!!");
        Game.scheduler.runTaskLater(Game.plugin, () ->
                Game.server.broadcastMessage(ChatColor.GOLD +
                        "10 Seconds left to hide!!"), 1000L);
        Game.scheduler.runTaskLater(Game.plugin, () ->
                Game.server.broadcastMessage(ChatColor.GOLD +
                        "30 Seconds left to hide!!"), 600L);
        Game.scheduler.runTaskLater(Game.plugin, () -> {
            for (GamePlayer seeker : seekers) {
                Player player = seeker.getPlayer();
                player.teleport(player.getLocation().
                        clone().add(0, 10, 0));
            }
            Game.server.broadcastMessage(ChatColor.DARK_RED +
                    "Release the seekers!!");
            startSeeking();
        }, 1200L);
    }

    public static void startSeeking() {
        Game.server.broadcastMessage(ChatColor.GREEN +
                "The seekers have five minutes to find all " +
                hidersToFind.size() + " hiders! Hurry!!");
        seekTask[0] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(
                        ChatColor.GOLD + "4 Minutes Left!"), 1200L);
        seekTask[1] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(
                        ChatColor.GOLD + "3 Minutes Left!"), 2400L);
        seekTask[2] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(
                        ChatColor.GOLD + "2 Minutes Left!"), 3600L);
        seekTask[3] = Game.scheduler.runTaskLater(Game.plugin,
                () -> Game.server.broadcastMessage(
                        ChatColor.GOLD + "1 Minute Left!"), 4800L);
        seekTask[4] = Game.scheduler.runTaskLater(Game.plugin,
                () -> end(true), 6000L);
    }

    public static void start() {
        Game.gameGoingAlready = true;
        Game.gameWorldName = "hideandseek";
        HideAndSeek.Utility.setRoles();
        HideAndSeek.Utility.searchGameWorld();
        hideAndSeekBoard = Game.scoreboardManager.getNewScoreboard();
        Team hidingTeam = hideAndSeekBoard.registerNewTeam("Hiders");
        for (GamePlayer hider : hiders)
            hidingTeam.addEntry(hider.getPlayer().getName());
        hidingTeam.setOption(Team.Option.NAME_TAG_VISIBILITY,
                Team.OptionStatus.FOR_OWN_TEAM);
        HideAndSeek.Utility.startHiding();
    }

    public static void end(boolean hidersWin) {
        for (BukkitTask task : seekTask)
            if (!task.isCancelled()) task.cancel();
        if (hidersWin) Game.server.broadcastMessage(ChatColor.GREEN +
                "The hiders won! Great hiding spots, everyone!");
        else Game.server.broadcastMessage(ChatColor.GREEN +
                "The seekers have won this time! Excellent seeking skills!");

        for (GamePlayer gamePlayer : lobbyPlayers)
            gamePlayer.setReady(false);

        for (GamePlayer hider : hiders) {
//            if (hidersWin) hider.getPlayerData().win();
//            else hider.getPlayerData().lose();
            hider.getPlayer().getInventory().clear();
            Game.changeWorld(hider.getPlayer(), Game.lobbyWorldName);
        }

        for (GamePlayer seeker : seekers) {
//            if (hidersWin) seeker.getPlayerData().lose();
//            else seeker.getPlayerData().win();
            seeker.getPlayer().getInventory().clear();
            Game.changeWorld(seeker.getPlayer(), Game.lobbyWorldName);
            seeker.setIt(false);
        }

        lobbyPlayers.clear();
        hiders.clear();
        hidersToFind.clear();
        seekers.clear();
        Game.gameGoingAlready = false;
    }

    @Override
    public void start(Collection<GamePlayer> gamePlayers) {

    }

    @Override
    public void end(Collection<GamePlayer> winningPlayers) {

    }

    @Override
    public boolean checkGamePlayers() {
        return false;
    }
}
