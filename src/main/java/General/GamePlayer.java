package General;

import PantyRaid.PlayerData;
import Team.Team;
import Team.TeamColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class GamePlayer {
    private final Player player;
    private PlayerData playerData;
    private boolean ready = false;
    private Team team = null;

    // PantyRaid Fields
    private ArmorStand flagOrigin;
    private int oneUpsBought = 0;
    private int lives = -1;
    private BukkitTask armorWeightTask;
    private Player lastDamager;
    private long timeLastDamaged;
    private final boolean[][] armorUnlocked;
    private final boolean[][] toolsUnlocked;

    // HideAndSeek Fields
    private boolean it;

    GamePlayer(Player player) {
        this.player = player;
        this.playerData = PlayerData.getPlayerData(player);
        this.armorUnlocked = new boolean[4][5];
        resetArmorUnlocked();
        this.toolsUnlocked = new boolean[5][4];
        resetToolsUnlocked();
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerData getPlayerData() {
        if (playerData == null) playerData = PlayerData.getPlayerData(player);
        return playerData;
    }

    public boolean isNotReady() {
        return !ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        if (team == null || team.getColor() == TeamColor.NULL) return;
        this.team = team;
        System.out.println(player.getName() + " changed teams!!");
        player.sendMessage(ChatColor.GREEN + " (+) Added you to the " +
                team.getColor() + ChatColor.GREEN + " Team.");
    }

    public TeamColor getTeamColor() {
        return team == null ? TeamColor.NULL : team.getColor();
    }

    // PantyRaid Methods Below:

    public ArmorStand getFlagOrigin() {
        return flagOrigin;
    }

    public void setFlagOrigin(ArmorStand flagOrigin) {
        this.flagOrigin = flagOrigin;
    }

    public int getLives() {
        return lives;
    }

    public void oneUp() {
        if (lives == -1) lives = 1;
        oneUpsBought++;
        lives++;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getOneUpsBought() {
        return oneUpsBought;
    }

    /**
     * If the GamePlayer's Team's ArmorStand has had all of it's flags
     * captured, then the player will only have as many lives as they
     * have purchased from the shop. If they have not purchased any, they
     * will die permanently.
     */
    public void die() {
        if (!team.isArmorStandAlive()) {
            if (lives > 0) {
                player.sendMessage(getTeamColor().getTextColor() + "" +
                        lives + "x Live(s) left");
                lives--;
            } else {
                lives = 0;
            }
        }
    }

    /**
     * Sets the ArmorWeight BukkitTask that controls when the player
     * is slowed down due to the weight of the opposing team's flag
     * in the Player's inventory.
     *
     * @param level is the level that the ArmorWeight upgrade will be
     *              leveled up / down to.
     */
    public void setArmorWeightTask(int level) {
        armorWeightTask = Game.scheduler.runTaskTimer(Game.plugin, () -> {
            ItemStack flagItem = player.getInventory().getItem(4);
            if (flagItem.getType().name().endsWith("PANE") ||
                    flagItem.getType() == Material.NETHER_STAR) {
                armorWeightTask.cancel();
            } else {
                Game.addPotionEffect(player, new PotionEffect(
                        PotionEffectType.SLOW, 60, level - 1));
            }
        }, 0, 60);
    }

    public Player getLastDamager() {
        return lastDamager;
    }

    public void setLastDamager(Player lastDamager) {
        this.lastDamager = lastDamager;
        setTimeLastDamaged(System.currentTimeMillis());
    }

    public long getTimeLastDamaged() {
        return timeLastDamaged;
    }

    public void setTimeLastDamaged(long timeLastDamaged) {
        this.timeLastDamaged = timeLastDamaged;
    }

    public boolean isArmorUnlocked(int x, int y) {
        return armorUnlocked[x][y];
    }

    public void setArmorUnlocked(int x, int y) {
        armorUnlocked[x][y] = true;
    }

    public void resetArmorUnlocked() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                armorUnlocked[i][j] = (j == 0);
            }
        }
    }

    public boolean isToolUnlocked(int x, int y) {
        return toolsUnlocked[x][y];
    }

    public void setToolUnlocked(int x, int y) {
        toolsUnlocked[x][y] = true;
    }

    public void resetToolsUnlocked() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                toolsUnlocked[i][j] = (i == 0);
            }
        }
    }

    // HideAndSeek / Tag Methods Below:

    public boolean isIt() {
        return it;
    }

    public void setIt(boolean it) {
        this.it = it;
    }

    @Override
    public String toString() {
        if (team == null || team.getColor() == TeamColor.NULL) {
            return String.format("{%s}: No Team", player.getName());
        }
        return String.format("{%s}: %s Team", player.getName(),
                (team.getColor() + "").substring(2));
    }
}
