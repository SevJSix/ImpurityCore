package me.sevj6.listeners.patches;

import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;

import java.util.Arrays;

public class EntityThroughPortalLag implements Listener {

    /**
     * @author SevJ6
     * not configurable but who cares
     */

    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        if (checkEntity(event.getEntity())) {
            event.setCancelled(true);
            event.getEntity().remove();
            return;
        }
        if (event.getEntity().getChunk().getEntities().length > 20 || Bukkit.getTPS()[0] < 19.3) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortalExit(EntityPortalExitEvent event) {
        if (checkEntity(event.getEntity())) {
            event.setCancelled(true);
            event.getEntity().remove();
            return;
        }
        if (event.getTo().getChunk().getEntities().length > 20 || Bukkit.getTPS()[0] < 19.3) {
            event.setCancelled(true);
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onPortalEnter(EntityPortalEnterEvent event) {
        Entity[] entities = event.getLocation().getNearbyEntities(4, 4, 4).toArray(new Entity[0]);
        if (entities.length > 30 || Bukkit.getTPS()[0] < 19.3) {
            Arrays.stream(entities).filter(entity -> entity instanceof Item).forEach(Entity::remove);
        }
    }

    private boolean checkEntity(Entity entity) {
        return entity instanceof ExperienceOrb || entity instanceof FallingBlock || entity instanceof Explosive;
    }
}
