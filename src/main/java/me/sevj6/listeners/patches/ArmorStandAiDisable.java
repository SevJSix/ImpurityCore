package me.sevj6.listeners.patches;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;

public class ArmorStandAiDisable implements Listener {

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            if (Arrays.stream(event.getEntity().getChunk().getEntities()).filter(entity -> entity instanceof ArmorStand).count() > 25) {
                event.setCancelled(true);
                return;
            }
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            armorStand.setAI(false);
        }
    }
}
