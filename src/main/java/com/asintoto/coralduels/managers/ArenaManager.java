package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.Arena;
import com.asintoto.coralduels.utils.Debug;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ArenaManager {

    private Set<Arena> arenaList;
    private YamlConfiguration arena;
    private File arenaFile;
    private final CoralDuels plugin;



    public ArenaManager(CoralDuels plugin) {

        this.plugin = plugin;

        arenaList = new HashSet<>();
        arenaFile = new File(CoralDuels.getInstance().getDataFolder(), "arenas.yml");
        if(!arenaFile.exists()) {
            try {
                arenaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        arena = YamlConfiguration.loadConfiguration(arenaFile);

    }

    public void init() {
        loadArenas();

        Debug.log("&aArena Manager inizializzato");
    }

    public void term() {
        saveArenas();

        Debug.log("&aArena Manager terminato");
    }

    public void addArena(Arena a) {
        if(!arenaList.contains(a)) {
            arenaList.add(a);
        }
    }

    public void removeArena(Arena a) {
        if(arenaList.contains(a)) {
            arenaList.remove(a);
        }
    }

    public void removeArena(String name) {
        for(Arena a : arenaList) {
            if(a.getName().equalsIgnoreCase(name)) {
                removeArena(a);
                return;
            }
        }
    }

    public Set<Arena> getArenaList() {
        return arenaList;
    }

    public boolean containsArena(Arena a) {
        return arenaList.contains(a);
    }

    public boolean isNameUsed(String name) {
        for(Arena a : arenaList) {
            if(a.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    public boolean arenaAvaiable() {
        for(Arena a : arenaList) {
            if (!a.hasPlayer()) return true;
        }

        return false;
    }

    public Arena getAvaiableArena() {
        for(Arena a : arenaList) {
            if (!a.hasPlayer()) return a;
        }

        return null;
    }

    private void loadArenas() {
        if(!arena.isSet("arenas")) {
            return;
        }

        arenaList.clear();

        for (String name : arena.getConfigurationSection("arenas").getKeys(false)) {
            Double x = arena.getDouble("arenas." + name + ".first-location.x");
            Double y = arena.getDouble("arenas." + name + ".first-location.y");
            Double z = arena.getDouble("arenas." + name + ".first-location.z");
            Double pitch = arena.getDouble("arenas." + name + ".first-location.pitch");
            Double yaw = arena.getDouble("arenas." + name + ".first-location.yaw");
            World w = Bukkit.getWorld(arena.getString("arenas." + name + ".first-location.world"));

            if(w == null) {
                continue;
            }

            Location loc1 = new Location(w, x, y, z, yaw.floatValue(), pitch.floatValue());

            x = arena.getDouble("arenas." + name + ".second-location.x");
            y = arena.getDouble("arenas." + name + ".second-location.y");
            z = arena.getDouble("arenas." + name + ".second-location.z");
            pitch = arena.getDouble("arenas." + name + ".second-location.pitch");
            yaw = arena.getDouble("arenas." + name + ".second-location.yaw");
            w = Bukkit.getWorld(arena.getString("arenas." + name + ".second-location.world"));

            if(w == null) {
                continue;
            }

            Location loc2 = new Location(w, x, y, z, yaw.floatValue(), pitch.floatValue());

            new Arena(name, loc1, loc2);

            Debug.log("&aArena " + name + " caricata");

        }
    }

    private void saveArenas() {

        if(arenaFile.exists()) {
            arenaFile.delete();
        }

        try {
            arenaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(arenaList.isEmpty()) return;

        for(Arena a : arenaList) {
            arena.set("arenas." + a.getName() + ".first-location.x", a.getLoc1().getX());
            arena.set("arenas." + a.getName() + ".first-location.y", a.getLoc1().getX());
            arena.set("arenas." + a.getName() + ".first-location.z", a.getLoc1().getX());

            arena.set("arenas." + a.getName() + ".first-location.pitch", a.getLoc1().getPitch());
            arena.set("arenas." + a.getName() + ".first-location.yaw", a.getLoc1().getYaw());

            arena.set("arenas." + a.getName() + ".first-location.world", a.getLoc1().getWorld().getName());

            arena.set("arenas." + a.getName() + ".second-location.x", a.getLoc2().getX());
            arena.set("arenas." + a.getName() + ".second-location.y", a.getLoc2().getX());
            arena.set("arenas." + a.getName() + ".second-location.z", a.getLoc2().getX());

            arena.set("arenas." + a.getName() + ".second-location.pitch", a.getLoc2().getPitch());
            arena.set("arenas." + a.getName() + ".second-location.yaw", a.getLoc2().getYaw());

            arena.set("arenas." + a.getName() + ".second-location.world", a.getLoc2().getWorld().getName());
        }

        try {
            arena.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
