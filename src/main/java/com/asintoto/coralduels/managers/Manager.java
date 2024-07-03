package com.asintoto.coralduels.managers;

import com.asintoto.colorlib.ColorLib;
import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.utils.Keys;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class Manager {

    public static String formatMessage(String msg) {
        msg = msg.replace("%prefix%", CoralDuels.getInstance().getPrefix());
        return ColorLib.setColors(msg);
    }

    public static void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(formatMessage(msg));
    }

    public static boolean isValidInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
