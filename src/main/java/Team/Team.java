package Team;

import General.Game;
import General.GamePlayer;
import PantyRaid.Utility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;

public class Team extends ArrayList<GamePlayer> {
    private final TeamColor color;
    private ArmorStand armorStand;
    private ItemStack[] copyArmorStand;
    private boolean liveArmorStand = true;
    private Location spawnPoint;
    private int flagsAdded = -1;

    private int maxRespawnUpgrade = 0;
    private int maxArmorWeightUpgrade = 0;
    private int maxRegenerationUpgrade = 0;

    private int maxRunForItUpgrade = 0;
    private int maxKillsRewardsUpgrade = 0;
    private int maxPressureUpgrade = 0;

    private int respawnUpgrade = 0;
    private int armorWeightUpgrade = 0;
    private int regenerationUpgrade = 0;
    private BukkitTask regenTask;

    private int runForItUpgrade = 0;
    private int killRewardsUpgrade = 0;
    private int pressureUpgrade = 0;

    public Team(TeamColor color, GamePlayer... players) {
        this.color = color;
        this.addAll(Arrays.asList(players));
    }

    public TeamColor getColor() {
        return color;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void setArmorStand(ArmorStand armorStand) {
        if (armorStand == null) {
            liveArmorStand = false;
            return;
        }
        liveArmorStand = true;
        this.armorStand = armorStand;
        this.spawnPoint = armorStand.getLocation();
    }

    public ItemStack[] getArmorStandContents() {
        return armorStand.getEquipment() == null ? new ItemStack[]{} :
                armorStand.getEquipment().getArmorContents();
    }

    public ItemStack[] copyArmorStand() {
        this.copyArmorStand = armorStand.getEquipment().getArmorContents();
        return copyArmorStand;
    }

    public void resetArmorStand() {
        if (copyArmorStand.length > 0)
            copyArmorStand = new ItemStack[0];
    }

    public void addFlag() {
        if (copyArmorStand == null || copyArmorStand.length == 0)
            copyArmorStand();
        ItemStack[] armor = copyArmorStand;
        String[] armorEndings = new String[]{
                "BOOTS", "LEGGINGS", "CHESTPLATE", "HELMET"};
        if (flagsAdded == -1) {
            ItemStack firstFlag = new ItemStack(Material.LEATHER_LEGGINGS);
            LeatherArmorMeta meta = (LeatherArmorMeta)
                    firstFlag.getItemMeta();
            meta.setDisplayName(getColor().getTextColor() + ""
                    + getColor() + " Team's Flag");
            meta.setColor(getColor().getArmorColor());
            firstFlag.setItemMeta(meta);
            armorStand.getEquipment().setLeggings(firstFlag);
            flagsAdded++;
            return;
        }
        for (int i = armor.length - 1; i > -1; i--) {
            if (!armor[i].getType().name().endsWith(armorEndings[i])) {
                String flagToAdd = "";
                switch (getArmorWeightUpgrade()) {
                    case 0:
                        flagToAdd = "LEATHER_" + armorEndings[i];
                        break;
                    case 1:
                        flagToAdd = "CHAINMAIL_" + armorEndings[i];
                        break;
                    case 2:
                        flagToAdd = "IRON_" + armorEndings[i];
                        break;
                    case 3:
                        flagToAdd = "GOLD_" + armorEndings[i];
                }
                Material flagAsMaterial = Material.getMaterial(flagToAdd);
                ItemStack flagAsItemStack = new ItemStack(flagAsMaterial);
                LeatherArmorMeta meta = (LeatherArmorMeta)
                        flagAsItemStack.getItemMeta();
                assert meta != null;
                meta.setDisplayName(getColor().getTextColor() + ""
                        + getColor() + " Team's Flag");
                meta.setColor(getColor().getArmorColor());
                flagAsItemStack.setItemMeta(meta);
                switch (armorEndings[i]) {
                    case "HELMET":
                        armorStand.getEquipment().setHelmet(flagAsItemStack);
                        break;
                    case "CHESTPLATE":
                        armorStand.getEquipment().
                                setChestplate(flagAsItemStack);
                        break;
                    case "LEGGINGS":
                        armorStand.getEquipment().
                                setLeggings(flagAsItemStack);
                        break;
                    case "BOOTS":
                        armorStand.getEquipment().setBoots(flagAsItemStack);
                }
                flagsAdded++;
                return;
            }
        }
    }

    public void upgradeRespawnTime(int level) {
        int currentLevel = getRespawnUpgrade();
        maxRespawnUpgrade = Math.max(maxRespawnUpgrade, level);
        if (level < 4) {
            respawnUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Respawn Upgrade has " +
                        "been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Respawn Upgrade has " +
                        "been downgraded to level " + level + ".");
        }
    }

