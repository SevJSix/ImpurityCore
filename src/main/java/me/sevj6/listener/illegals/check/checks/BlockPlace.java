package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.BlockPlace");

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (enabled.getValue()) {
            if (CheckUtil.unobtainable.contains(event.getBlockPlaced().getType())) {
                event.setCancelled(true);
                new IllegalWrapper<>(Player.class, event.getPlayer()).check();
            }
            if (event.getBlockPlaced().getState() instanceof Container) {
                Container container = (Container) event.getBlockPlaced().getState();
                new IllegalWrapper<>(Container.class, container).check();
            }
        }
    }
}
