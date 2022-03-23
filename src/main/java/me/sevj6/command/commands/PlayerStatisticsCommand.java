package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.statstics.PlayerStat;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerStatisticsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            OfflinePlayer of;
            StringBuilder builder = new StringBuilder();
            if (args.length == 0) {
                of = player;
            } else {
                of = Bukkit.getOfflinePlayer(args[0]);
            }
            try {
                PlayerStat stat = new PlayerStat(of);
                String days = (stat.getLastPlayed().equals("0")) ? "today" : stat.getLastPlayed() + " days ago";
                builder.append("&5&l--- &r").append(of.getName()).append("'s Stats").append(" &5&l---&r")
                        .append("\n")
                        .append("&7Last Played: &b").append(days)
                        .append("\n")
                        .append("&7Total Playtime: &b").append(stat.getTotalPlaytime())
                        .append("\n")
                        .append("&7First Joined: &b").append(stat.getDaysSinceFirstJoined()).append(" days ago, on ").append(PlayerUtil.getJoinDate(of))
                        .append("\n")
                        .append("&7Total Kills (No Crystals): &b").append(stat.getTotalKills())
                        .append("\n")
                        .append("&7Total Deaths: &b").append(stat.getTotalDeaths())
                        .append("\n")
                        .append("&7Totems Popped: &b").append(stat.getTotemPops())
                        .append("\n")
                        .append("&5&l---------------------------");
                MessageUtil.sendMessage(player, builder.toString());
            } catch (Throwable t) {
                MessageUtil.sendMessage(player, "&4That player has not joined before");
            }
        }
        return true;
    }
}
