package General;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum MiniGame {
    PANTYRAID(ChatColor.LIGHT_PURPLE + "PantyRaid",
            Material.ARMOR_STAND, true),
    HIDEANDSEEK(ChatColor.DARK_AQUA + "HideAndSeek",
            Material.WARPED_TRAPDOOR, false);
//    DODGEBALL(ChatColor.AQUA + "Dodgeball", Material.SNOWBALL,
//            true),
//    SPLEEF(ChatColor.YELLOW + "Spleef", Material.SAND, false),
//    TAG(ChatColor.GOLD + "Tag", Material.PLAYER_HEAD, false);

    private final String string;
    private final Material minigameMaterial;
    private final boolean teamBased;
//    private final PandamoniumGame pandamoniumGame;
//    private final Listener gameListener;

    MiniGame(String string, Material minigameMaterial, boolean teamBased) {
        this.string = string;
        this.minigameMaterial = minigameMaterial;
        this.teamBased = teamBased;
    }

    public String getDisplayName() {
        return string;
    }

    public Material getMinigameMaterial() {
        return minigameMaterial;
    }

    public boolean isTeamBased() {
        return teamBased;
    }

//    public PandamoniumGame getPandamoniumGame() {
//        return pandamoniumGame;
//    }

//    public Listener getGameListener() {
//        return gameListener;
//    }
}
