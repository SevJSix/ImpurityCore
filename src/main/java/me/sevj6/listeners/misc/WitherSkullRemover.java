package me.sevj6.listeners.misc;

import me.sevj6.Instance;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWitherSkull;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SevJ6
 */

public class WitherSkullRemover implements Listener, Instance {

    private final int maxPerLog = config.getInt("WitherSkullHandling.TraveledTooLong.amount-per-console-log");
    private int amount = 0;

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        if (config.getBoolean("WitherSkullHandling.Enabled") && config.getBoolean("WitherSkullHandling.DeleteOnChunkLoad.Enabled")) {
            List<CraftWitherSkull> skulls = new ArrayList<>();
            for (Entity entity : event.getChunk().getEntities()) {
                if (entity instanceof CraftWitherSkull) skulls.add((CraftWitherSkull) entity);
            }
            if (skulls.size() > 0) {
                skulls.forEach(Entity::remove);
                if (config.getBoolean("WitherSkullHandling.DeleteOnChunkLoad.log-console")) {
                    plugin.getLogger().info("Removed " + skulls.size() + " WitherSkulls from a chunk in " + event.getChunk().getWorld());
                }
            }
        }
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (config.getBoolean("WitherSkullHandling.Enabled") && config.getBoolean("WitherSkullHandling.TraveledTooLong.Enabled")) {
            if (event.getEntity() instanceof CraftWitherSkull) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (event.getEntity().isDead()) return;
                    event.getEntity().remove();
                    if (config.getBoolean("WitherSkullHandling.log-console")) {
                        amount++;
                        if (amount == maxPerLog) {
                            amount -= maxPerLog;
                            plugin.getLogger().info("Routinely removed flying WitherSkulls that were traveling for too long.");
                        }
                    }
                }, 20 * config.getLong("WitherSkullHandling.TraveledTooLong.time-limit"));
            }
        }
    }
}
