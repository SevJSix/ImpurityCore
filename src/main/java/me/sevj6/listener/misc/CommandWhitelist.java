package me.sevj6.listener.misc;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

/**
 * @author SevJ6
 */

public class CommandWhitelist implements Listener, Instance {

    private final Setting<Boolean> enabled = Setting.getBoolean("command_whitelist.enabled");
    private final Setting<Boolean> killCommand = Setting.getBoolean("enable_kill_command");
    private final Setting<String> unknown_command = Setting.getString("commands.unknown-command");
    private final Setting<List<String>> commands = Setting.getStringList("commands.command_list");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) return;
        String[] args = event.getMessage().toLowerCase().split(" ");
        if (enabled.getValue()) {
            if (commands.getValue().contains(args[0].replace("/", ""))) {
                if ("kill".equals(args[0].replace("/", ""))) {
                    if (killCommand.getValue() || !player.isOp() && !player.hasPermission("minecraft.command.kill")) {
                        player.damage(1);
                        player.setHealth(0);
                        event.setCancelled(true);
                    }
                }
            } else {
                MessageUtil.sendMessage(player, unknown_command.getValue());
                event.setCancelled(true);
            }
        }
    }
}