package General;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
    private final Player player;
    private int counter;

    public Countdown(Player player, int counter) {
        this.player = player;
        this.counter = counter;
    }

    @Override
    public void run() {
        if (counter > 0) {
            player.sendTitle(ChatColor.AQUA + "" + counter--,
                    ChatColor.AQUA + "Seconds until respawn!",
                    2, 15, 2);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendTitle(ChatColor.GREEN + "GO!", "",
                    0, 40, 0);
            cancel();
        }
    }
}
