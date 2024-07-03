package com.asintoto.coralduels.managers;

import com.asintoto.colorlib.ColorLib;
import com.asintoto.coralduels.CoralDuels;
import org.bukkit.Bukkit;

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
