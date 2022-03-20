package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.PlayerDropItem");

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (enabled.getValue()) {
            if (event.getItemDrop() != null && event.getItemDrop().getItemStack() != null) {
                Player player = event.getPlayer();
                if (CheckUtil.isIllegal(event.getItemDrop().getItemStack())) {
                    event.setCancelled(true);
                    event.getItemDrop().remove();
                    new IllegalWrapper<>(Player.class, player).check();
                }
            }
        }
    }
}
