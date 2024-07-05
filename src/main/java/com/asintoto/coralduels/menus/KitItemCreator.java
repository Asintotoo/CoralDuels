package com.asintoto.coralduels.menus;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KitItemCreator {
    private final CoralDuels plugin;
    private String path;
    private Player holder;
    private String kitName;

    public KitItemCreator(Player holder, String path, String kitName) {
        this.plugin = CoralDuels.getInstance();
        this.path = path;
        this.holder = holder;
        this.kitName = kitName;
    }

    public ItemStack make() {
        Material material = Material.getMaterial(plugin.getMenus().getString(path + ".item"));

        if(material == null) {
            return new ItemStack(Material.STONE);
        }

        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();

        String name = plugin.getMenus().getString(path + ".display-name");
        name = Manager.formatMessage(name);

        meta.setDisplayName(name);

        List<String> lore = new ArrayList<>();

        for(String s : plugin.getMenus().getStringList(path + ".lore")) {
            lore.add(Manager.formatMessage(s));
        }

        if(!holder.hasPermission("coralduels.kit." + kitName) || !plugin.getKitManager().kitExist(kitName)) {
            lore.add(Manager.formatMessage(plugin.getMessages().getString("player.kits.not-avaiable")));
        } else {
            lore.add(Manager.formatMessage(plugin.getMessages().getString("player.kits.avaiable")));
        }

        meta.setLore(lore);

        if(plugin.getMenus().getBoolean(path + ".glow")) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);

        return item;
    }

    public int slot() {
        return plugin.getMenus().getInt(path + ".slot");
    }
}
