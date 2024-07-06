package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfflinePlayerRestorer {
    private CoralDuels plugin;

    private YamlConfiguration offlinePlayers;
    private File offlinePlayersFile;

    public OfflinePlayerRestorer(CoralDuels plugin) {
        this.plugin = plugin;

        offlinePlayersFile = new File(plugin.getDataFolder(), YamlManager.SAVES_DIRECTORY + "offlineplayers.yml");

        if(!offlinePlayersFile.exists()) {
            try {
                offlinePlayersFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        offlinePlayers = YamlConfiguration.loadConfiguration(offlinePlayersFile);
    }

    public void savePlayer(Player p, Location loc, GameMode gm, ItemStack[] inv) {

        offlinePlayers = YamlConfiguration.loadConfiguration(offlinePlayersFile);

        String uuid = p.getUniqueId().toString();

        offlinePlayers.set(uuid + ".inventory", inv);
        offlinePlayers.set(uuid + ".location", loc);
        offlinePlayers.set(uuid + ".gamemode", gm.toString());

        try {
            offlinePlayers.save(offlinePlayersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadPlayer(Player p) {

        offlinePlayers = YamlConfiguration.loadConfiguration(offlinePlayersFile);

        if(!playerExists(p)) {
            return;
        }

        String uuid = p.getUniqueId().toString();

        ItemStack[] content = ((List<ItemStack>) offlinePlayers.getList(uuid + ".inventory")).toArray(new ItemStack[0]);

        Location loc = (Location) offlinePlayers.get(uuid + ".location");
        GameMode gm = GameMode.valueOf(offlinePlayers.getString(uuid + ".gamemode"));

        if(content != null) p.getInventory().setContents(content);

        if(loc != null) p.teleport(loc);
        if(gm != null) p.setGameMode(gm);

        offlinePlayers.set(uuid, null);

        try {
            offlinePlayers.save(offlinePlayersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean playerExists(Player p) {

        offlinePlayers = YamlConfiguration.loadConfiguration(offlinePlayersFile);

        return offlinePlayers.contains(p.getUniqueId().toString());
    }
}
