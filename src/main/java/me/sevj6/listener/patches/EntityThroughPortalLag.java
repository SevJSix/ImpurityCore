package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class EntityThroughPortalLag implements Listener, Instance {

    private final Setting<Boolean> stopRidableEntities = Setting.getBoolean("portals.stop_rideable_entities");
    private final Setting<List<String>> blacklistedEntityTypes = Setting.getStringList("portals.blacklisted_entities");

    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        if (stopRidableEntities.getValue()) {
            if (event.getEntity().isInsideVehicle()) {
                event.setCancelled(true);
                return;
            }
        }
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
        if (stopRidableEntities.getValue()) {
            if (event.getEntity().isInsideVehicle()) {
                event.setCancelled(true);
                return;
            }
        }
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
        try {
            for (String s : blacklistedEntityTypes.getValue()) {
                return EntityType.valueOf(s).equals(entity.getType());
            }
        } catch (Throwable t) {
            plugin.getLogger().log(Level.SEVERE, "Invalid entity type! check your blacklisted_entities config in settings.yml");
        }
        return false;
    }
}
