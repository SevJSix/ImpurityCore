package me.sevj6.listeners.misc;

import me.sevj6.Impurity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
    }
}
