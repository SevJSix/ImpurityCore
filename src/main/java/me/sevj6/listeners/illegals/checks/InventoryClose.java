package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.wrapper.IllegalWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClose implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        new IllegalWrapper<>(Player.class, player).check();
        new IllegalWrapper<>(Inventory.class, inventory).check();
    }
}
