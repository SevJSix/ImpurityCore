package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.TippedArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ThrownEntityDelete implements Listener {

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (event.getEntity() instanceof ThrownExpBottle || event.getEntity() instanceof Snowball || event.getEntity() instanceof Arrow || event.getEntity() instanceof TippedArrow) {
            Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                if (event.getEntity().isDead()) return;
                event.getEntity().remove();
            }, 20L);
        }
    }
}
