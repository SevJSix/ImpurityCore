package me.sevj6.listeners.misc;

import me.sevj6.Instance;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author SevJ6
 */

public class NameColorJoinListener implements Listener, Instance {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String displayName = fileConfig.getNamecolor().getString(String.valueOf(player.getUniqueId()));
        if (fileConfig.getNamecolor().contains(String.valueOf(player.getUniqueId()))) {
            if (displayName.equals("null")) {
                player.setDisplayName(null);
                event.setJoinMessage(ChatColor.GRAY + player.getName() + " joined");
            } else {
                player.setDisplayName(displayName + ChatColor.RESET);
                event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', displayName) + ChatColor.GRAY + " joined");
            }
        } else {
            event.setJoinMessage(ChatColor.GRAY + player.getName() + " joined");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String displayName = fileConfig.getNamecolor().getString(String.valueOf(player.getUniqueId()));
        if (fileConfig.getNamecolor().contains(String.valueOf(player.getUniqueId()))) {
            if (displayName.equals("null")) {
                event.setQuitMessage(ChatColor.GRAY + player.getName() + " left");
            } else {
                event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', displayName + " &r&7left"));
            }
        } else {
            event.setQuitMessage(ChatColor.GRAY + player.getName() + " left");
        }
    }
}