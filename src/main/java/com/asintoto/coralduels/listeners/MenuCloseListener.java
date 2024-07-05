package com.asintoto.coralduels.listeners;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class MenuCloseListener implements Listener {
    private final CoralDuels plugin;

    public MenuCloseListener(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        plugin.getMenuManager().removePlayer(p);
    }
}
