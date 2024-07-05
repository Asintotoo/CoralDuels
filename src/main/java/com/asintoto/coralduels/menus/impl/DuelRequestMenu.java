package com.asintoto.coralduels.menus.impl;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.menus.Button;
import com.asintoto.coralduels.menus.KitItemCreator;
import com.asintoto.coralduels.menus.Menu;
import com.asintoto.coralduels.utils.DuelRequest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DuelRequestMenu extends Menu {

    private Player target;
    private Map<Integer, String> buttonSlotMap;


    public DuelRequestMenu(Player holder, Player target) {
        super(holder);

        this.target = target;
        this.buttonSlotMap = new HashMap<>();

        String title = plugin.getMenus().getString("menus.duel-request.title").replace("%player%", target.getName());
        int size = plugin.getMenus().getInt("menus.duel-request.size");

        createMenu(size, title);

        for(String kit : plugin.getMenus().getConfigurationSection("menus.duel-request.kits").getKeys(false)) {
            KitItemCreator kitItemCreator = new KitItemCreator(holder, "menus.duel-request.kits." + kit, kit);

            Button b = new Button(kitItemCreator.make());
            addButton(b, kitItemCreator.slot());

            buttonSlotMap.put(kitItemCreator.slot(), kit);
        }

        Button close = new Button(createCloseButton("duel-request"));
        addButton(close, plugin.getMenus().getInt("menus.duel-request.close-button.slot"));

    }

    @Override
    public void onClick(Player p, int slot) {
        if(slot == plugin.getMenus().getInt("menus.duel-request.close-button.slot")) {
            p.closeInventory();
            return;
        }

        if(!buttonSlotMap.containsKey(slot)) {
            return;
        }

        String kitName = buttonSlotMap.get(slot);

        if(!p.hasPermission("coralduels.kit." + kitName) || !plugin.getKitManager().kitExist(kitName)) {
            String msg = plugin.getMessages().getString("error.kit-locked");
            p.sendMessage(Manager.formatMessage(msg));
            p.closeInventory();
            return;
        }

        new DuelRequest(p, target, kitName);
        p.closeInventory();
    }
}
