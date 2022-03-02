package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.CheckUtil;
import me.sevj6.listeners.illegals.wrapper.ObjectWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop() != null && event.getItemDrop().getItemStack() != null) {
            Player player = event.getPlayer();
            if (CheckUtil.isIllegal(event.getItemDrop().getItemStack())) {
                event.setCancelled(true);
                event.getItemDrop().remove();
                new ObjectWrapper<>(player, Player.class).check();
            }
        }
    }
}
