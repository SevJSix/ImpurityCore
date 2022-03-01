package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinDate extends Command {

    public JoinDate(Impurity plugin) {
        super("jd", "&4Usage: &c/jd <player>", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            sendMessage(player, "&3You joined this map on &b" + PlayerUtil.getJoinDate(player));
        } else {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
            if (offlinePlayer.hasPlayedBefore()) {
                sendMessage(player, "&3" + offlinePlayer.getName() + "&3 joined this map on &b" + PlayerUtil.getJoinDate(offlinePlayer));
            } else {
                sendMessage(player, "&3That player has not joined the server before");
            }
        }
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
