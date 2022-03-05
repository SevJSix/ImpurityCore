package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.CheckUtil;
import me.sevj6.listeners.illegals.wrapper.IllegalWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;

public class PlayerPickupItem implements Listener {

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        if (event.getItem() != null && event.getItem().getItemStack() != null) {
            Player player = event.getPlayer();
            if (CheckUtil.isIllegal(event.getItem().getItemStack())) {
                event.setCancelled(true);
                event.getItem().remove();
                new IllegalWrapper<>(player).check();
            }
        }
    }
}
