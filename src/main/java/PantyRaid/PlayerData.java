package PantyRaid;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.sql.*;
import java.util.LinkedList;
import java.util.UUID;

public class PlayerData implements Serializable {
    public static LinkedList<PlayerData> data = new LinkedList<>();
    private static Connection connection;
    private static String table;

    private Player player;
    private String nickname;

    protected int kills;
    protected int finalKills;
    protected int deaths;

    protected int recordKills;
    protected int recordFinalKills;
    protected int recordDeaths;

    protected int gamesPlayed;
    protected int wins;
    protected int losses;

    protected int flagsStolen;
    protected int yourFlagsStolen;
    protected int teamsEliminated;
    protected int netherStarCashedIn;
    protected int purchasesMade;

    public PlayerData() {
        mySqlSetup();
    }

    PlayerData(Player player) {
        this.player = player;
    }

    /**
     * Establishes the connection with the sql database.
     */
    public void mySqlSetup() {
        String host = "91.134.15.73";
        int port = 3306;
        String database = "s2_playerdata";
        String username = "u2_AyQiN0aoXY";
        String password = "kwAuSh@5W9RCb1bc+E=v8dKu";
        table = "pantyraid";

        try {
            synchronized (this) {
                if (getConnection() != null &&
                        !getConnection().isClosed()) return;
                Class.forName("java.sql.Driver");
                setConnection(DriverManager.getConnection(
                        "jdbc:mysql://" + host + ":" + port + "/" +
                                database, username, password));
                Bukkit.getConsoleSender().sendMessage(
                        ChatColor.GREEN + "MySQL Connected!");
            }
        } catch (SQLException | ClassNotFoundException throwables) {
            Bukkit.getConsoleSender().sendMessage(
                    ChatColor.DARK_RED + "MySQL Failed to Connect!");
            throwables.printStackTrace();
        }
    }

