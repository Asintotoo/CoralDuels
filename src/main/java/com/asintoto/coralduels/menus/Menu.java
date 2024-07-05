package com.asintoto.coralduels.menus;

import com.asintoto.coralduels.CoralDuels;
import com.asintoto.coralduels.managers.Manager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public abstract class Menu {
    private Inventory inventory;

    private int size;
    private String title;
    private Menu prevMenu;
    private Map<Integer, Button> buttonSlotMap;

    private Player holder;

    protected final CoralDuels plugin;

    public Menu(Player holder) {

        this.plugin = CoralDuels.getInstance();

        this.buttonSlotMap = new HashMap<>();

        this.prevMenu = null;

        this.holder = holder;

    }

    protected void createMenu(int size, String title) {
        this.title = title;
        this.size = size;
        if(size <= 0 || size > 54 || size % 9 != 0) size = 54;
        this.inventory = Bukkit.createInventory(null, this.size, Manager.formatMessage(this.title));
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Menu getPrevMenu() {
        return prevMenu;
    }

    public void setPrevMenu(Menu prevMenu) {
        this.prevMenu = prevMenu;
    }

    public void open() {
        holder.openInventory(inventory);
        plugin.getMenuManager().addPlayer(holder, this);
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addButton(Button button, int slot) {
        this.inventory.setItem(slot, button.getItem());
        this.buttonSlotMap.put(slot, button);
    }

    public Button getButtonAtSlot(int slot) {
        if(buttonSlotMap.containsKey(slot)) {
            return buttonSlotMap.get(slot);
        }

        return null;
    }

    public void onClick(Player p, int slot) {

    }

    public Player getHolder() {
        return holder;
    }
}
