package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class PlayerPickupItem implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.PlayerPickupItem");

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        if (enabled.getValue()) {
            if (event.getItem() != null && event.getItem().getItemStack() != null) {
                Player player = event.getPlayer();
                if (CheckUtil.isIllegal(event.getItem().getItemStack())) {
                    event.setCancelled(true);
                    event.getItem().remove();
                    new IllegalWrapper<>(Player.class, player).check();
                }
            }
        }
    }
}
