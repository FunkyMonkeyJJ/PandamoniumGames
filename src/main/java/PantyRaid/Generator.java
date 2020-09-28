package PantyRaid;

import General.Game;
import com.google.common.collect.ImmutableList;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class Generator {
    private final boolean isAltar;
    private final World world;
    private final int taskId;
    private boolean announced = false;

    public Generator(Material genMaterial, Location location,
                     int itemLimit, long rateInSeconds) {
        this(genMaterial, location, itemLimit, rateInSeconds, rateInSeconds);
    }

    public Generator(Material genMaterial, Location location, int itemLimit,
                     long rateInSeconds, long delayInSeconds) {
        this.isAltar = (delayInSeconds != rateInSeconds);
        this.world = location.getWorld();
        this.taskId = Game.scheduler.scheduleSyncRepeatingTask(Game.plugin,
                () -> {
                    if (delayInSeconds > 1 && !announced && isAltar) {
                        announced = true;
                        assert world != null;
                        Firework firework = (Firework) world.spawnEntity(
                                location.clone().add(0, 4, 0),
                                EntityType.FIREWORK);
                        FireworkMeta meta = firework.getFireworkMeta();
                        ImmutableList<Color> colors = ImmutableList.of(
                                Color.RED, Color.FUCHSIA, Color.AQUA);
                        ImmutableList<Color> fadeColors = ImmutableList.of(
                                Color.AQUA, Color.PURPLE);
                        FireworkEffect effect = FireworkEffect.builder().
                                flicker(true).trail(true).withColor(colors).
                                withFade(colors).build();
                        meta.addEffect(effect);
                        firework.setFireworkMeta(meta);
                    }

                    if (!Game.gameGoingAlready) {
                        stop();
                        return;
                    }

                    if (genMaterial == Material.IRON_NUGGET ||
                            genMaterial == Material.GOLD_NUGGET) {
                        boolean armorStandNearby = false;
                        assert world != null;
                        for (Entity entity : world.getNearbyEntities(
                                location, 7, 5, 7)) {
                            if (entity instanceof ArmorStand) {
                                armorStandNearby = true;
                                break;
                            }
                        }

                        if (!armorStandNearby) {
                            stop();
                            return;
                        }
                    }

                    assert world != null;
                    Item item = world.dropItemNaturally(location.clone().add(
                            0, 1, 0), new ItemStack(genMaterial));
                    item.setVelocity(new Vector(0, 0, 0));
                    if (isAltar) Bukkit.broadcastMessage(ChatColor.RED +
                            "A Nether Star has spawned at the altar!");
                }, delayInSeconds * 20, rateInSeconds * 20);
        Utility.generators.add(this);
    }

    public void stop() {
        Game.scheduler.cancelTask(taskId);
    }
}
