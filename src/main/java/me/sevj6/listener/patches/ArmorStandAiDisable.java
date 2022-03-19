package me.sevj6.listener.patches;

import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.Arrays;

public class ArmorStandAiDisable implements Listener {

    private final Setting<Integer> maxCount = Setting.getInt("armor_stand.spawn_limit_per_chunk");
    private final Setting<Boolean> disableAI = Setting.getBoolean("armor_stand.disable_ai");

    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            if (Arrays.stream(event.getEntity().getChunk().getEntities()).filter(entity -> entity instanceof ArmorStand).count() > maxCount.getValue()) {
                event.setCancelled(true);
                return;
            }
            if (disableAI.getValue()) {
                ArmorStand armorStand = (ArmorStand) event.getEntity();
                armorStand.setAI(false);
            }
        }
    }
}
