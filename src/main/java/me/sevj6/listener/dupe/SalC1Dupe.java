package me.sevj6.listener.dupe;

import me.sevj6.Instance;
import me.sevj6.util.TimerUtil;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author SevJ6
 */

public class SalC1Dupe implements Listener, Instance {

    private final Setting<Boolean> enabled = Setting.getBoolean("dupe.salc1_dupe");
    TimerUtil timer = new TimerUtil();

    @EventHandler
    public void onVehicleEnter(PlayerInteractAtEntityEvent event) {
        if (enabled.getValue()) {
            if (event.getRightClicked() instanceof Llama || event.getRightClicked() instanceof Mule || event.getRightClicked() instanceof Donkey) {
                if ((event.getPlayer().getInventory().getItemInMainHand().getType() == Material.CHEST) || (event.getPlayer().getInventory().getItemInOffHand().getType() == Material.CHEST)) {
                    PlayerDupeEvent playerDupeEvent = new PlayerDupeEvent(event.getPlayer(), event.getPlayer().getChunk(), event.getRightClicked());
                    Bukkit.getServer().getPluginManager().callEvent(playerDupeEvent);
                    timer.reset();
                    ChestedHorse entity = (ChestedHorse) event.getRightClicked();
                    if (entity.getPassenger() == null) {
                        entity.setPassenger(event.getPlayer());
                    }
                    event.setCancelled(true);
                    for (ItemStack item : entity.getInventory().getContents()) {
                        if (item != null) {
                            if (!(item.getType() == Material.SADDLE)) {
                                entity.getWorld().dropItemNaturally(entity.getLocation(), item);
                            }
                        }
                    }
                    entity.setCarryingChest(false);
                }
            }
        }
    }

    @EventHandler
    public void onDupe(PlayerDupeEvent event) {
        plugin.getLogger().log(Level.INFO, "Player " + event.getPlayer().getName() + " duped at " + event.getPlayer().getLocation());
        List<Item> allDrops = getDrops(event.getChunk());
        if (allDrops.size() > 50)
            plugin.getLogger().log(Level.WARNING, "Detected duping with an excess amount of entities in a chunk. Culling entities...");
        while (allDrops.size() > 50) {
            allDrops.get(allDrops.size() - 1).remove();
            allDrops.remove(allDrops.size() - 1);
        }
    }

    private List<Item> getDrops(Chunk chunk) {
        List<Item> drops = new ArrayList<>();
        Arrays.stream(chunk.getEntities()).filter(entity -> entity instanceof Item).forEach(entity -> drops.add((Item) entity));
        return drops;
    }
}
