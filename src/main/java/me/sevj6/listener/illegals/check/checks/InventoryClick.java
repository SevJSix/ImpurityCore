package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.InventoryClick");

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (enabled.getValue()) {
            if (event.getClickedInventory() != null && event.getClickedInventory().getType() != InventoryType.HOPPER) {
                Inventory inventory = event.getClickedInventory();
                new IllegalWrapper<>(Inventory.class, inventory).check();
            }
        }
    }
}