    /**
     * Checks if the sql database has data for the player.
     *
     * @return whether or not the player was found.
     */
    public boolean playerExists() {
        final UUID uuid = player.getUniqueId();
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM " + table + " WHERE UUID=?")) {
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            if (results.next()) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +
                        "Player Found: " + uuid.toString());
                results.close();
                return true;
            }
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED +
                    "Player NOT Found: " + uuid.toString());
            results.close();
        } catch (SQLException ignored) {
        } catch (Exception lostConnectionException) {
            mySqlSetup();
            return playerExists();
        }
        return false;
    }

    /**
     * Adds the player's data into the sql database.
     */
    public void createPlayer() {
        final UUID uuid = player.getUniqueId();
        try {
            if (!(playerExists())) {
                PreparedStatement statement = getConnection().prepareStatement
                        ("SELECT * FROM " + table + " WHERE UUID=?");
                statement.setString(1, uuid.toString());

                ResultSet results = statement.executeQuery();
                results.next();
                PreparedStatement insert = getConnection().prepareStatement(
                        "INSERT INTO " + table + " (UUID,Name,Nickname," +
                                "Kills,FinalKills,Deaths,RecordKills," +
                                "RecordFinalKills,RecordDeaths,GamesPlayed," +
                                "Wins,Losses,FlagsStolen,YourFlagsStolen," +
                                "TeamsEliminated,NetherStarsCashedIn," +
                                "PurchasesMade) VALUE (?,?,?,?,?,?,?,?,?,?," +
                                "?,?,?,?,?,?)");
                insert.setString(1, uuid.toString());
                insert.setString(2, player.getName());
                insert.setString(3, player.getDisplayName());
                insert.setInt(4, kills);
                insert.setInt(5, finalKills);
                insert.setInt(6, deaths);
                insert.setInt(7, recordKills);
                insert.setInt(8, recordFinalKills);
                insert.setInt(9, recordDeaths);
                insert.setInt(10, gamesPlayed);
                insert.setInt(11, wins);
                insert.setInt(12, losses);
                insert.setInt(13, flagsStolen);
                insert.setInt(14, yourFlagsStolen);
                insert.setInt(15, teamsEliminated);
                insert.setInt(16, netherStarCashedIn);
                insert.setInt(17, purchasesMade);
                insert.executeUpdate();
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN +
                        "Player Created: " + player.getName());

                insert.close();
                results.close();
                statement.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception lostConnectionException) {
            mySqlSetup();
            createPlayer();
        }
    }

    /**
     * Updates the player's data in the sql database.
     */
    public void updatePlayerData() {
        final UUID uuid = player.getUniqueId();
        if (!(playerExists())) createPlayer();
        try (PreparedStatement statement = getConnection().prepareStatement(
                "UPDATE " + table + " SET Nickname=?,Kills=?," +
                        "FinalKills=?,Deaths=?,RecordKills=?," +
                        "RecordFinalKills=?,RecordDeaths=?," +
                        "GamesPlayed=?,Wins=?,Losses=?,FlagsStolen=?," +
                        "YourFlagsStolen=?,NetherStarsCashedIn=?," +
                        "TeamsEliminated=?,PurchasesMade=? WHERE UUID=?")) {
            statement.setString(1, player.getDisplayName());
            statement.setInt(2, kills);
            statement.setInt(3, finalKills);
            statement.setInt(4, deaths);
            statement.setInt(5, recordKills);
            statement.setInt(6, recordFinalKills);
            statement.setInt(7, recordDeaths);
            statement.setInt(8, gamesPlayed);
            statement.setInt(9, wins);
            statement.setInt(10, losses);
            statement.setInt(11, flagsStolen);
            statement.setInt(12, yourFlagsStolen);
            statement.setInt(13, teamsEliminated);
            statement.setInt(14, netherStarCashedIn);
            statement.setInt(15, purchasesMade);
            statement.setString(16, uuid.toString());
            statement.executeUpdate();
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +
                    "Player Updated: " + player.getName());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception lostConnectionException) {
            mySqlSetup();
            updatePlayerData();
        }
    }

    /**
     * Loads the player's data from the sql database.
     */
    public void loadPlayerData() {
        final UUID uuid = player.getUniqueId();
        if (!(playerExists())) createPlayer();
        try (PreparedStatement statement = getConnection().prepareStatement(
                "SELECT * FROM " + table + " WHERE UUID=?")) {
            statement.setString(1, uuid.toString());

            ResultSet results = statement.executeQuery();
            results.next();
            player.setDisplayName(results.getString("Nickname"));
            kills = results.getInt("Kills");
            finalKills = results.getInt("FinalKills");
            deaths = results.getInt("Deaths");
            recordKills = results.getInt("RecordKills");
            recordFinalKills = results.getInt("RecordFinalKills");
            recordDeaths = results.getInt("RecordDeaths");
            gamesPlayed = results.getInt("GamesPlayed");
            wins = results.getInt("Wins");
            losses = results.getInt("Losses");
            flagsStolen = results.getInt("FlagsStolen");
            yourFlagsStolen = results.getInt("YourFlagsStolen");
            teamsEliminated = results.getInt("TeamsEliminated");
            netherStarCashedIn = results.getInt(
                    "NetherStarsCashedIn");
            purchasesMade = results.getInt("PurchasesMade");
            Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD +
                    "Player Loaded: " + player.getName());
            results.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception lostConnectionException) {
            mySqlSetup();
            loadPlayerData();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        PlayerData.connection = connection;
    }

    /**
     * Searches through all of the PlayerData's in data and return
     * the PlayerData that correlates to the given player. If one
     * is not found, add a new PlayerData for the given player, and
     * then return them.
     *
     * @param player is the Player that is used to find the correct
     *               PlayerData, or make a new PlayerData for them.
     * @return the PlayerData that correlates to the given player.
     */
    public static PlayerData getPlayerData(Player player) {
        for (PlayerData playerData : data)
            if (playerData.getPlayer() == player) return playerData;
        PlayerData newPlayerData = new PlayerData(player);
        data.add(newPlayerData);
        return newPlayerData;
    }

    /**
     * Resets all values in this back to 0.
     */
    public void clear() {
        this.kills = 0;
        this.finalKills = 0;
        this.deaths = 0;

        this.recordKills = 0;
        this.recordFinalKills = 0;
        this.recordDeaths = 0;

        this.gamesPlayed = 0;
        this.wins = 0;
        this.losses = 0;

        this.flagsStolen = 0;
        this.yourFlagsStolen = 0;
        this.netherStarCashedIn = 0;
        this.purchasesMade = 0;

        updatePlayerData();
    }

    public Player getPlayer() {
        return player;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void addGame() {
        gamesPlayed++;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void addFinalKill() {
        finalKills++;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public int getRecordKills() {
        return recordKills;
    }

    public void setRecordKills(int recordKills) {
        this.recordKills = recordKills;
    }

    public int getRecordFinalKills() {
        return recordFinalKills;
    }

    public void setRecordFinalKills(int recordFinalKills) {
        this.recordFinalKills = recordFinalKills;
    }

    public int getRecordDeaths() {
        return recordDeaths;
    }

    public void setRecordDeaths(int recordDeaths) {
        this.recordDeaths = recordDeaths;
    }

    public float getKillDeathRatio() {
        if (deaths == 0) return 0;
        return ((float) Math.round((((float) kills) / deaths) * 100) / 100);
    }

    public float getRecordKillDeathRatio() {
        if (recordDeaths == 0) return 0;
        return ((float) Math.round((((float)
                recordKills) / recordDeaths) * 100) / 100);
    }

    public float getWinLossRatio() {
        if (losses == 0) return 0;
        return ((float) Math.round((((float) wins) / losses) * 100) / 100);
    }

    public void win() {
        wins++;
    }

    public void lose() {
        losses++;
    }

    @Override
    public String toString() {
        return String.format("%s: %d Kills, %d Deaths, %.2f K/D Ratio..." +
                        "%d Games Played", player.getName(), kills, deaths,
                getKillDeathRatio(), gamesPlayed);
    }
}
