package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.menus.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class MenuManager {
    private final CoralDuels plugin;

    private Map<Player, Menu> playerMenuMap;

    public MenuManager(CoralDuels plugin) {
        this.plugin = plugin;
        this.playerMenuMap = new HashMap<>();
    }

    public void closeAllMenus() {
        for(Player p : playerMenuMap.keySet()) {
            p.closeInventory();
        }
    }

    public void addPlayer(Player p, Menu m) {
        if(playerMenuMap.containsKey(p)) {
            playerMenuMap.remove(p);
        }

        playerMenuMap.put(p, m);
    }

    public void removePlayer(Player p) {
        if(playerMenuMap.containsKey(p)) {
            playerMenuMap.remove(p);
        }
    }

    public Menu getPlayerMenu(Player p) {
        if(playerMenuMap.containsKey(p)) {
            return playerMenuMap.get(p);
        }

        return null;
    }

    public boolean contains(Player p) {
        return playerMenuMap.containsKey(p);
    }
}
