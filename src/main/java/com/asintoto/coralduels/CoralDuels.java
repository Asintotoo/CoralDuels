package com.asintoto.coralduels;

import com.asintoto.colorlib.ColorLib;
import com.asintoto.coralduels.commands.DuelAcceptCommand;
import com.asintoto.coralduels.commands.DuelAdminCommand;
import com.asintoto.coralduels.commands.DuelCommand;
import com.asintoto.coralduels.hooks.PapiHook;
import com.asintoto.coralduels.listeners.GameListener;
import com.asintoto.coralduels.listeners.WandClickListener;
import com.asintoto.coralduels.managers.*;
import com.asintoto.coralduels.tabcompleters.DuelAdminTabCompleter;
import com.asintoto.coralduels.tabcompleters.DuelTabCompleter;
import com.asintoto.coralduels.utils.Debug;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.sql.SQLException;

public final class CoralDuels extends JavaPlugin {

    private YamlConfiguration messages;
    private YamlConfiguration kits;
    private YamlConfiguration menus;
    private YamlConfiguration rewards;
    private String prefix;

    private DatabaseManager dataManager;
    private ArenaManager arenaManager;
    private WandManager wandManager;
    private RequestManager requestManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        initFiles();
        this.prefix = ColorLib.setColors(getConfig().getString("general.prefix"));

        File dataFolder = new File(getDataFolder() + "/" + YamlManager.DATA_DIRECTORY);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        try {
            dataManager = new DatabaseManager(getDataFolder().getAbsolutePath() + "/" + YamlManager.DATA_DIRECTORY + "/storage.db");
            Debug.log("&aDatabase inizializzato");
        } catch (SQLException e) {
            String msg = CoralDuels.getInstance().getMessages().getString("error.database-error");
            getServer().getConsoleSender().sendMessage(Manager.formatMessage(msg));
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(CoralDuels.getInstance());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PapiHook(this).register();
            Debug.log("&aPapi Registrato");
        }

        getCommand("dueladmin").setExecutor(new DuelAdminCommand(this));
        getCommand("duel").setExecutor(new DuelCommand(this));
        getCommand("duelaccept").setExecutor(new DuelAcceptCommand(this));

        getCommand("dueladmin").setTabCompleter(new DuelAdminTabCompleter(this));
        getCommand("duel").setTabCompleter(new DuelTabCompleter(this));
        getCommand("duelaccept").setTabCompleter(new DuelTabCompleter(this));

        Debug.log("&aComandi inizializzati");

        getServer().getPluginManager().registerEvents(new WandClickListener(this), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);


        Debug.log("&aEventi registrati");

        arenaManager = new ArenaManager(this);
        wandManager = new WandManager(this);
        requestManager = new RequestManager(this);
        gameManager = new GameManager(this);

        arenaManager.init();

        String msg = messages.getString("system.on-enable");
        Manager.sendConsoleMessage(Manager.formatMessage(msg));

    }

    @Override
    public void onDisable() {

        try {
            dataManager.closeConnection();
        } catch (SQLException e) {
            String msg = getMessages().getString("error.database-error");
            Manager.sendConsoleMessage(Manager.formatMessage(msg));
            e.printStackTrace();
        }

        arenaManager.term();

        String msg = messages.getString("system.on-disable");
        Manager.sendConsoleMessage(Manager.formatMessage(msg));
    }

    private void initFiles() {
        saveDefaultConfig();
        this.messages = YamlManager.createYamlConfiguration("messages.yml");
        this.menus = YamlManager.createYamlConfiguration("menus.yml");
        this.kits = YamlManager.createYamlConfiguration("kits.yml");
        this.rewards = YamlManager.createYamlConfiguration("rewards.yml");

        Debug.log("&aFiles caricati");
    }

    public DatabaseManager getDataManager() {
        return dataManager;
    }

    public YamlConfiguration getMessages() {
        return messages;
    }

    public YamlConfiguration getKits() {
        return kits;
    }

    public YamlConfiguration getMenus() {
        return menus;
    }

    public YamlConfiguration getRewards() {
        return rewards;
    }

    public String getPrefix() {
        return prefix;
    }

    public void reload() {
        reloadConfig();
        this.messages = YamlManager.reloadYamlConfiguration("messages.yml");
        this.menus = YamlManager.reloadYamlConfiguration("menus.yml");
        this.kits = YamlManager.reloadYamlConfiguration("kits.yml");
        this.rewards = YamlManager.reloadYamlConfiguration("rewards.yml");

        this.prefix = ColorLib.setColors(getConfig().getString("general.prefix"));
        GameListener.reloadBlockedCommand();

        Debug.log("&aReload effettuato");
    }

    public static CoralDuels getInstance() {
        return getPlugin(CoralDuels.class);
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public WandManager getWandManager() {
        return wandManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