    public void upgradeRegeneration(int level) {
        int currentLevel = getRegenerationUpgrade();
        maxRegenerationUpgrade = Math.max(maxRegenerationUpgrade, level);
        if (level > 0) {
            boolean contains = false;
            for (Team regenTeam : Utility.regenTeams) {
                if (regenTeam.equals(this)) {
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                Utility.regenTeams.add(this);
                regenTask = Game.scheduler.runTaskTimer(Game.plugin, () -> {
                    if (!isAlive()) {
                        regenTask.cancel();
                        return;
                    }

                    for (Entity entity : armorStand.getNearbyEntities(
                            8, 5, 8)) {
                        if (entity instanceof Player) {
                            Player player = (Player) entity;
                            GamePlayer gamePlayer = Game.
                                    getGamePlayer(player);
                            if (gamePlayer.getTeam().equals(this)) {
                                Game.addPotionEffect(player, new
                                        PotionEffect(PotionEffectType.
                                        REGENERATION, 100,
                                        getRegenerationUpgrade()
                                                - 1));
                            }
                        }
                    }
                }, 0, 100);
            }
        }
        if (level < 4) {
            regenerationUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Regeneration Upgrade " +
                        "has been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Regeneration Upgrade " +
                        "has been downgraded to level " + level + ".");
        }
    }

