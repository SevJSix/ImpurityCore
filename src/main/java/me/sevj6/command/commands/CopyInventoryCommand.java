package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CopyInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.isOp()) {
                MessageUtil.sendMessage(player, "&cYou must be opped to use this command");
                return true;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target.isOnline()) {
                player.getInventory().setContents(target.getInventory().getContents());
                MessageUtil.sendMessage(player, "&3You have copied &3" + target.getName() + "'s &binventory");
            } else {
                MessageUtil.sendMessage(player, "&4That player is not online");
            }
        }
        return true;
    }
}
