package me.sevj6.listeners.playtimes;

import me.sevj6.Impurity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlaytimeListeners implements Listener {

    private final Impurity plugin;
    private final HashMap<Player, Long> playerMap;
    private final PlaytimeManager manager;

    public PlaytimeListeners(Impurity plugin) {
        this.plugin = plugin;
        this.playerMap = new HashMap<>();
        manager = plugin.getPlaytimeManager();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerMap.put(event.getPlayer(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        long timeToAdd = (System.currentTimeMillis() - playerMap.get(player));
        if (manager.getPlaytimes().contains(player.getUniqueId().toString()))
            timeToAdd += (this.plugin.getPlaytimeManager().getPlaytimes().getLong(player.getUniqueId().toString()));
        handlePlaytime(player.getUniqueId(), timeToAdd);
        playerMap.remove(player);
    }

    private void handlePlaytime(UUID uuid, long time) {
        manager.getPlaytimes().set(uuid.toString(), time);
        manager.getPlaytimes().saveConfig();
        manager.getPlaytimes().reloadConfig();
    }
}
