package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.wrapper.ObjectWrapper;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getState() instanceof Container) {
            Container container = (Container) event.getBlockPlaced().getState();
            new ObjectWrapper<>(container, Container.class).check();
        }
    }
}
