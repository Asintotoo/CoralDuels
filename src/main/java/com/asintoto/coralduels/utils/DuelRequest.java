package com.asintoto.coralduels.utils;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DuelRequest {
    private Player sender;
    private Player target;
    private BukkitTask task;
    private String kitName;
    private final CoralDuels plugin;

    public DuelRequest(Player sender, Player target, String kitName) {
        this.sender = sender;
        this.target = target;
        this.kitName = kitName;
        this.plugin = CoralDuels.getInstance();

        sendMessages();

        plugin.getRequestManager().addRequest(this);

        startTask();
    }

    private void sendMessages() {
        String selfMsg = plugin.getMessages().getString("player.duel.request-sent")
                .replace("%player%", target.getName());

        sender.sendMessage(Manager.formatMessage(selfMsg));

        for(String s : plugin.getMessages().getStringList("player.duel.request-message")) {
            target.sendMessage(Manager.formatMessage(s).replace("%player%", sender.getName()).replace("%kit%", kitName));
        }


        String msg = plugin.getMessages().getString("player.duel.request-message-accept")
                .replace("%sender%", sender.getName());

        msg = Manager.formatMessage(msg);
        String hoverMsg = Manager.formatMessage(plugin.getMessages()
                .getString("player.duel.request-hover-message"));

        TextComponent message = new TextComponent(msg);
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverMsg).create()));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duelaccept " + sender.getName()));

        target.spigot().sendMessage(message);

    }

    private void startTask() {

        int time = plugin.getConfig().getInt("duel.request-expire-time");
        DuelRequest req = this;

        task = new BukkitRunnable() {
            @Override
            public void run() {

                if(!plugin.getRequestManager().contains(req)) {
                    this.cancel();
                    return;
                }

                String toSender = plugin.getMessages().getString("player.duel.request-expired-sender")
                        .replace("%player%", target.getName());

                sender.sendMessage(Manager.formatMessage(toSender));

                String toTarget = plugin.getMessages().getString("player.duel.request-expired-target")
                        .replace("%player%", sender.getName());

                target.sendMessage(Manager.formatMessage(toTarget));

                CoralDuels.getInstance().getRequestManager().removeRequest(req);

            }
        }.runTaskLater(plugin, time * 20L);
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public Player getTarget() {
        return target;
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    public BukkitTask getTask() {
        return task;
    }

    public void setTask(BukkitTask task) {
        this.task = task;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }
}
