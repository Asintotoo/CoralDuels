package com.asintoto.coralduels.managers;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.DuelRequest;
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
}
