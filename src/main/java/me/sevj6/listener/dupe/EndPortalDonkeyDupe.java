package me.sevj6.listener.dupe;

import me.sevj6.Impurity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Donkey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

public class EndPortalDonkeyDupe implements Listener {

    @EventHandler
    public void onDeathAtPortal(EntityDeathEvent event) {
        if (event.getEntity() instanceof Donkey) {
            Donkey donkey = (Donkey) event.getEntity();
            if (getBlocksInRadius(donkey).stream().map(Block::getType).anyMatch(material -> material == Material.ENDER_PORTAL)) {
                event.setCancelled(true);
                Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                    if (donkey.getWorld().getEnvironment().equals(World.Environment.THE_END)) {
                        Location location = donkey.getLocation();
                        if (!location.getChunk().isLoaded()) location.getChunk().load();
                        Donkey dupedDonkey = location.getWorld().spawn(location, Donkey.class);
                        dupedDonkey.setCanPickupItems(true);
                        dupedDonkey.setCarryingChest(true);
                        dupedDonkey.getInventory().setContents(donkey.getInventory().getContents());
                        donkey.damage(1000);
                        dupedDonkey.damage(1000);
                    }
                }, 10L);
            }
        }
    }

    public List<Block> getBlocksInRadius(Donkey donkey) {
        Location loc = donkey.getLocation();
        int radius = 3;
        List<Block> blocks = new ArrayList<>();
        for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
            for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                    Location l = new Location(loc.getWorld(), x, y, z);
                    blocks.add(l.getBlock());
                }
            }
        }
        return blocks;
    }
}
