package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class InventoryOpen implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.InventoryOpen");

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (enabled.getValue()) {
            Player player = (Player) event.getPlayer();
            Inventory inventory = event.getInventory();
            new IllegalWrapper<>(Player.class, player).check();
            new IllegalWrapper<>(Inventory.class, inventory).check();
        }
    }
}
