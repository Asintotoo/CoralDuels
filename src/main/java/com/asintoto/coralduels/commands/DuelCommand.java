package com.asintoto.coralduels.commands;

import com.asintoto.coralduels.CoralDuels;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DuelCommand implements CommandExecutor {

    private final CoralDuels plugin;

    public DuelCommand(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
        return true;
    }
}
