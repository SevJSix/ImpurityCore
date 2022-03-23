package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                MessageUtil.sendMessage(player, "&3Your ping is &b" + PlayerUtil.getPlayerPing(player));
            } else {
                Player target = Bukkit.getPlayer(args[0]);
                if (!target.isOnline()) {
                    MessageUtil.sendMessage(player, "&4That player is not online.");
                } else {
                    MessageUtil.sendMessage(player, "&3" + target.getName() + "'s &3ping is &b" + PlayerUtil.getPlayerPing(target));
                }
            }
        }
        return true;
    }
}
