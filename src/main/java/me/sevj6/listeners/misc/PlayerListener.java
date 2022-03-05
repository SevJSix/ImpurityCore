package me.sevj6.listeners.misc;

import me.sevj6.Impurity;
import me.sevj6.util.ObjectChecker;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
        ObjectChecker<Player> playerObjectChecker = new ObjectChecker<>(event.getPlayer());
        playerObjectChecker.check();
        playerObjectChecker.getNearbyItemDrops(20).forEach(itemStack -> {
            new ObjectChecker<>(itemStack).check();
        });
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
    }
}
