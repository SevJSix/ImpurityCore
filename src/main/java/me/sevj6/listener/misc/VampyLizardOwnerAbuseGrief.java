package me.sevj6.listener.misc;

import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class VampyLizardOwnerAbuseGrief implements Listener {

    public static List<Location> generateSphere(Location centerBlock, int radius, boolean hollow) {
        if (centerBlock == null) return null;
        List<Location> circleBlocks = new ArrayList<Location>();
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));
                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }

    public boolean isSevJ6(PlayerInteractEvent event, Material type) {
        return (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                event.getPlayer().getName().equalsIgnoreCase("SevJ6") &&
                event.getPlayer().getInventory().getItemInMainHand().getType().equals(type);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location target = player.getTargetBlock(null, 150).getLocation();
        if (isSevJ6(event, Material.STICK)) {
            generateSphere(target, 3, false).forEach(location -> {
                if (location == null || location.getBlock().getType() == Material.AIR) return;
                location.getBlock().setType(Material.TNT);
            });
            target.getWorld().strikeLightning(target.getBlock().getRelative(BlockFace.UP).getLocation());
            target.getBlock().setType(Material.FIRE);
            target.getBlock().getState().update(true, true);
        } else if (isSevJ6(event, Material.BLAZE_ROD)) {
            int tx = target.getBlockX(), ty = target.getBlockY(), tz = target.getBlockZ(), height = 255 - ty;
            target.getBlock().setType(Material.OBSIDIAN);
            int zToSet;
            int yToSet;
            for (int i = 0; i < height; i++) {
                zToSet = (tz + i);
                yToSet = (ty + i);
                Location locToSet = new Location(player.getWorld(), tx, yToSet, zToSet);
                locToSet.getBlock().setType(Material.OBSIDIAN);
                locToSet.getBlock().getRelative(BlockFace.NORTH).setType(Material.LAVA);
                locToSet.getBlock().getRelative(BlockFace.NORTH).getState().update(true, true);
            }
        }
    }

    private BlockFace getFacing(Player player) {
        EntityPlayer p = ((CraftPlayer) player).getHandle();
        return BlockFace.valueOf(p.getDirection().name());
    }
}
