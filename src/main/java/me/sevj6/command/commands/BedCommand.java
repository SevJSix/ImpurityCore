package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.getBedSpawnLocation() == null) {
                MessageUtil.sendMessage(player, "&3You currently do not have a bed set.");
            } else {
                MessageUtil.sendMessage(player, "&3Your bed spawn location is at &r" + Utils.formatLocation(player.getBedSpawnLocation()));
            }
        }
        return true;
    }
}
