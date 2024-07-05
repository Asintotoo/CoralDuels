package com.asintoto.coralduels.listeners;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.menus.Menu;
import com.asintoto.coralduels.utils.Keys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class MenuClickListener implements Listener {

    private final CoralDuels plugin;

    public MenuClickListener(CoralDuels plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        if(item == null) return;

        if(!plugin.getMenuManager().contains(p)) return;

        PersistentDataContainer itemData = item.getItemMeta().getPersistentDataContainer();


        boolean isUnocked = itemData.has(Keys.BUTTON_TYPE_UNLOCKED, PersistentDataType.BOOLEAN);

        if(!isUnocked) {
            e.setCancelled(true);
        }

        if(itemData.has(Keys.BUTTON_TYPE_CLOSE, PersistentDataType.BOOLEAN)) {
            p.closeInventory();
            return;
        }

        if(itemData.has(Keys.BUTTON_TYPE_PREV, PersistentDataType.BOOLEAN)) {

            Menu prev = plugin.getMenuManager().getPlayerMenu(p).getPrevMenu();

            if(prev != null) prev.open();

            return;
        }

        int slot = e.getRawSlot();

        Menu menu = plugin.getMenuManager().getPlayerMenu(p);

        if(slot >= 0 && slot <= menu.getSize() - 1) {
            menu.onClick(p, slot);
        }
    }
}
