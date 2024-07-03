package com.asintoto.coralduels.utils;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.ArenaManager;
import org.bukkit.Location;

public class Arena {
    private Location loc1;
    private Location loc2;
    private boolean hasPlayer;
    private String name;

    public Arena(String name, Location loc1, Location loc2) {
        this.loc1 = loc1;
        this.loc2 = loc2;
        this.hasPlayer = false;
        this.name = name;

        CoralDuels.getInstance().getArenaManager().addArena(this);
    }

    public Location getLoc1() {
        return loc1;
    }

    public Location getLoc2() {
        return loc2;
    }

    public void setLoc1(Location loc1) {
        this.loc1 = loc1;
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
    }

    public boolean hasPlayer() {
        return hasPlayer;
    }

    public void setHasPlayer(boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
