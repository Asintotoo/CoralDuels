package com.asintoto.coralduels.tabcompleters;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.List;

public class DuelTabCompleter implements TabCompleter {
    private final CoralDuels plugin;

    public DuelTabCompleter(CoralDuels plugin) {
        this.plugin = plugin;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            return null;
        }

        return Collections.emptyList();
    }
}
