package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Ping extends Command {
    public Ping(Impurity plugin) {
        super("ping", "&4Usage: &c/ping <player>", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) {
            sendMessage(player, "&3Your ping is &b" + PlayerUtil.getPlayerPing(player));
            return;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (!target.isOnline()) {
            sendMessage(player, "&4That player is not online.");
            return;
        }
        sendMessage(player, "&3" + target.getName() + "'s &3ping is &b" + PlayerUtil.getPlayerPing(target));
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }

}
