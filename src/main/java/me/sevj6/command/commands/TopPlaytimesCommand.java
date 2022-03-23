package me.sevj6.command.commands;

import me.sevj6.Instance;
import me.sevj6.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TopPlaytimesCommand implements CommandExecutor, Instance {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String header = "&5&l------- Top Playtimes -------";
        sendMsg(sender, header);

        AtomicInteger place = new AtomicInteger(1);
        HashMap<String, Long> unSortedMap = new HashMap<>();
        for (String key : playtimes.getKeys(true)) {
            unSortedMap.put(key, playtimes.getLong(key));
        }

        unSortedMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> {
                    if (place.get() > 10) return;
                    String playerName = Bukkit.getOfflinePlayer(UUID.fromString(x.getKey())).getName();
                    String playTime = Utils.getFormattedPlaytime(UUID.fromString(x.getKey()));
                    sendMsg(sender, "&6" + place + ".&r &3" + playerName + ": &b" + playTime);
                    place.getAndIncrement();
                });

        sendMsg(sender, "&5&l---------------------------");
        return true;
    }
}
