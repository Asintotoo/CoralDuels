package com.asintoto.coralduels.utils;

import com.asintoto.colorlib.ColorLib;
import com.asintoto.coralduels.CoralDuels;
import org.bukkit.Bukkit;

public class Debug {

    public static void log(String msg) {

        if(CoralDuels.getInstance().getConfig().getBoolean("general.debug")) {
            Bukkit.getConsoleSender().sendMessage(ColorLib.setColors(msg));
        }

    }
}
