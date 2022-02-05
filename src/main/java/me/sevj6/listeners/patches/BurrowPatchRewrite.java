package me.sevj6.listeners.patches;

import me.sevj6.Instance;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class BurrowPatchRewrite implements Listener, Instance {

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        Location location = event.getPlayer().getLocation();
        int x = location.getBlockX();
        int y = location.getBlockY();
        double yy = location.getY();
        int z = location.getBlockZ();
        Player player = event.getPlayer();
        Material material = event.getPlayer().getLocation().getWorld().getBlockAt(x, y, z).getType();
        boolean isBurrowed = !material.equals(Material.AIR) && (material.isOccluding() || material.equals(Material.ANVIL)) && !material.equals(Material.SOUL_SAND);
        if (isBurrowed) {
            player.teleport(new Location(location.getWorld(), location.getX(), y + 1, location.getZ(), location.getYaw(), location.getPitch()));
        }
        switch (material) {
            case ENDER_CHEST:
            case SOUL_SAND: {
                if (yy - y < 0.875) {
                    player.teleport(new Location(location.getWorld(), location.getX(), y + 1, location.getZ(), location.getYaw(), location.getPitch()));
                }
            }
            case ENCHANTMENT_TABLE: {
                if (yy - y < 0.75) {
                    player.teleport(new Location(location.getWorld(), location.getX(), y + 1, location.getZ(), location.getYaw(), location.getPitch()));
                }
            }
        }
    }
}
