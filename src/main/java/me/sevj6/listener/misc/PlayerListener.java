package me.sevj6.listener.misc;

import me.sevj6.Impurity;
import me.sevj6.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class PlayerListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
        Player player = event.getPlayer();
        boolean isInsideChunkban = PlayerUtil.checkForTooLargeNBTAroundPlayer(player);
        if (isInsideChunkban) {
            Impurity.getPlugin().getLogger().log(Level.WARNING, "Possible chunkban at " + player.getLocation());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Impurity.getPlugin().getViolationManagers().forEach(v -> v.remove(event.getPlayer().getUniqueId()));
    }
}
