package me.sevj6.listeners.patches;

import me.sevj6.Instance;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author SevJ6
 */

public class TeleportCoordLog implements Listener, Instance {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (config.getBoolean("Exploits.tp-coord-log")) {
            try {
                PlayerTeleportEvent.TeleportCause cause = event.getCause();
                double distance = event.getFrom().distance(event.getTo());
                Player player = event.getPlayer();
                if (cause == PlayerTeleportEvent.TeleportCause.PLUGIN || cause == PlayerTeleportEvent.TeleportCause.COMMAND || cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL && distance > 250) {
                    vanish(player);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> unVanish(player), 20 * 5L);
                }
            } catch (Throwable ignored) {
            }
        }
    }

    private void vanish(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(player)) {
                if (!player.isOnline()) return;
                online.hidePlayer(plugin, player);
            }
        }
    }

    private void unVanish(Player player) {
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online.equals(player)) {
                if (!player.isOnline()) return;
                online.showPlayer(plugin, player);
            }
        }
    }
}
