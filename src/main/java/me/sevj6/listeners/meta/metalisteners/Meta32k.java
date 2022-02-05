package me.sevj6.listeners.meta.metalisteners;

import me.sevj6.Impurity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Meta32k implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!Impurity.getPlugin().getMetaManager().getSettings().is32kEnabled()) {
            if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
                if (event.getDamage() > 50) event.setCancelled(true);
            }
        }
    }
}
