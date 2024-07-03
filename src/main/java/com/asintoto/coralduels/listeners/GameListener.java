package com.asintoto.coralduels.listeners;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.PlayerStatus;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.utils.Debug;
import com.asintoto.coralduels.utils.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class GameListener implements Listener {
    private final CoralDuels plugin;
    private static List<String> blockedCommands;

    public GameListener(CoralDuels plugin) {
        this.plugin = plugin;

        blockedCommands = plugin.getConfig().getStringList("duel.commands-to-block");
    }

    public static void reloadBlockedCommand () {
        blockedCommands = CoralDuels.getInstance().getConfig().getStringList("duel.commands-to-block");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity().getPlayer();

        if(!plugin.getGameManager().isPlayerInGame(p)) {
            return;
        }

        Game g = plugin.getGameManager().getPlayerGame(p);

        if(g == null) {
            return;
        }

        Player otherPlayer = g.getP1().getName().equalsIgnoreCase(p.getName()) ? g.getP2() : g.getP1();

        Player killer = p.getKiller();

        plugin.getDataManager().addDeaths(p, 1);
        plugin.getDataManager().addWins(otherPlayer, 1);

        if(killer != null && killer.getName().equalsIgnoreCase(otherPlayer.getName())) {
            plugin.getDataManager().addKills(otherPlayer, 1);
        }

        plugin.getGameManager().setPlayerStatus(p, PlayerStatus.NOT_IN_GAME);
        plugin.getGameManager().setPlayerStatus(otherPlayer, PlayerStatus.NOT_IN_GAME);

        String msg = plugin.getMessages().getString("player.duel.duel-win").replace("%player%", p.getName());
        otherPlayer.sendMessage(Manager.formatMessage(msg));

        msg = plugin.getMessages().getString("player.duel.duel-lost").replace("%player%", otherPlayer.getName());
        p.sendMessage(Manager.formatMessage(msg));

        g.getArena().setHasPlayer(false);

    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String command = e.getMessage();
        Debug.log(command);

        if(!plugin.getConfig().getBoolean("duel.block-commands")) {
            return;
        }

        if(!plugin.getGameManager().isPlayerInGame(p)) {
            return;
        }

        for(String s : blockedCommands) {
            if(command.startsWith(s)) {
                e.setCancelled(true);
                String msg = plugin.getMessages().getString("error.blocked-command");
                p.sendMessage(Manager.formatMessage(msg));
                return;
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if(plugin.getGameManager().getPlayerStatus(p) == PlayerStatus.STARTING) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if(plugin.getConfig().getBoolean("duel.allow-block-destruction")) {
            return;
        }

        if(plugin.getGameManager().isPlayerInGame(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if(plugin.getConfig().getBoolean("duel.allow-block-placement")) {
            return;
        }

        if(plugin.getGameManager().isPlayerInGame(p)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(!plugin.getGameManager().isPlayerInGame(p)) {
            return;
        }

        Game g = plugin.getGameManager().getPlayerGame(p);

        Player otherPlayer = g.getP1().getName().equalsIgnoreCase(p.getName()) ? g.getP2() : g.getP1();

        plugin.getDataManager().addDeaths(p, 1);
        plugin.getDataManager().addWins(otherPlayer, 1);

        String msg = plugin.getMessages().getString("player.duel.duel-win").replace("%player%", p.getName());
        otherPlayer.sendMessage(Manager.formatMessage(msg));

        plugin.getGameManager().setPlayerStatus(p, PlayerStatus.NOT_IN_GAME);
        plugin.getGameManager().setPlayerStatus(otherPlayer, PlayerStatus.NOT_IN_GAME);

        g.getArena().setHasPlayer(false);

    }
}