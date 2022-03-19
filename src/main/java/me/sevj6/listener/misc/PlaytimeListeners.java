package me.sevj6.listener.misc;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlaytimeListeners implements Listener, Instance {

    private final Impurity plugin;
    private final HashMap<Player, Long> playerMap;

    public PlaytimeListeners(Impurity plugin) {
        this.plugin = plugin;
        this.playerMap = new HashMap<>();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerMap.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long timeToAdd = (System.currentTimeMillis() - playerMap.get(player));
        if (playtimes.contains(player.getUniqueId().toString()))
            timeToAdd += (playtimes.getLong(player.getUniqueId().toString()));
        handlePlaytime(player.getUniqueId(), timeToAdd);
        playerMap.remove(player);
    }

    private void handlePlaytime(UUID uuid, long time) {
        playtimes.set(uuid.toString(), time);
        playtimes.saveConfig();
        playtimes.reloadConfig();
    }
}
