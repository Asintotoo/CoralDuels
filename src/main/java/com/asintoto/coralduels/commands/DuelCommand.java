package com.asintoto.coralduels.commands;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.PlayerStatus;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.utils.DuelRequest;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelCommand implements CommandExecutor {

    private final CoralDuels plugin;

    public DuelCommand(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,String[] args) {
        if(!sender.hasPermission("coralduels.use")) {
            String msg = plugin.getMessages().getString("error.no-permission");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

        if(!(sender instanceof Player)) {
            String msg = plugin.getMessages().getString("error.not-a-player");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

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

            if(plugin.getRequestManager().hasRequestFrom(target, (Player) sender)) {
                String msg = plugin.getMessages().getString("error.request-already-existing")
                        .replace("%player%", target.getName());
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if(plugin.getGameManager().getPlayerStatus(target) == PlayerStatus.IN_GAME ||
                    plugin.getGameManager().getPlayerStatus(target) == PlayerStatus.STARTING) {
                String msg = plugin.getMessages().getString("error.already-in-duel")
                        .replace("%player%", target.getName());
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            new DuelRequest((Player) sender, target);
            return true;
        }

        String msg = plugin.getMessages().getString("player.duel.usage");
        sender.sendMessage(Manager.formatMessage(msg));
        return true;
    }
}
