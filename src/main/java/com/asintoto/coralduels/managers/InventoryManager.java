package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryManager {
    private final CoralDuels plugin;

    private Set<String> leftPlayers = new HashSet<>();

    private Map<String, ItemStack[]> leftInventory = new HashMap<>();
    private Map<String, ItemStack[]> leftArmor = new HashMap<>();
    private Map<String, Location> leftLocation = new HashMap<>();
    private Map<String, GameMode> leftGamemode = new HashMap<>();

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

        leftPlayers.add(p.getUniqueId().toString());

        if(savedInventories.containsKey(p)) {
            leftInventory.put(p.getUniqueId().toString(), savedInventories.get(p));
            savedInventories.remove(p);
        }
        if(savedArmorContents.containsKey(p)) {
            leftArmor.put(p.getUniqueId().toString(), savedArmorContents.get(p));
            savedArmorContents.remove(p);
        }

        if(plugin.getGameManager().getPlayerLocationMap().containsKey(p)) {
            leftLocation.put(p.getUniqueId().toString(), plugin.getGameManager().getPlayerLocationMap().get(p));
            plugin.getGameManager().getPlayerLocationMap().remove(p);
        }

        if(plugin.getGameManager().getGameModeMap().containsKey(p)) {
            leftGamemode.put(p.getUniqueId().toString(), plugin.getGameManager().getGameModeMap().get(p));
            plugin.getGameManager().getGameModeMap().remove(p);
        }
    }

    public void restorePlayerLeft(Player p) {
        String uuid = p.getUniqueId().toString();

        if(leftInventory.containsKey(uuid)) {
            p.getInventory().setContents(leftInventory.get(uuid));
            leftInventory.remove(uuid);
        }

        if(leftArmor.containsKey(uuid)) {
            p.getInventory().setArmorContents(leftArmor.get(uuid));
            leftArmor.remove(uuid);
        }

        if(leftLocation.containsKey(uuid)) {
            p.teleport(leftLocation.get(uuid));
            leftLocation.remove(uuid);
        }

        if(leftGamemode.containsKey(uuid)) {
            p.setGameMode(leftGamemode.get(uuid));
            leftGamemode.remove(uuid);
        } else {
            p.setGameMode(GameMode.SURVIVAL);
        }

        leftPlayers.remove(p);
    }

    public Set<String> getLeftPlayers() {
        return leftPlayers;
    }
}
