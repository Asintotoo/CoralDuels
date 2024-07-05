package com.asintoto.coralduels.commands;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.ArenaManager;
import com.asintoto.coralduels.managers.Manager;
import com.asintoto.coralduels.managers.WandManager;
import com.asintoto.coralduels.utils.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DuelAdminCommand implements CommandExecutor {

    private final CoralDuels plugin;

    public DuelAdminCommand(CoralDuels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("coralduels.admin")) {
            String msg = plugin.getMessages().getString("error.no-permission");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("coralduels.admin.reload")) {
                String msg = plugin.getMessages().getString("error.no-permission");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            plugin.reload();
            String msg = plugin.getMessages().getString("admin.reload");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("getarenawand")) {
            if (!sender.hasPermission("coralduels.admin.wand")) {
                String msg = plugin.getMessages().getString("error.no-permission");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (!(sender instanceof Player)) {
                String msg = plugin.getMessages().getString("error.not-a-player");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            Player p = (Player) sender;

            if (p.getInventory().firstEmpty() == -1) {
                String msg = plugin.getMessages().getString("error.full-inventory");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            plugin.getWandManager().giveArenaWand(p);
            String msg = plugin.getMessages().getString("admin.wand.get");
            sender.sendMessage(Manager.formatMessage(msg));
            return true;

        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("arena")) {
            if (!sender.hasPermission("coralduels.admin.arena")) {
                String msg = plugin.getMessages().getString("error.no-permission");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (args.length < 3 && !args[1].equalsIgnoreCase("list")) {
                String msg = plugin.getMessages().getString("error.not-enough-args");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (args[1].equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    String msg = plugin.getMessages().getString("error.not-a-player");
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
                }

                Player p = (Player) sender;

                if (plugin.getWandManager().contains(p)) {
                    String name = args[2];

                    if (plugin.getArenaManager().isNameUsed(name)) {
                        String msg = plugin.getMessages().getString("error.arena-already-exist");
                        sender.sendMessage(Manager.formatMessage(msg));
                        return true;
                    }

                    Location loc1 = plugin.getWandManager().getPlayerLocationFirst().get(p);
                    Location loc2 = plugin.getWandManager().getPlayerLocationSecond().get(p);

                    if (loc1.getWorld() != loc2.getWorld()) {
                        String msg = plugin.getMessages().getString("error.different-world-location");
                        sender.sendMessage(Manager.formatMessage(msg));
                        return true;
                    }

                    new Arena(name, loc1, loc2);

                    String msg = plugin.getMessages().getString("admin.arena.created").replace("%name%", name);
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
                } else {
                    String msg = plugin.getMessages().getString("error.no-positions");
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
                }
            }

            if (args[1].equalsIgnoreCase("delete")) {

                String name = args[2];
                if (!plugin.getArenaManager().isNameUsed(name)) {
                    String msg = plugin.getMessages().getString("error.arena-not-existing");
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
                }

                plugin.getArenaManager().removeArena(name);

                String msg = plugin.getMessages().getString("admin.arena.deleted").replace("%name%", name);
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (args[1].equalsIgnoreCase("list")) {

                String avaiable = plugin.getMessages().getString("admin.arena.avaiable");
                String notAvaiable = plugin.getMessages().getString("admin.arena.not-avaiable");
                String format = plugin.getMessages().getString("admin.arena.list");

                if (plugin.getArenaManager().getArenaList().isEmpty()) {
                    String msg = plugin.getMessages().getString("error.arena-list-empty");
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
                }

                for (Arena a : plugin.getArenaManager().getArenaList()) {
                    String msg = format.replace("%name%", a.getName());
                    if (a.hasPlayer()) {
                        msg = msg.replace("%status%", notAvaiable);
                    } else {
                        msg = msg.replace("%status%", avaiable);
                    }

                    sender.sendMessage(Manager.formatMessage(msg));
                }
                return true;
            }

        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("stats")) {
            if (!sender.hasPermission("coralduels.admin.stats")) {
                String msg = plugin.getMessages().getString("error.no-permission");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (args.length < 4) {
                String msg = plugin.getMessages().getString("error.not-enough-args");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            if (args.length < 5 && !(args[2].equalsIgnoreCase("get") || args[2].equalsIgnoreCase("reset"))) {
                String msg = plugin.getMessages().getString("error.not-enough-args");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }

            Player target = Bukkit.getPlayer(args[3]);

            if (target == null) {
                String msg = plugin.getMessages().getString("error.player-offline");
                sender.sendMessage(Manager.formatMessage(msg));
                return true;
            }


            String msg = "";
            int amount = -1;

            if (!(args[2].equalsIgnoreCase("get") || args[2].equalsIgnoreCase("reset"))) {
                try {
                    amount = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    String error = plugin.getMessages().getString("error.not-a-number");
                    sender.sendMessage(Manager.formatMessage(error));
                    return true;
                }
                if (amount < 0) amount = 0;
            }

            switch (args[1].toLowerCase()) {
                case "wins":
                    if (args[2].equalsIgnoreCase("get")) {
                        amount = plugin.getDataManager().getWins(target);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("set")) {
                        plugin.getDataManager().setWins(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("add")) {
                        plugin.getDataManager().addWins(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("remove")) {
                        plugin.getDataManager().removeWins(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("reset")) {
                        plugin.getDataManager().resetWins(target);
                        break;
                    }
                    break;
                case "kills":
                    if (args[2].equalsIgnoreCase("get")) {
                        amount = plugin.getDataManager().getKills(target);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("set")) {
                        plugin.getDataManager().setKills(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("add")) {
                        plugin.getDataManager().addKills(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("remove")) {
                        plugin.getDataManager().removeKills(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("reset")) {
                        plugin.getDataManager().resetKills(target);
                        break;
                    }
                    break;
                case "deaths":
                    if (args[2].equalsIgnoreCase("get")) {
                        amount = plugin.getDataManager().getDeaths(target);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("set")) {
                        plugin.getDataManager().setDeaths(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("add")) {
                        plugin.getDataManager().addDeaths(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("remove")) {
                        plugin.getDataManager().removeDeaths(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("reset")) {
                        plugin.getDataManager().resetDeaths(target);
                        break;
                    }
                    break;
                case "games":
                    if (args[2].equalsIgnoreCase("get")) {
                        amount = plugin.getDataManager().getGames(target);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("set")) {
                        plugin.getDataManager().setGames(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("add")) {
                        plugin.getDataManager().addGames(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("remove")) {
                        plugin.getDataManager().removeGames(target, amount);
                        break;
                    }
                    if (args[2].equalsIgnoreCase("reset")) {
                        plugin.getDataManager().resetGames(target);
                        break;
                    }
                    break;
                default:
                    msg = plugin.getMessages().getString("error.unknown-arg");
                    sender.sendMessage(Manager.formatMessage(msg));
                    return true;
            }

            msg = plugin.getMessages().getString("admin.stats." + args[2].toLowerCase() + "." + args[1].toLowerCase());
            msg = msg.replace("%player%", target.getName()).replace("%amount%", Integer.toString(amount));

            sender.sendMessage(Manager.formatMessage(msg));
            return true;

        }

        String msg = plugin.getMessages().getString("admin.usage");
        sender.sendMessage(Manager.formatMessage(msg));
        return true;
    }
}
