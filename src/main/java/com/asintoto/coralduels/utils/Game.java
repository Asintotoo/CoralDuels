package com.asintoto.coralduels.utils;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.entity.Player;

public class Game {
    private Arena arena;
    private Player p1;
    private Player p2;

    private final CoralDuels plugin;


    public Game(Arena arena, Player p1, Player p2) {
        this.arena = arena;
        this.p1 = p1;
        this.p2 = p2;

        this.plugin = CoralDuels.getInstance();

        plugin.getGameManager().addGame(this);

        plugin.getDataManager().addGames(p1, 1);
        plugin.getDataManager().addGames(p2, 1);
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }

    public CoralDuels getPlugin() {
        return plugin;
    }
}
