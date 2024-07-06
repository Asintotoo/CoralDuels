package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class KitManager {
    private final CoralDuels plugin;
    private YamlConfiguration kits;
    private File kitsFile;

    public KitManager(CoralDuels plugin) {
        this.plugin = plugin;

        kitsFile = new File(plugin.getDataFolder(), YamlManager.SAVES_DIRECTORY + "kits.yml");
        if(!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        kits = YamlConfiguration.loadConfiguration(kitsFile);
    }

    public void deleteKit(String kitName) {
        if(!kits.contains(kitName)) {
            return;
        }

        kits.set(kitName, null);

        try {
            kits.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getKitList() {
        List<String> list = new ArrayList<>();

        Set<String> set = kits.getConfigurationSection("").getKeys(false);

        if(set.isEmpty()) {
            list.add("");
            return list;
        }

        list.addAll(set);

        return list;
    }

    public boolean kitExist(String kitName) {
        kits = YamlConfiguration.loadConfiguration(kitsFile);

        return kits.contains(kitName);
    }

    public void saveKit(Player p, String kitName) {

        kits = YamlConfiguration.loadConfiguration(kitsFile);

        Map<String, Object> kitMap = new HashMap<>();

        ItemStack[] contents = p.getInventory().getContents();

        kitMap.put("content", contents);


        kits.set(kitName, kitMap);

        try {
            kits.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadKit(Player p, String kitName) {

        kits = YamlConfiguration.loadConfiguration(kitsFile);

        if(!kitExist(kitName)) {
            return;
        }


        ItemStack[] content = ((List<ItemStack>) kits.getList(kitName + ".content")).toArray(new ItemStack[0]);

        p.getInventory().setContents(content);
    }
}
