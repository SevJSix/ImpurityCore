package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class AntiAfkBedTrapKilling implements Listener {

    private final HashMap<Player, Integer> playerKillMap;

    public AntiAfkBedTrapKilling() {
        this.playerKillMap = new HashMap<>();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (playerKillMap.containsKey(player)) {
            if (playerKillMap.get(player) > 4) event.setCancelled(true);
            playerKillMap.replace(player, (playerKillMap.get(player) + 1));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player playerDamaged = (Player) event.getEntity();
            if (playerKillMap.containsKey(playerDamaged)) return;
            if (playerDamaged.getBedSpawnLocation() != null && playerDamaged.getLocation().distance(playerDamaged.getBedSpawnLocation()) < 15) {
                playerKillMap.put(playerDamaged, 0);
                Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                    playerKillMap.remove(playerDamaged);
                }, 20 * 40L);
            }
        }
    }
}