    public void upgradeArmorWeight(int level) {
        int currentLevel = getArmorWeightUpgrade();
        maxArmorWeightUpgrade = Math.max(maxArmorWeightUpgrade, level);
        if (!liveArmorStand) return;
        armorStand.getEquipment().setArmorContents(replaceArmor(
                armorStand.getEquipment().getArmorContents(), level));
        copyArmorStand = replaceArmor(copyArmorStand, level);
        if (level < 4) {
            armorWeightUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Armor Weight Upgrade " +
                        "has been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Armor Weight Upgrade " +
                        "has been downgraded to level " + level + ".");
        }
    }

    private ItemStack[] replaceArmor(ItemStack[] flags, int level) {
        if (flags == null) return flags;
        ItemStack[] newArmorStand = new ItemStack[flags.length];
        for (int i = 0; i < flags.length; i++) {
            ItemStack flag = flags[i];
            if (flag.getType() == Material.AIR) {
                newArmorStand[i] = flag;
                continue;
            }
            String typeOfFlag = flag.getType().name();
            int beginMaterialIndex = 0;
            for (int j = 0; j < typeOfFlag.length(); j++) {
                if (typeOfFlag.toCharArray()[j] == '_') {
                    beginMaterialIndex = j + 1;
                    break;
                }
            }
            Material newFlagMaterial = Material.getMaterial(upgradeArmor(
                    level) + typeOfFlag.substring(beginMaterialIndex));
            ItemStack newFlag = flag.clone();
            newFlag.setType(newFlagMaterial);
            newArmorStand[i] = newFlag;
        }
        return newArmorStand;
    }

    private String upgradeArmor(int level) {
        switch (level) {
            case 1:
                return "CHAINMAIL_";
            case 2:
                return "IRON_";
            case 3:
                return "GOLDEN_";
        }
        return "LEATHER_";
    }

    public void upgradePressure(int level) {
        int currentLevel = getPressureUpgrade();
        maxPressureUpgrade = Math.max(maxPressureUpgrade, level);
        if (level < 4) {
            pressureUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Pressure Upgrade has " +
                        "been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Pressure Upgrade has " +
                        "been downgraded to level " + level + ".");
        }
    }

    public void upgradeKillRewards(int level) {
        int currentLevel = getKillRewardsUpgrade();
        maxKillsRewardsUpgrade = Math.max(maxKillsRewardsUpgrade, level);
        if (level < 4) {
            killRewardsUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Kill Rewards Upgrade has " +
                        "been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Kill Rewards Upgrade has " +
                        "been downgraded to level " + level + ".");
        }
    }

    public void upgradeRunForIt(int level) {
        int currentLevel = getRunForItUpgrade();
        maxRunForItUpgrade = Math.max(maxRunForItUpgrade, level);
        if (level < 4) {
            runForItUpgrade = level;
            if (currentLevel < level)
                broadcast(color.getTextColor() + "Run For It! Upgrade has " +
                        "been upgraded to level " + level + ".");
            else if (currentLevel > level)
                broadcast(color.getTextColor() + "Run For It! Upgrade has " +
                        "been downgraded to level " + level + ".");
        }
    }

    public int getMaxRespawnUpgrade() {
        return maxRespawnUpgrade;
    }

    public int getMaxRegenerationUpgrade() {
        return maxRegenerationUpgrade;
    }

    public int getMaxArmorWeightUpgrade() {
        return maxArmorWeightUpgrade;
    }

    public int getMaxPressureUpgrade() {
        return maxPressureUpgrade;
    }

    public int getMaxKillsRewardsUpgrade() {
        return maxKillsRewardsUpgrade;
    }

    public int getMaxRunForItUpgrade() {
        return maxRunForItUpgrade;
    }

    public int getRespawnUpgrade() {
        return respawnUpgrade;
    }

    public int getRespawnTime() {
        switch (respawnUpgrade) {
            case 1:
                return 12;
            case 2:
                return 8;
            case 3:
                return 5;
        }
        return 15;
    }

    public int getRegenerationUpgrade() {
        return regenerationUpgrade;
    }

    public int getArmorWeightUpgrade() {
        return armorWeightUpgrade;
    }

    public int getPressureUpgrade() {
        return pressureUpgrade;
    }

    public int getKillRewardsUpgrade() {
        return killRewardsUpgrade;
    }

    public int getRunForItUpgrade() {
        return runForItUpgrade;
    }

    public Location getSpawnPoint() {
        return new Location(spawnPoint.getWorld(), spawnPoint.getX(),
                spawnPoint.getY(), spawnPoint.getZ()).add(
                randomShift(), 1, randomShift());
    }

    public void setSpawnPoint(Location spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    private double randomShift() {
        return (Math.random() * 1.5) < 0.75 ? -1 : 1;
    }

    public boolean isAlive() {
        if (this.size() == 0 || armorStand == null) return false;
        for (ItemStack item : armorStand.getEquipment().getArmorContents())
            if (item.getType() != Material.AIR) return true;
        for (GamePlayer player : this)
            if (player.getLives() != 0 && player.getPlayer().isOnline())
                return true;
        return false;
    }

    public boolean isArmorStandAlive() {
        return liveArmorStand;
    }

    public boolean remove(GamePlayer gamePlayer) {
        for (int i = 0; i < this.size(); i++) {
            GamePlayer teamGamePlayer = this.get(i);
            if (teamGamePlayer.getPlayer().equals(gamePlayer.getPlayer())) {
                remove(i);
                return true;
            }
        }
        return false;
    }

    public void broadcast(String message) {
        for (GamePlayer gamePlayer : this)
            gamePlayer.getPlayer().sendMessage(message);
    }

    @Override
    public String toString() {
        return color.getTeamName() + " Team: " + Arrays.toString(toArray());
    }
}
