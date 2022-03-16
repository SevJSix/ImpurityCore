package me.sevj6.listener.patches;

import me.sevj6.Impurity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.List;
import java.util.logging.Level;

public class ThrownEntityDelete implements Listener {

    private final List<String> types = Impurity.getPlugin().getConfig().getStringList("entity_types");

    private boolean isEntityOfType(Entity entity) {
        try {
            for (String type : types) {
                EntityType entityType = EntityType.valueOf(type);
                if (entity.getType() == entityType) return true;
            }
        } catch (Throwable t) {
            Impurity.getPlugin().getLogger().log(Level.SEVERE, "Invalid Entity Types for ProjectileLaunchEvent. Change your configuration!");
        }
        return false;
    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {
        if (isEntityOfType(event.getEntity())) {
            Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                if (event.getEntity().isDead()) return;
                event.getEntity().remove();
            }, 20L);
        }
    }
}
