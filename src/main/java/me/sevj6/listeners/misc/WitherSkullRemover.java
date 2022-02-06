package me.sevj6.listeners.misc;

import me.sevj6.Instance;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftWitherSkull;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;

/**
 * @author SevJ6
 */

public class WitherSkullRemover extends TimerTask implements Listener, Instance {

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


    @Override
    public void run() {
        if (config.getBoolean("WitherSkullHandling.Enabled") && config.getBoolean("WitherSkullHandling.DeleteAllOnStartup.Enabled")) {
            List<CraftWitherSkull> skulls = new ArrayList<>();
            for (World world : Bukkit.getWorlds()) {
                for (Chunk chunk : world.getLoadedChunks()) {
                    for (Entity entity : chunk.getEntities()) {
                        if (entity instanceof CraftWitherSkull) {
                            skulls.add((CraftWitherSkull) entity);
                        }
                    }
                }
            }
            if (skulls.size() > 10) {
                skulls.forEach(Entity::remove);
                if (config.getBoolean("WitherSkullHandling.DeleteAllOnStartup.log-console")) {
                    plugin.getLogger().log(Level.WARNING, "Removed " + skulls.size() + " excess WitherSkulls from all loaded spawn chunks.");
                }
            }
        }
    }
}
