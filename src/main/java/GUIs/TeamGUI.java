package GUIs;

import General.Game;
import General.GamePlayer;
import Items.NamedItem;
import Team.Team;
import Team.TeamColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TeamGUI {
    private final GamePlayer gamePlayer;
    private final Inventory gui;

    public TeamGUI(Player player) {
        gui = Bukkit.createInventory(player, 27, "Team GUI");
        gamePlayer = Game.getGamePlayer(player);
        reload();
        player.openInventory(gui);
    }

    void reload() {
        TeamColor color = gamePlayer.getTeamColor();
        gui.setItem(13, new NamedItem(color.getBannerMaterial(),
                color.getTextColor() + "Your Current Team").
                getItemStack());

        NamedItem air = new NamedItem(
                Material.GRAY_STAINED_GLASS_PANE, " ");

        List<Integer> guiSpots = Arrays.asList(
                1, 2, 3, 5, 6, 7, 19, 20, 21, 23, 24, 25);

        TeamColor yourTeamColor = gamePlayer.getTeamColor();
        gui.setItem(13, new NamedItem(yourTeamColor.getBannerMaterial(),
                "Your Team: " + yourTeamColor.getTextColor() +
                        yourTeamColor.getTeamName()).getItemStack());

        int teamIndex = 1;
        for (int j = 0; j < 27; j++)
            if (guiSpots.contains(j)) {
                Team players = Game.lobbyTeams.get(teamIndex++);
                TeamColor teamColor = players.getColor();

                if (players.size() > 0) {
                    ItemStack item = new NamedItem(teamColor.
                            getBannerMaterial(),
                            teamColor + " Team").getItemStack();
                    item.setAmount(players.size());
                    gui.setItem(j, item);
                } else {
                    gui.setItem(j, new NamedItem(
                            teamColor.getNoTeammatesBannerMaterial(),
                            teamColor + " Team - 0 Teammates").
                            getItemStack());
                }
            } else gui.setItem(j, air.getItemStack());
    }
}
