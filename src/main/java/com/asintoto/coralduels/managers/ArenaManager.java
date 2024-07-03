package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.Arena;
import com.asintoto.coralduels.utils.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashSet;
import java.util.Set;

public class ArenaManager {

    private static Set<Arena> arenaList;

    public static void init() {
        arenaList = new HashSet<>();

        loadArenas();

        Debug.log("&aArena Manager inizializzato");
    }

    public static void term() {
        saveArenas();

        Debug.log("&aArena Manager terminato");
    }

    public static void addArena(Arena a) {
        if(!arenaList.contains(a)) {
            arenaList.add(a);
        }
    }

    public static void removeArena(Arena a) {
        if(arenaList.contains(a)) {
            arenaList.remove(a);
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName(), null);
            CoralDuels.getInstance().saveArenaFile();
        }
    }

    public static Set<Arena> getArenaList() {
        return arenaList;
    }

    public static boolean arenaAvaiable() {
        for(Arena a : arenaList) {
            if (!a.hasPlayer()) return true;
        }

        return false;
    }

    public static Arena getAvaiableArena() {
        for(Arena a : arenaList) {
            if (!a.hasPlayer()) return a;
        }

        return null;
    }

    private static void loadArenas() {
        if(!CoralDuels.getInstance().getArenas().isSet("arenas")) {
            return;
        }

        arenaList.clear();

        for (String name : CoralDuels.getInstance().getArenas().getConfigurationSection("arenas").getKeys(false)) {
            Double x = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".first-location.x");
            Double y = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".first-location.y");
            Double z = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".first-location.z");
            Double pitch = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".first-location.pitch");
            Double yaw = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".first-location.yaw");
            World w = Bukkit.getWorld(CoralDuels.getInstance().getArenas().getString("arenas." + name + ".first-location.world"));

            if(w == null) {
                continue;
            }

            Location loc1 = new Location(w, x, y, z, yaw.floatValue(), pitch.floatValue());

            x = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".second-location.x");
            y = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".second-location.y");
            z = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".second-location.z");
            pitch = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".second-location.pitch");
            yaw = CoralDuels.getInstance().getArenas().getDouble("arenas." + name + ".second-location.yaw");
            w = Bukkit.getWorld(CoralDuels.getInstance().getArenas().getString("arenas." + name + ".second-location.world"));

            if(w == null) {
                continue;
            }

            Location loc2 = new Location(w, x, y, z, yaw.floatValue(), pitch.floatValue());

            new Arena(name, loc1, loc2);

            Debug.log("&aArena " + name + " caricata");

        }
    }

    private static void saveArenas() {

        CoralDuels.getInstance().resetArenaFile();

        for(Arena a : arenaList) {
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.x", a.getLoc1().getX());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.y", a.getLoc1().getX());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.z", a.getLoc1().getX());

            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.pitch", a.getLoc1().getPitch());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.yaw", a.getLoc1().getYaw());

            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".first-location.world", a.getLoc1().getWorld());

            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.x", a.getLoc2().getX());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.y", a.getLoc2().getX());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.z", a.getLoc2().getX());

            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.pitch", a.getLoc2().getPitch());
            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.yaw", a.getLoc2().getYaw());

            CoralDuels.getInstance().getArenas().set("arenas." + a.getName() + ".second-location.world", a.getLoc2().getWorld());
        }

        CoralDuels.getInstance().saveArenaFile();
    }
}
