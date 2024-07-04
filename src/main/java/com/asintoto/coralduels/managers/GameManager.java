package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.PlayerStatus;
import com.asintoto.coralduels.utils.Arena;
import com.asintoto.coralduels.utils.Game;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GameManager {
    private final CoralDuels plugin;

    private Map<Player, PlayerStatus> playerStatus;
    private Set<Game> gameList;
    private Map<Player, GameMode> gameModeMap;

    public GameManager(CoralDuels plugin) {
        this.plugin = plugin;
        this.playerStatus = new HashMap<>();
        this.gameList = new HashSet<>();
        this.gameModeMap = new HashMap<>();
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

    public void endDuel(Player loser, Player winner, Game g) {
        plugin.getGameManager().setPlayerStatus(loser, PlayerStatus.NOT_IN_GAME);
        plugin.getGameManager().setPlayerStatus(winner, PlayerStatus.NOT_IN_GAME);

        String msg = plugin.getMessages().getString("player.duel.duel-win").replace("%player%", loser.getName());
        winner.sendMessage(Manager.formatMessage(msg));

        msg = plugin.getMessages().getString("player.duel.duel-lost").replace("%player%", winner.getName());
        loser.sendMessage(Manager.formatMessage(msg));

        g.getArena().setHasPlayer(false);

        List<String> msgs = plugin.getMessages().getStringList("player.duel.duel-end");
        for(String s : msgs) {
            String formatted = Manager.formatMessage(s
                    .replace("%winner%", winner.getName())
                    .replace("%loser%", loser.getName()));

            winner.sendMessage(formatted);
            loser.sendMessage(formatted);
        }

        if(plugin.getConfig().getBoolean("duel.gamemode-settings.set-prev-gamemode-when-end")) {
            if(gameModeMap.containsKey(winner)) {
                winner.setGameMode(gameModeMap.get(winner));
                gameModeMap.remove(winner);
            }

            if(gameModeMap.containsKey(loser)) {
                loser.setGameMode(gameModeMap.get(loser));
                gameModeMap.remove(loser);
            }
        }
    }

    public void startDuel(Player sender, Player target) {
        String msg = plugin.getMessages().getString("player.duel.accept-sender")
                .replace("%player%", target.getName());

        sender.sendMessage(Manager.formatMessage(msg));

        msg = plugin.getMessages().getString("player.duel.accept-target")
                .replace("%player%", sender.getName());

        target.sendMessage(Manager.formatMessage(msg));

        Arena a = plugin.getArenaManager().getAvaiableArena();
        if(a == null) {
            msg = plugin.getMessages().getString("error.no-arena-avaiable");
            sender.sendMessage(Manager.formatMessage(msg));
            target.sendMessage(Manager.formatMessage(msg));
            return;
        }

        if(plugin.getConfig().getBoolean("duel.gamemode-settings.change-gamemode")) {
            GameMode mode;
            String gm = plugin.getConfig().getString("duel.gamemode-settings.gamemode");

            switch (gm.toUpperCase()) {
                case "SURVIVAL":
                    mode = GameMode.SURVIVAL;
                    break;
                case "CREATIVE":
                    mode = GameMode.CREATIVE;
                    break;
                case "SPECTATOR":
                    mode = GameMode.SPECTATOR;
                    break;
                case "ADVENTURE":
                    mode = GameMode.ADVENTURE;
                    break;
                default:
                    mode = GameMode.SURVIVAL;
            }

            if(plugin.getConfig().getBoolean("duel.gamemode-settings.set-prev-gamemode-when-end")) {
                gameModeMap.put(sender, sender.getGameMode());
                gameModeMap.put(target, target.getGameMode());
            }

            sender.setGameMode(mode);
            target.setGameMode(mode);
        }

        a.teleport(sender, target);
        a.setHasPlayer(true);

        new Game(a, sender, target);

        plugin.getGameManager().setPlayerStatus(sender, PlayerStatus.STARTING);
        plugin.getGameManager().setPlayerStatus(target, PlayerStatus.STARTING);

        List<String> msgs = plugin.getMessages().getStringList("player.duel.duel-start");
        for(String s : msgs) {
            sender.sendMessage(Manager.formatMessage(s
                    .replace("%player%", sender.getName())
                    .replace("%rival%", target.getName())));

            target.sendMessage(Manager.formatMessage(s
                    .replace("%player%", target.getName())
                    .replace("%rival%", sender.getName())));
        }

        gameCountdown(sender, target);
    }


}
