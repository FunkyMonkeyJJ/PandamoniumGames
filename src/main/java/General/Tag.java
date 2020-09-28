package General;

import static org.bukkit.ChatColor.*;

public enum Tag {
    MURDERER(DARK_RED + "[" + RED + "Murderer!" + DARK_RED + "]",
            GOLD + "Commit your first (and hopefully not last) " +
                    "murder in PantyRaid. Once you start, you can't stop..."),
    SERIALKILLER(DARK_RED + "[" + RED + "⚔ Serial Killer ⚔" + DARK_RED +
            "]", GOLD + "Fall victim to it... Murder 50 people in " +
            "cold blood."),
    SLAYER(DARK_RED + BOLD.toString() + "[" + RED + BOLD.toString() +
            "Slayer." + DARK_RED + "]", GOLD + "Slay everyone in " +
            "your path. Leave none behind. Remorse is no longer your friend." ),
    THEFLASH(DARK_RED + "[" + RED + "The " + YELLOW + "⚡" + RED +
            " Flash" + DARK_RED + "]", GOLD + ""),
    LITTLERUNNER(YELLOW + "[" + DARK_PURPLE + "Little" + YELLOW + "⚡" +
            " Runner" + YELLOW + "]", GOLD + "");

    private final String tag;
    private final String description;

    Tag(String tag, String description) {
        this.tag = tag;
        this.description = description;
    }

    public String getTag() {
        return tag;
    }
}
