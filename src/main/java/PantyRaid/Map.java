package PantyRaid;

import General.Game;
import org.bukkit.World;

public enum Map {
    CLASSIC("General"),
    SPACE("Space"),
    ZOMBIE("Zombie");

    private final String worldName;
    private World world;

    Map(String worldName) {
        this.worldName = worldName;
    }

    public String getWorldName() {
        return worldName;
    }

    void setWorld() {
        for (World world : Game.server.getWorlds())
            if (world.getName().equalsIgnoreCase(worldName))
                this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
