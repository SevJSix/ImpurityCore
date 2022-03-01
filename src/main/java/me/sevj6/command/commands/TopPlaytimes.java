package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.listeners.playtimes.PlaytimeManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TopPlaytimes extends Command {

    Impurity plugin;
    PlaytimeManager manager;

    public TopPlaytimes(Impurity plugin) {
        super("toptimes", "&4Usage: &c/toptimes", plugin);
        this.plugin = plugin;
        this.manager = plugin.getPlaytimeManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String header = "&5&l------- Top Playtimes -------";
        sendMessage(sender, header);

        AtomicInteger place = new AtomicInteger(1);
        HashMap<String, Long> unSortedMap = new HashMap<>();
        for (String key : manager.getPlaytimes().getKeys(true)) {
            unSortedMap.put(key, manager.getPlaytimes().getLong(key));
        }

        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> {
                    if (place.get() > 10) return;
                    String playerName = Bukkit.getOfflinePlayer(UUID.fromString(x.getKey())).getName();
                    String playTime = manager.getFormattedPlaytime(UUID.fromString(x.getKey()));
                    sendMessage(sender, "&6" + place + ".&r &3" + playerName + ": &b" + playTime);
                    place.getAndIncrement();
                });

        sendMessage(sender, "&5&l---------------------------");
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
