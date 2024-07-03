package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.DatabaseType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String path) throws SQLException {
        DatabaseType type = getDatabaseType();


        if (type == DatabaseType.SQLITE) {

            connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        } else {
            String databaseName = CoralDuels.getInstance().getConfig().getString("database.database-name");
            String ipAddress = CoralDuels.getInstance().getConfig().getString("database.ip-address");
            String port = Integer.toString(CoralDuels.getInstance().getConfig().getInt("database.port"));
            String user = CoralDuels.getInstance().getConfig().getString("database.user");
            String password = CoralDuels.getInstance().getConfig().getString("database.password");

            if (type == DatabaseType.MYSQL) {

                String url = "jdbc:mysql://" + ipAddress + ":" + port + "/" + databaseName;


                connection = DriverManager.getConnection(url, user, password);

            } else {
                connection = null;
                String msg = CoralDuels.getInstance().getMessages().getString("error.invalid-database-type");
                Manager.sendConsoleMessage(Manager.formatMessage(msg));
                Bukkit.getServer().getPluginManager().disablePlugin(CoralDuels.getInstance());
            }
        }

        try (Statement statement = connection.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS players (
                    uuid TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    games INTEGER NOT NULL DEFAULT 0,
                    kills INTEGER NOT NULL DEFAULT 0,
                    deaths INTEGER NOT NULL DEFAULT 0)
                    """);
        }
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    public static DatabaseType getDatabaseType() {
        String type = CoralDuels.getInstance().getConfig().getString("database.type");
        return switch (type.toLowerCase()) {
            case "sqlite" -> DatabaseType.SQLITE;
            case "mysql" -> DatabaseType.MYSQL;
            default -> DatabaseType.INVALID;
        };
    }

    public void addPlayer(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid, username) VALUES (?, ?)")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }
    }

    public boolean playerExists(String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
            return false;
        }
    }

    public boolean playerExists(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e) {
        String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
        Manager.sendConsoleMessage(Manager.formatMessage(msg));
        e.printStackTrace();
        return false;
    }
    }

    public int getGames(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT games FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("games");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public int getGames(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT games FROM players WHERE username = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("games");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public int getKills(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public int getKills(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT kills FROM players WHERE username = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("kills");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public int getDeaths(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM players WHERE uuid = ?")) {
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public int getDeaths(String name) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT deaths FROM players WHERE username = ?")) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            }
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return 0;
    }

    public void setGames(Player player, int b){

        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET games = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, b);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }
    }

    public void setKills(Player player, int b){

        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET kills = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, b);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }
    }

    public void setDeaths(Player player, int b){

        if (!playerExists(player)) {
            addPlayer(player);
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET deaths = ? WHERE uuid = ?")) {
            preparedStatement.setInt(1, b);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }
    }

    public void addGames(Player p, int b) {
        int current = getGames(p);

        if(b < 0) b = 0;

        setGames(p, current + b);
    }

    public void addKills(Player p, int b) {
        int current = getKills(p);

        if(b < 0) b = 0;

        setKills(p, current + b);
    }

    public void addDeaths(Player p, int b) {
        int current = getDeaths(p);

        if(b < 0) b = 0;

        setDeaths(p, current + b);
    }

    public void removeGames(Player p, int b) {
        int current = getGames(p);

        if(b > 0) b = 0;

        setGames(p, current - b);
    }

    public void removeKills(Player p, int b) {
        int current = getKills(p);

        if(b > 0) b = 0;

        setKills(p, current - b);
    }

    public void removeDeaths(Player p, int b) {
        int current = getDeaths(p);

        if(b > 0) b = 0;

        setDeaths(p, current - b);
    }

    public void resetGames(Player p) {
        setGames(p, 0);
    }

    public void resetKills(Player p) {
        setKills(p, 0);
    }

    public void resetDeaths(Player p) {
        setDeaths(p, 0);
    }

    public void resetAllStats(Player p) {
        resetGames(p);
        resetKills(p);
        resetDeaths(p);
    }

    public String getLeaderboardGamesPlayer(int pos){
        String query = "SELECT username FROM players ORDER BY games DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                if(getGames(username) == 0) {
                    return "--";
                }
                return username;
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }

    public String getLeaderboardGamesCount(int pos){
        String query = "SELECT games FROM players ORDER BY games DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int games = resultSet.getInt("games");
                if(games == 0) {
                    return "--";
                }
                return Integer.toString(games);
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }

    public String getLeaderboardKillsPlayer(int pos){
        String query = "SELECT username FROM players ORDER BY kills DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                if(getKills(username) == 0) {
                    return "--";
                }
                return username;
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }

    public String getLeaderboardKillsCount(int pos){
        String query = "SELECT kills FROM players ORDER BY kills DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int kills = resultSet.getInt("kills");
                if(kills == 0) {
                    return "--";
                }
                return Integer.toString(kills);
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }

    public String getLeaderboardDeathsPlayer(int pos){
        String query = "SELECT username FROM players ORDER BY deaths DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                if(getDeaths(username) == 0) {
                    return "--";
                }
                return username;
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }

    public String getLeaderboardDeathsCount(int pos){
        String query = "SELECT deaths FROM players ORDER BY deaths DESC LIMIT 1 OFFSET ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, pos - 1);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int deaths = resultSet.getInt("deaths");
                if(deaths == 0) {
                    return "--";
                }
                return Integer.toString(deaths);
            }
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        return "--";
    }


}
