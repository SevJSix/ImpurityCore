package me.sevj6.listener.dupe;

import me.sevj6.Impurity;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntityHopper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.atomic.AtomicBoolean;

public class LavaDupe implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("dupe.lava_dupe");

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!enabled.getValue()) return;
        if (!(event.getEntity() instanceof Item)) return;
        EntityDamageEvent.DamageCause cause = event.getCause();
        Item item = (Item) event.getEntity();
        Location location = item.getLocation().clone().add(0, -1, 0);
        if (cause == EntityDamageEvent.DamageCause.FIRE) {
            AtomicBoolean dead = new AtomicBoolean(false);
            Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                if (item.isDead()) dead.set(true);
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                    if (dead.get()) {
                        handleDupe(location, item);
                    }
                });
            }, 3L);
        }
    }

    public void handleDupe(Location location, Item item) {
        BlockPosition possibleHopperPos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        TileEntityHopper hopper = (TileEntityHopper) ((CraftWorld) location.getWorld()).getHandle().getTileEntity(possibleHopperPos);
        if (hopper != null) {
            Hopper blockHopper = (Hopper) location.getBlock().getState();
            int slot = blockHopper.getInventory().firstEmpty();
            if (slot == -1) {
                location.getWorld().dropItemNaturally(location, item.getItemStack());
                return;
            }
            blockHopper.getInventory().addItem(item.getItemStack(), item.getItemStack());
        }
    }
}
