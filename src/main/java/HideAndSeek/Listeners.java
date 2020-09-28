package HideAndSeek;

import General.Game;
import General.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.LinkedList;
import java.util.stream.IntStream;

public class Listeners implements Listener {
    public Listeners(Plugin plugin) {
        System.out.println("\u001B[32mEnabling \u001B[36mHide" +
                "\u001B[33mAnd\u001B[36mSeek \u001B[32mListeners! \u001B[0m");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void onDamage(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    void onExhaustion(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!(event.getRightClicked() instanceof Player)) return;
        Player clickedPlayer = (Player) event.getRightClicked();
        GamePlayer gamePlayer = Game.getGamePlayer(player);
        GamePlayer clickedGamePlayer = Game.getGamePlayer(clickedPlayer);
        player.sendMessage(ChatColor.GOLD + "You are it: " +
                ChatColor.DARK_GREEN + gamePlayer.isIt() + ChatColor.GOLD +
                " | They are it: " + ChatColor.DARK_GREEN +
                clickedGamePlayer.isIt());
        if (gamePlayer.isIt() && !clickedGamePlayer.isIt()) {
            clickedPlayer.setGameMode(GameMode.SPECTATOR);
            Game.server.broadcastMessage(clickedPlayer.getDisplayName() +
                    ChatColor.GOLD + " was found!");
            LinkedList<GamePlayer> hidersToFind = Utility.hidersToFind;
            IntStream.range(0, hidersToFind.size()).forEach(i -> {
                if (hidersToFind.get(i).getPlayer().getUniqueId().equals(
                        clickedGamePlayer.getPlayer().getUniqueId()))
                    Utility.hidersToFind.remove(i);
            });
            if (Utility.hidersToFind.size() > 0)
                Game.server.broadcastMessage(ChatColor.GOLD + "" +
                        Utility.hidersToFind.size() + " hiders left to find!");
            else Utility.end(false);
        }
    }

    @EventHandler
    void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType().name().
                    toLowerCase().endsWith("door")) {
                for (Block block : Utility.doors.keySet())
                    if (block.getLocation().equals(event.
                            getClickedBlock().getLocation())) return;
                Utility.doors.put(event.getClickedBlock(),
                        (Openable) event.getClickedBlock().getBlockData());
                // Openable can open and close doors.
            }
        }
    }
}
