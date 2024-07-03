package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.enums.PlayerStatus;
import com.asintoto.coralduels.utils.Arena;
import com.asintoto.coralduels.utils.Debug;
import com.asintoto.coralduels.utils.DuelRequest;
import com.asintoto.coralduels.utils.Game;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class RequestManager {
    private final CoralDuels plugin;
    private Set<DuelRequest> requestsList;

    public RequestManager(CoralDuels plugin) {
        this.plugin = plugin;
        this.requestsList = new HashSet<>();
    }

    public boolean hasRequest(Player p) {
        for(DuelRequest r : requestsList) {
            if(r.getTarget() == p) {
                return true;
            }
        }

        return false;
    }

    public void addRequest(DuelRequest r) {
        if(!requestsList.contains(r)) {
            requestsList.add(r);
        }
    }

    public void removeRequest(DuelRequest r) {
        if(requestsList.contains(r)) {
            requestsList.remove(r);
        }
    }

    public boolean contains(DuelRequest r) {
        return requestsList.contains(r);
    }

    public boolean hasRequestFrom(Player p, Player sender) {
        for(DuelRequest r : requestsList) {
            if(r.getTarget() == p && r.getSender() == sender) {
                return true;
            }
        }
        return false;
    }

    public DuelRequest getDuelRequest(Player sender, Player target) {
        for(DuelRequest r : requestsList) {
            if(r.getSender() == sender && r.getTarget() == target) {
                return r;
            }
        }

        return null;
    }

    public void acceptRequest(Player sender, Player target) {
        DuelRequest r = getDuelRequest(sender, target);
        if(r == null) {
            Debug.log("&cRichiesta nulla");
            return;
        }

        r.getTask().cancel();

        requestsList.remove(r);

        String msg = plugin.getMessages().getString("player.duel.accept-sender")
                .replace("%player%", target.getName());

        sender.sendMessage(Manager.formatMessage(msg));

        msg = plugin.getMessages().getString("player.duel.accept-target")
                .replace("%player%", sender.getName());

        target.sendMessage(Manager.formatMessage(msg));

        Arena a = plugin.getArenaManager().getAvaiableArena();
        if(a == null) {
            msg = plugin.getMessages().getString("error.no-arena-avaiable");
            sender.sendMessage(Manager.formatMessage(msg));
            target.sendMessage(Manager.formatMessage(msg));
            return;
        }

        a.teleport(sender, target);
        a.setHasPlayer(true);

        new Game(a, sender, target);

        plugin.getGameManager().setPlayerStatus(sender, PlayerStatus.STARTING);
        plugin.getGameManager().setPlayerStatus(target, PlayerStatus.STARTING);

        plugin.getGameManager().gameCountdown(sender, target);

    }
}
