package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.CheckUtil;
import me.sevj6.listeners.illegals.wrapper.ObjectWrapper;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityEnterPortal implements Listener {

    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            new ObjectWrapper<>(player, Player.class).check();
        } else if (event.getEntity() instanceof Item) {
            Item item = (Item) event.getEntity();
            if (item.getItemStack() == null) return;
            ItemStack itemStack = item.getItemStack();
            if (CheckUtil.isIllegal(itemStack)) {
                event.setCancelled(true);
                item.remove();
            }
        } else if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) event.getEntity();
            EntityEquipment entityEquipment = entity.getEquipment();
            new ObjectWrapper<>(entityEquipment, EntityEquipment.class).check();
        }
    }
}
