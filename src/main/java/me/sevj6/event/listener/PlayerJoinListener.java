package me.sevj6.event.listener;

import me.sevj6.util.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Utils.inject(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Utils.removeHook(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Utils.removeHook(event.getPlayer());
    }

}
