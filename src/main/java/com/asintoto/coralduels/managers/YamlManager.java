package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class YamlManager {

    public static final String DATA_DIRECTORY = "data/";
    public static final String SAVES_DIRECTORY = "saves/";

    public static YamlConfiguration createYamlConfiguration(String filename) {
        File configFile = new File(CoralDuels.getInstance().getDataFolder(), filename);
        if(!configFile.exists()) {
            CoralDuels.getInstance().saveResource(filename, false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static YamlConfiguration reloadYamlConfiguration(String filename) {
        File configFile = new File(CoralDuels.getInstance().getDataFolder(), filename);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
