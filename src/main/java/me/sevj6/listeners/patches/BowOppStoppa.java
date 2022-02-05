package me.sevj6.listeners.patches;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class BowOppStoppa implements Listener {

    @EventHandler
    public void onBow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            if (event.getEntity().getVelocity().lengthSquared() > 11) {
                event.setCancelled(true);
                ((Player) event.getEntity().getShooter()).kickPlayer("Opp detected, arrow deflected");
            }
        }
    }
}
