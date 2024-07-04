package com.asintoto.coralduels.hooks;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiHook extends PlaceholderExpansion {
    private final CoralDuels plugin;

    public PapiHook(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "coralduels";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    public String onRequest(OfflinePlayer player, String params) {

        if(params.equalsIgnoreCase("games")) {
            if(player.isOnline()) {
                Player p = (Player) player;
                return Integer.toString(plugin.getDataManager().getGames(p));
            } else {
                return Integer.toString(plugin.getDataManager().getGames(player.getName()));
            }
        }

        if(params.equalsIgnoreCase("kills")) {
            if(player.isOnline()) {
                Player p = (Player) player;
                return Integer.toString(plugin.getDataManager().getKills(p));
            } else {
                return Integer.toString(plugin.getDataManager().getKills(player.getName()));
            }
        }

        if(params.equalsIgnoreCase("deaths")) {
            if(player.isOnline()) {
                Player p = (Player) player;
                return Integer.toString(plugin.getDataManager().getDeaths(p));
            } else {
                return Integer.toString(plugin.getDataManager().getDeaths(player.getName()));
            }
        }

        if(params.equalsIgnoreCase("wins")) {
            if(player.isOnline()) {
                Player p = (Player) player;
                return Integer.toString(plugin.getDataManager().getWins(p));
            } else {
                return Integer.toString(plugin.getDataManager().getWins(player.getName()));
            }
        }

        int topPos = getTopPos(params);

        // DA OTTIMIZZARE

        if(topPos > 0) {
            if(params.contains("games")) {
                if(params.contains("value")) {
                    return plugin.getDataManager().getLeaderboardGamesCount(topPos);
                } else if(params.contains("name")) {
                    return plugin.getDataManager().getLeaderboardGamesPlayer(topPos);
                }
            }

            if(params.contains("death")) {
                if(params.contains("value")) {
                    return plugin.getDataManager().getLeaderboardDeathsCount(topPos);
                } else if(params.contains("name")) {
                    return plugin.getDataManager().getLeaderboardDeathsPlayer(topPos);
                }
            }

            if(params.contains("kills")) {
                if(params.contains("value")) {
                    return plugin.getDataManager().getLeaderboardKillsCount(topPos);
                } else if(params.contains("name")) {
                    return plugin.getDataManager().getLeaderboardKillsPlayer(topPos);
                }
            }

            if(params.contains("wins")) {
                if(params.contains("value")) {
                    return plugin.getDataManager().getLeaderboardWinsCount(topPos);
                } else if(params.contains("name")) {
                    return plugin.getDataManager().getLeaderboardWinsPlayer(topPos);
                }
            }
        }

        return null;
    }

    private static int getTopPos(String s) {
        s = s.replace("top_", "")
                .replace("_value", "")
                .replace("_name", "")
                .replace("games_", "")
                .replace("kills_", "")
                .replace("deaths_", "")
                .replace("wins_", "");

        if(Manager.isValidInteger(s)) {
            return Integer.parseInt(s);
        }

        return -1;
    }
}
