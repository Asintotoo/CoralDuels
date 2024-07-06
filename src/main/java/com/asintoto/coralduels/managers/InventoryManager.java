package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    private final CoralDuels plugin;

    private Map<Player, ItemStack[]> savedInventories = new HashMap<>();
    private Map<Player, ItemStack[]> savedArmorContents = new HashMap<>();

    public InventoryManager(CoralDuels plugin) {
        this.plugin = plugin;
    }

    public void savePlayerInventory(Player p) {
        savedInventories.put(p, p.getInventory().getContents());
        savedArmorContents.put(p, p.getInventory().getArmorContents());
    }

    public void restorePlayerInventory(Player p) {
        if (savedInventories.containsKey(p) && savedArmorContents.containsKey(p)) {
            p.getInventory().setContents(savedInventories.get(p));
            p.getInventory().setArmorContents(savedArmorContents.get(p));
            savedInventories.remove(p);
            savedArmorContents.remove(p);
        }
    }

    public void clearPlayerInventory(Player p) {
        p.getInventory().clear();
        p.getInventory().setHelmet(new ItemStack(Material.AIR));
        p.getInventory().setChestplate(new ItemStack(Material.AIR));
        p.getInventory().setLeggings(new ItemStack(Material.AIR));
        p.getInventory().setBoots(new ItemStack(Material.AIR));
    }

    public void addPlayerLeft(Player p) {

        String uuid = p.getUniqueId().toString();

        ItemStack[] content = null;
        Location loc = null;
        GameMode gm = null;
        int lvl = 0;
        float points = 0;

        if(savedInventories.containsKey(p)) {
            content = savedInventories.get(p);
            savedInventories.remove(p);
        }

        if(plugin.getGameManager().getPlayerLocationMap().containsKey(p)) {
            loc = plugin.getGameManager().getPlayerLocationMap().get(p);
            plugin.getGameManager().getPlayerLocationMap().remove(p);
        }

        if(plugin.getGameManager().getGameModeMap().containsKey(p)) {
            gm = plugin.getGameManager().getGameModeMap().get(p);
            plugin.getGameManager().getGameModeMap().remove(p);
        }

        if(plugin.getGameManager().getExperienceLevelMap().containsKey(p)) {
            lvl = plugin.getGameManager().getExperienceLevelMap().get(p);
            plugin.getGameManager().getExperienceLevelMap().remove(p);
        }

        if(plugin.getGameManager().getExperienceMap().containsKey(p)) {
            points = plugin.getGameManager().getExperienceMap().get(p);
            plugin.getGameManager().getExperienceMap().remove(p);
        }

        plugin.getOfflinePlayerRestorer().savePlayer(p, loc, gm, content, lvl, points);
    }
}
