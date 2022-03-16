package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.EntityEquipment;

public class EntityPickupItem implements Listener {

    @EventHandler
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (event.getItem() != null && event.getItem().getItemStack() != null) {
            LivingEntity entity = event.getEntity();
            EntityEquipment equipment = entity.getEquipment();
            if (CheckUtil.isIllegal(event.getItem().getItemStack())) {
                event.setCancelled(true);
                event.getItem().remove();
            }
            new IllegalWrapper<>(EntityEquipment.class, equipment).check();
        }
    }
}
