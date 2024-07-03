package com.asintoto.coralduels.utils;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.ArenaManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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

        fixLocation1();
        fixLocation2();
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

    public void teleport(Player p1, Player p2) {
        p1.teleport(loc1);
        p2.teleport(loc2);
    }

    private void fixLocation1() {
        double dx = loc1.getX() - loc2.getX();
        double dy = loc1.getY() - loc2.getY();
        double dz = loc1.getZ() - loc2.getZ();


        double yaw = Math.toDegrees(Math.atan2(-dx, dz));
        yaw = yaw < 0 ? yaw + 360 : yaw;


        double distanceXZ = Math.sqrt(dx * dx + dz * dz);


        double pitch = Math.toDegrees(Math.atan2(-dy, distanceXZ));


        loc2.setYaw((float) yaw);
        loc2.setPitch((float) pitch);
    }

    private void fixLocation2() {
        double dx = loc2.getX() - loc1.getX();
        double dy = loc2.getY() - loc1.getY();
        double dz = loc2.getZ() - loc1.getZ();


        double yaw = Math.toDegrees(Math.atan2(-dx, dz));
        yaw = yaw < 0 ? yaw + 360 : yaw;


        double distanceXZ = Math.sqrt(dx * dx + dz * dz);


        double pitch = Math.toDegrees(Math.atan2(-dy, distanceXZ));


        loc1.setYaw((float) yaw);
        loc1.setPitch((float) pitch);
    }
}
