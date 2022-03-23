package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoindateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                MessageUtil.sendMessage(player, "&3You joined this map on &b" + PlayerUtil.getJoinDate(player));
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer.hasPlayedBefore()) {
                    MessageUtil.sendMessage(player, "&3" + offlinePlayer.getName() + "&3 joined this map on &b" + PlayerUtil.getJoinDate(offlinePlayer));
                } else {
                    MessageUtil.sendMessage(player, "&3That player has not joined the server before");
                }
            }
        }
        return true;
    }
}
