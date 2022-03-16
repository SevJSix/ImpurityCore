package me.sevj6.listeners.illegals.check.checks;

import me.sevj6.listeners.illegals.wrapper.IllegalWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.HOPPER) {
            Inventory inventory = event.getClickedInventory();
            new IllegalWrapper<>(Inventory.class, inventory).check();
        }
    }
}
