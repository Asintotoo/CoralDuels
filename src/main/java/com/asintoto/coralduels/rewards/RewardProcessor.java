package com.asintoto.coralduels.rewards;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RewardProcessor {
    private final CoralDuels plugin;

    public RewardProcessor(CoralDuels plugin) {
        this.plugin = plugin;
    }

    public List<String> getRewardsNameList() {
        List<String> list = new ArrayList<>();

        for(String s : plugin.getRewards().getConfigurationSection("rewards").getKeys(false)) {
            list.add(s);
        }

        return list;
    }

    public boolean isRewardsEmpty() {
        List<String> list = new ArrayList<>();

        for(String s : plugin.getRewards().getConfigurationSection("rewards").getKeys(false)) {
            list.add(s);
        }

        return list.isEmpty();
    }

    public void reward(Player p) {
        for(String rewardName : getRewardsNameList()) {
            if(canReceiveReward(p, rewardName)) {
                giveReward(p, rewardName);
            }
        }

    }

    private boolean canReceiveReward(Player p, String rewardName) {

        int wins = plugin.getDataManager().getWins(p);

        if (!plugin.getRewards().contains("rewards." + rewardName)) {
            return false;
        }

        int requiredWins = plugin.getRewards().getInt("rewards." + rewardName + ".wins");
        boolean repeat = plugin.getRewards().getBoolean("rewards." + rewardName + ".repeat");

        if (repeat) {
            return wins >= requiredWins && wins % requiredWins == 0;
        } else {
            return wins == requiredWins;
        }
    }

    private void giveReward(Player p, String rewardName) {
        if (!plugin.getRewards().contains("rewards." + rewardName)) {
            return;
        }

        List<String> rewards = plugin.getRewards().getStringList("rewards." + rewardName + ".rewards");
        for (String reward : rewards) {
            if (reward.startsWith("[command]")) {
                String command = reward.replace("[command] ", "").replace("%player%", p.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            } else if (reward.startsWith("[player]")) {
                String command = reward.replace("[player] ", "").replace("%player%", p.getName());
                p.performCommand(command);
            } else if (reward.startsWith("[money]")) {
                if (plugin.getEconomy() != null) {
                    double amount = Double.parseDouble(reward.replace("[money] ", ""));
                    plugin.getEconomy().depositPlayer(p, amount);
                }
            } else if (reward.startsWith("[item]")) {
                String[] itemInfo = reward.replace("[item] ", "").split(":");
                Material material = Material.getMaterial(itemInfo[0]);
                int amount = Integer.parseInt(itemInfo[1]);

                if (material != null) {
                    ItemStack itemStack = new ItemStack(material, amount);
                    p.getInventory().addItem(itemStack);
                }
            } else if (reward.startsWith("[message]")) {
                String msg = reward.replace("[message] ", "").replace("%player%", p.getName());
                msg = PlaceholderAPI.setPlaceholders(p, msg);
                p.sendMessage(Manager.formatMessage(msg));
            }
        }
    }
}
