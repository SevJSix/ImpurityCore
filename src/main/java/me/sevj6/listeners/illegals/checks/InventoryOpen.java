package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.wrapper.ObjectWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class InventoryOpen implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        new ObjectWrapper<>(player, Player.class).check();
        new ObjectWrapper<>(inventory, Inventory.class).check();
    }
}
