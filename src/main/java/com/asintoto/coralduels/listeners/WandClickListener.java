package com.asintoto.coralduels.listeners;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.utils.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class WandClickListener implements Listener {
    private final CoralDuels plugin;

    public WandClickListener(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();

        if(item == null) {
            return;
        }

        if(!item.hasItemMeta()) {
            return;
        }

        ItemMeta meta = item.getItemMeta();

        if(meta.getPersistentDataContainer().has(Keys.ARENA_WAND, PersistentDataType.BOOLEAN)) {
            if(!p.hasPermission("coralduels.admin.arenawand")) {
                p.getInventory().remove(item);
            }

            if (e.getClickedBlock() != null) {
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                    plugin.getWandManager().getPlayerLocationFirst().put(p, e.getClickedBlock().getLocation().add(0, 1, 0));
                    String msg = plugin.getMessages().getString("admin.wand.second-pos");
                    p.sendMessage(Manager.formatMessage(msg));

                    e.setCancelled(true);
                } else if (e.getAction() == Action.LEFT_CLICK_BLOCK) {

                    plugin.getWandManager().getPlayerLocationSecond().put(p, e.getClickedBlock().getLocation().add(0, 1, 0));

                    String msg = plugin.getMessages().getString("admin.wand.first-pos");
                    p.sendMessage(Manager.formatMessage(msg));
                    e.setCancelled(true);
                }
            }
        }

    }
}
