package me.sevj6.listeners.patches;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.Arrays;
import java.util.List;

public class BowOppStoppa implements Listener {
    private List<EntityType> bowList = Arrays.asList(EntityType.ARROW, EntityType.SPECTRAL_ARROW, EntityType.TIPPED_ARROW);

    @EventHandler
    public void onBow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player && bowList.contains(event.getEntityType())) {
            if (event.getEntity().getVelocity().lengthSquared() > 11) {
                event.setCancelled(true);
                ((Player) event.getEntity().getShooter()).kickPlayer("Opp detected, arrow deflected");
            }
        }
    }
}
