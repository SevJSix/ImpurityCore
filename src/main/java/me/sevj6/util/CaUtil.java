package me.sevj6.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CaUtil {
    private static final BlockFace[] faces = new BlockFace[]{
            BlockFace.UP,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.NORTH,
            BlockFace.WEST
    };

    private static final List<Material> types = Arrays.asList(Material.ENDER_CHEST, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.DISPENSER, Material.HOPPER, Material.RAILS, Material.EXPLOSIVE_MINECART,
            Material.ANVIL, Material.WOOD_PLATE, Material.REDSTONE_TORCH_ON, Material.BED);

    public static boolean canPlace(Location location, Player player) {
        if (player.getInventory().getItemInMainHand() != null && types.contains(player.getInventory().getItemInMainHand().getType()))
            return false;
        Location clone = location.clone();
        Block above = clone.getWorld().getBlockAt(clone).getRelative(BlockFace.UP); //Check if there is air above the block the player clicked
        if (above.getType() != Material.AIR) return false;
        Collection<Entity> nearby = clone.add(0.5, 1, 0.5).getNearbyEntities(0.5, 1, 0.5);
        return nearby.isEmpty();
    }

    public static boolean shouldPlace(Location location) {
        Location clone = location.clone();
        Location crystal = clone.add(0.5, 1, 0.5);
        for (BlockFace face : faces) {
            if (crystal.getWorld().getBlockAt(crystal).getRelative(face).getType() != Material.AIR) {
                return true;
            }
        }
        return false;
    }
}
