package me.sevj6.listener.misc;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.PluginUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author SevJ6
 */

public class CommandWhitelist implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        String[] args = event.getMessage().toLowerCase().split(" ");
        if (PluginUtil.config().getBoolean("CommandWhitelist.Enabled")) {
            if (PluginUtil.config().getStringList("CommandWhitelist.command-list").contains(args[0].replace("/", ""))) {
                if ("kill".equals(args[0].replace("/", ""))) {
                    if (PluginUtil.config().getBoolean("Plugin.use-kill-command") || !player.isOp() && !player.hasPermission("minecraft.command.kill")) {
                        player.damage(1);
                        player.setHealth(0);
                        event.setCancelled(true);
                    }
                }
            } else {
                MessageUtil.sendMessage(player, PluginUtil.config().getString("CommandWhitelist.unknown-command"));
                event.setCancelled(true);
            }
        }
    }
}