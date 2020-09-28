package Team;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;

public enum TeamColor {
    NULL("No", ChatColor.WHITE, Color.WHITE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.WHITE_BANNER, Material.WHITE_BANNER),
    PINK("Pink", ChatColor.LIGHT_PURPLE, Color.FUCHSIA,
            Material.PINK_STAINED_GLASS_PANE,
            Material.PINK_BANNER, Material.PINK_WOOL),
    RED("Red", ChatColor.DARK_RED, Color.RED,
            Material.RED_STAINED_GLASS_PANE,
            Material.RED_BANNER, Material.RED_WOOL),
    ORANGE("Orange", ChatColor.GOLD, Color.ORANGE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.ORANGE_BANNER, Material.ORANGE_WOOL),
    YELLOW("Yellow", ChatColor.YELLOW, Color.YELLOW,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.YELLOW_BANNER, Material.YELLOW_WOOL),
    LIME("Lime", ChatColor.GREEN, Color.LIME,
            Material.LIME_STAINED_GLASS_PANE,
            Material.LIME_BANNER, Material.LIME_WOOL),
    GREEN("Green", ChatColor.DARK_GREEN, Color.GREEN,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.GREEN_BANNER, Material.GREEN_WOOL),
    AQUA("Aqua", ChatColor.AQUA, Color.AQUA,
            Material.LIGHT_BLUE_STAINED_GLASS_PANE,
            Material.LIGHT_BLUE_BANNER, Material.LIGHT_BLUE_WOOL),
    BLUE("Blue", ChatColor.BLUE, Color.BLUE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.BLUE_BANNER, Material.BLUE_WOOL),
    PURPLE("Purple", ChatColor.DARK_PURPLE, Color.PURPLE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.PURPLE_BANNER, Material.PURPLE_WOOL),
    WHITE("White", ChatColor.WHITE, Color.WHITE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.WHITE_BANNER, Material.WHITE_WOOL),
    SILVER("Silver", ChatColor.GRAY, Color.SILVER,
            Material.LIGHT_GRAY_STAINED_GLASS_PANE,
            Material.LIGHT_GRAY_BANNER, Material.LIGHT_GRAY_WOOL),
    BLACK("Black", ChatColor.DARK_GRAY, Color.BLACK,
            Material.BLACK_STAINED_GLASS_PANE,
            Material.BLACK_BANNER, Material.BLACK_WOOL);

    private final String teamName;
    private final ChatColor textColor;
    private final Color armorColor;
    private final Material flagMaterial;
    private final Material bannerMaterial;
    private final Material noTeammatesBannerMaterial;

    TeamColor(String teamName, ChatColor textColor, Color armorColor,
              Material flagMaterial, Material bannerMaterial,
              Material noTeammatesBannerMaterial) {
        this.teamName = teamName;
        this.textColor = textColor;
        this.armorColor = armorColor;
        this.flagMaterial = flagMaterial;
        this.bannerMaterial = bannerMaterial;
        this.noTeammatesBannerMaterial = noTeammatesBannerMaterial;
    }

    public String getTeamName() {
        return teamName;
    }

    public ChatColor getTextColor() {
        return textColor;
    }

    public Color getArmorColor() {
        return armorColor;
    }

    public Material getFlagMaterial() {
        return flagMaterial;
    }

    public Material getBannerMaterial() {
        return bannerMaterial;
    }

    public Material getNoTeammatesBannerMaterial() {
        return noTeammatesBannerMaterial;
    }

    @Override
    public String toString() {
        return getTextColor() + getTeamName();
    }
}
