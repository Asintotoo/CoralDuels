package com.asintoto.coralduels.commands;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.utils.DuelRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelAcceptCommand implements CommandExecutor {
    private final CoralDuels plugin;

    public DuelAcceptCommand(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("coralduels.accept")) {
            String msg = plugin.getMessages().getString("error.no-permission");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

        if(!(sender instanceof Player)) {
            String msg = plugin.getMessages().getString("error.not-a-player");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

        Player p = (Player) sender;

        if(args.length >= 1) {
            Player target = Bukkit.getPlayer(args[0]);

            if(target == null) {
                String msg = plugin.getMessages().getString("error.player-offline");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if(target.getName().equalsIgnoreCase(sender.getName())) {
                String msg = plugin.getMessages().getString("error.self-duel");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if(!plugin.getRequestManager().hasRequestFrom(p, target)) {
                String msg = plugin.getMessages().getString("error.no-request-from-player")
                        .replace("%player%", target.getName());
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            plugin.getRequestManager().acceptRequest(target, p);

            return true;
        }

        String msg = plugin.getMessages().getString("player.duel.accept-usage");
        sender.sendMessage(Manager.formatMessage(msg));
        return true;
    }
}
