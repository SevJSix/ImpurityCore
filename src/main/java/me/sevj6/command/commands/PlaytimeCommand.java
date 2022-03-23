package me.sevj6.command.commands;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlaytimeCommand implements CommandExecutor, Instance {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 0) {
                OfflinePlayer self = Bukkit.getOfflinePlayer(((Player) sender).getUniqueId());
                if (playtimes.contains(self.getUniqueId().toString())) {
                    String selfPlaytime = Utils.getFormattedPlaytime(self.getUniqueId());
                    MessageUtil.sendMessage(sender, "&3Your playtime is: &b" + selfPlaytime + "&r\n&3Your playtime will be updated the next time you leave.\n&r&bThis playtime may be different from the playtime you see using /pstats");
                } else {
                    MessageUtil.sendMessage(sender, "&4Your playtime has not yet been calculated. Re-join to see your playtime.");
                }
            } else {
                OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
                if (!player.hasPlayedBefore())
                    MessageUtil.sendMessage(sender, "&4That player has never joined the server before.");
                if (!playtimes.contains(String.valueOf(player.getUniqueId()))) {
                    MessageUtil.sendMessage(sender, "&4That player's playtime has not yet been calculated.");
                } else {
                    String playtime = Utils.getFormattedPlaytime(player.getUniqueId());
                    MessageUtil.sendMessage(sender, "&3" + args[0] + "'s playtime: &b" + playtime + "&r\n&3Their playtime will be updated the next time they leave.\n&r&bThis playtime may be different from the playtime you see using /pstats");
                }
            }
        }
        return true;
    }
}
