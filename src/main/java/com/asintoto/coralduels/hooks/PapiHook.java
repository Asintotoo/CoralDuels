package com.asintoto.coralduels.hooks;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class PapiHook extends PlaceholderExpansion {
    private final CoralDuels plugin;
    private Set<String> modes;

    public PapiHook(CoralDuels plugin) {
        this.plugin = plugin;

        this.modes = new HashSet<>();
        modes.add("wins");
        modes.add("deaths");
        modes.add("kills");
        modes.add("games");
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

        String[] args = params.split("_");

        if(args.length < 4) {
            return null;
        }

        if(!args[0].equalsIgnoreCase("top")) {
            return null;
        }

        if(!Manager.isValidInteger(args[2])) {
            return null;
        }

        int pos = Integer.parseInt(args[2]);

        if(pos <= 0) {
            return null;
        }

        String mode = args[1];

        if(!modes.contains(mode)) {
            return null;
        }

        String result = args[3];

        if(!result.equalsIgnoreCase("name") && !result.equalsIgnoreCase("value")) {
            return null;
        }

        switch (mode.toLowerCase()) {
            case "wins":
                if(result.equalsIgnoreCase("value")) {
                    return plugin.getDataManager().getLeaderboardWinsCount(pos);
                } else {
                    return plugin.getDataManager().getLeaderboardWinsPlayer(pos);
                }
            case "games":
                if(result.equalsIgnoreCase("value")) {
                    return plugin.getDataManager().getLeaderboardGamesCount(pos);
                } else {
                    return plugin.getDataManager().getLeaderboardGamesPlayer(pos);
                }
            case "deaths":
                if(result.equalsIgnoreCase("value")) {
                    return plugin.getDataManager().getLeaderboardDeathsCount(pos);
                } else {
                    return plugin.getDataManager().getLeaderboardDeathsPlayer(pos);
                }
            case "kills":
                if(result.equalsIgnoreCase("value")) {
                    return plugin.getDataManager().getLeaderboardKillsCount(pos);
                } else {
                    return plugin.getDataManager().getLeaderboardKillsPlayer(pos);
                }
        }


        return null;
    }
}
