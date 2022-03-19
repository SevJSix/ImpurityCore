package me.sevj6.listener.misc;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
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

public class JoinQuitListener implements Listener, Instance {

    private final Setting<Boolean> disableMsgs = Setting.getBoolean("disable_join_leave_messages");

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String displayName = fileConfig.getNamecolor().getString(String.valueOf(player.getUniqueId()));
        if (fileConfig.getNamecolor().contains(String.valueOf(player.getUniqueId()))) {
            if (displayName.equals("null") || displayName.contains("§k")) {
                player.setDisplayName(null);
                if (disableMsgs.getValue()) {
                    event.setJoinMessage(null);
                } else {
                    event.setJoinMessage(ChatColor.GRAY + player.getName() + " joined");
                }
            } else {
                player.setDisplayName(displayName + ChatColor.RESET);
                if (disableMsgs.getValue()) {
                    event.setJoinMessage(null);
                } else {
                    event.setJoinMessage(ChatColor.translateAlternateColorCodes('&', displayName) + ChatColor.GRAY + " joined");
                }
            }
        } else {
            if (disableMsgs.getValue()) {
                event.setJoinMessage(null);
            } else {
                event.setJoinMessage(ChatColor.GRAY + player.getName() + " joined");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String displayName = fileConfig.getNamecolor().getString(String.valueOf(player.getUniqueId()));
        if (fileConfig.getNamecolor().contains(String.valueOf(player.getUniqueId()))) {
            if (displayName.equals("null") || displayName.contains("§k")) {
                if (disableMsgs.getValue()) {
                    event.setQuitMessage(null);
                } else {
                    event.setQuitMessage(ChatColor.GRAY + player.getName() + " left");
                }
            } else {
                if (disableMsgs.getValue()) {
                    event.setQuitMessage(null);
                } else {
                    event.setQuitMessage(ChatColor.translateAlternateColorCodes('&', displayName + " &r&7left"));
                }
            }
        } else {
            if (disableMsgs.getValue()) {
                event.setQuitMessage(null);
            } else {
                event.setQuitMessage(ChatColor.GRAY + player.getName() + " left");
            }
        }
    }
}