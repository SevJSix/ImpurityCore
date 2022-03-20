package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class EntityEnterPortal implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.EntityEnterPortal");

    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        if (enabled.getValue()) {
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                new IllegalWrapper<>(Player.class, player).check();
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
                new IllegalWrapper<>(EntityEquipment.class, entityEquipment).check();
            }
        }
    }
}
