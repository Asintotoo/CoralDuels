package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.PlayerStatus;
import com.asintoto.coralduels.utils.Game;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameManager {
    private final CoralDuels plugin;

    private Map<Player, PlayerStatus> playerStatus;
    private Set<Game> gameList;

    public GameManager(CoralDuels plugin) {
        this.plugin = plugin;
        this.playerStatus = new HashMap<>();
        this.gameList = new HashSet<>();
    }

    public PlayerStatus getPlayerStatus(Player p) {
        if(playerStatus.containsKey(p)) {
            return playerStatus.get(p);
        }

        return PlayerStatus.NOT_IN_GAME;
    }

    public boolean isPlayerInGame(Player p) {
        return getPlayerStatus(p) == PlayerStatus.IN_GAME || getPlayerStatus(p) == PlayerStatus.STARTING;
    }

    public void setPlayerStatus(Player p, PlayerStatus status) {
        playerStatus.put(p, status);
    }

    public void gameCountdown(Player... players) {

        final String number = Manager.formatMessage(plugin.getMessages().getString("player.countdown.numbers"));

        final String go = Manager.formatMessage(plugin.getMessages().getString("player.countdown.go"));

        final int countdown = plugin.getConfig().getInt("duel.countdown-duration");

        BukkitTask task = new BukkitRunnable() {
            int index = countdown;

            @Override
            public void run() {
                if (index > 0) {
                    int seconds = index;
                    String title = number.replace("%number%", seconds + "");
                    for(Player p : players) {
                        p.sendTitle(title, "", 10, 20, 10);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
                    }
                    index--;
                } else {

                    String title = go;

                    for(Player p : players) {
                        p.sendTitle(title, "", 10, 20, 10);
                        p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 2);
                        setPlayerStatus(p, PlayerStatus.IN_GAME);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    public void addGame(Game g) {
        if(!gameList.contains(g)) {
            gameList.add(g);
        }
    }

    public void removeGame(Game g) {
        if(gameList.contains(g)) {
            gameList.remove(g);
        }
    }

    public Game getPlayerGame(Player p) {
        for(Game g : gameList) {
            if(g.getP1().getName().equalsIgnoreCase(p.getName()) || g.getP2().getName().equalsIgnoreCase(p.getName())) {
                return g;
            }
        }

        return null;
    }


}
