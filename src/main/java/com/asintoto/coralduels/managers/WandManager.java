package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.Keys;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WandManager {
    private Map<Player, Location> playerLocationFirst;
    private Map<Player, Location> playerLocationSecond;
    private final CoralDuels plugin;

    public WandManager(CoralDuels plugin) {
        this.plugin = plugin;
        playerLocationFirst = new HashMap<>();
        playerLocationSecond = new HashMap<>();
    }

    public Map<Player, Location> getPlayerLocationFirst() {
        return playerLocationFirst;
    }

    public Map<Player, Location> getPlayerLocationSecond() {
        return playerLocationSecond;
    }

    public boolean contains(Player p) {
        return playerLocationFirst.containsKey(p) && playerLocationSecond.containsKey(p);
    }

    public void remove(Player p) {
        playerLocationFirst.remove(p);
        playerLocationSecond.remove(p);
    }

    public void giveArenaWand(Player p) {
        Material mat = Material.getMaterial(CoralDuels.getInstance().getConfig().getString("arena-wand.item"));
        String name = Manager.formatMessage(CoralDuels.getInstance().getConfig().getString("arena-wand.name"));

        List<String> lore = CoralDuels.getInstance().getConfig().getStringList("arena-wand.lore");
        List<String> loreFormatted = new ArrayList<>();

        for(String s : lore) {
            loreFormatted.add(Manager.formatMessage(s));
        }

        boolean glow = CoralDuels.getInstance().getConfig().getBoolean("arena-wand.glow");

        ItemStack wand = new ItemStack(mat);
        ItemMeta meta = wand.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(loreFormatted);

        meta.setUnbreakable(true);

        meta.getPersistentDataContainer().set(Keys.ARENA_WAND, PersistentDataType.BOOLEAN, true);

        if(glow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        wand.setItemMeta(meta);

        p.getInventory().addItem(wand);
    }
}
