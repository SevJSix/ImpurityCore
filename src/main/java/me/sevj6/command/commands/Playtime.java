package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.listeners.playtimes.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Playtime extends Command {

    Impurity plugin;
    PlaytimeManager manager;

    public Playtime(Impurity plugin) {
        super("playtime", "&4Usage: &c/playtime <player>", plugin);
        this.plugin = plugin;
        this.manager = plugin.getPlaytimeManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            OfflinePlayer self = Bukkit.getOfflinePlayer(((Player) sender).getUniqueId());
            if (manager.getPlaytimes().contains(self.getUniqueId().toString())) {
                String selfPlaytime = manager.getFormattedPlaytime(self.getUniqueId());
                sendMessage(sender, "&3Your playtime is: &b" + selfPlaytime + "&r\n&3Your playtime will be updated the next time you leave.");
            } else {
                sendMessage(sender, "&4Your playtime has not yet been calculated. Re-join to see your playtime.");
            }
            return;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
        if (!player.hasPlayedBefore()) sendMessage(sender, "&4That player has never joined the server before.");
        if (!manager.getPlaytimes().contains(String.valueOf(player.getUniqueId()))) {
            sendMessage(sender, "&4That player's playtime has not yet been calculated.");
            return;
        }
        String playtime = manager.getFormattedPlaytime(player.getUniqueId());
        sendMessage(sender, "&3" + args[0] + "'s playtime: &b" + playtime + "&r\n&3Their playtime will be updated the next time they leave.");
    }
}
