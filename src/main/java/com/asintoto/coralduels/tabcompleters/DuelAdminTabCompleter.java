package com.asintoto.coralduels.tabcompleters;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class DuelAdminTabCompleter implements TabCompleter {

    private final CoralDuels plugin;

    public DuelAdminTabCompleter(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> subcommands = new ArrayList<>();

            if (sender.hasPermission("coralduels.admin.reload")) subcommands.add("reload");
            if (sender.hasPermission("coralduels.admin.arena")) subcommands.add("arena");
            if (sender.hasPermission("coralduels.admin.wand")) subcommands.add("getarenawand");
            if (sender.hasPermission("coralduels.admin.stats")) subcommands.add("stats");

            return subcommands;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("arena")) {
            List<String> subcommands = new ArrayList<>();

            subcommands.add("create");
            subcommands.add("delete");
            subcommands.add("list");

            return subcommands;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            List<String> subcommands = new ArrayList<>();

            subcommands.add("wins");
            subcommands.add("deaths");
            subcommands.add("kills");
            subcommands.add("games");

            return subcommands;
        }

        if (args.length == 3 && (args[1].equalsIgnoreCase("create") || args[1].equalsIgnoreCase("delete"))) {
            List<String> subcommands = new ArrayList<>();
            for (Arena a : plugin.getArenaManager().getArenaList()) {
                subcommands.add(a.getName());
            }

            return subcommands;
        }

        if (args.length == 3 &&
                (args[1].equalsIgnoreCase("wins") ||
                        args[1].equalsIgnoreCase("deaths") ||
                        args[1].equalsIgnoreCase("kills") ||
                        args[1].equalsIgnoreCase("games"))) {


            List<String> subcommands = new ArrayList<>();

            subcommands.add("get");
            subcommands.add("set");
            subcommands.add("add");
            subcommands.add("remove");
            subcommands.add("reset");

            return subcommands;

        }

        if (args.length == 4 &&
                (args[1].equalsIgnoreCase("wins") ||
                        args[1].equalsIgnoreCase("deaths") ||
                        args[1].equalsIgnoreCase("kills") ||
                        args[1].equalsIgnoreCase("games"))) {


            return null;
        }

            return Collections.emptyList();
    }
}
