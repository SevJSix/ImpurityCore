package me.sevj6.listener.patches;

import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Arrays;
import java.util.List;

public class BowOppStoppa implements Listener {
    private final List<EntityType> bowList = Arrays.asList(EntityType.ARROW, EntityType.SPECTRAL_ARROW, EntityType.TIPPED_ARROW);

    private final Setting<Integer> maxVelocity = Setting.getInt("velocity.max_velocity");
    private final Setting<Boolean> doKick = Setting.getBoolean("kick.doKick");
    private final Setting<String> kickMsg = Setting.getString("kick.message");

    @EventHandler
    public void onBow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player && bowList.contains(event.getEntityType())) {
            if (event.getEntity().getVelocity().lengthSquared() > maxVelocity.getValue()) {
                event.setCancelled(true);
                if (doKick.getValue()) {
                    ((Player) event.getEntity().getShooter()).kickPlayer(kickMsg.getValue());
                }
            }
        }
    }
}
