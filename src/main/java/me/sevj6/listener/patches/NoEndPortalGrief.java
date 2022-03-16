package me.sevj6.listener.patches;

import me.sevj6.Instance;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author 254n_m
 * Credit L2X9CoreRewrite
 */

public class NoEndPortalGrief implements Listener, Instance {

    List<Material> portal = Arrays.asList(Material.ENDER_PORTAL, Material.ENDER_PORTAL_FRAME);
    List<Material> buckets = Arrays.asList(Material.LAVA_BUCKET, Material.WATER_BUCKET);

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (config.getBoolean("Exploits.prevent-end-portal-grief")) {
            Block block = event.getBlockClicked();
            for (BlockFace face : BlockFace.values()) {
                if (portal.contains(block.getRelative(face).getType())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (config.getBoolean("Exploits.prevent-end-portal-grief")) {
            Block block = event.getBlock();
            Material type = event.getItem().getType();
            if (buckets.contains(type)) {
                for (BlockFace face : BlockFace.values()) {
                    if (portal.contains(block.getRelative(face).getType())) {
                        event.setCancelled(true);
                        break;
                    }
                }
            }
        }
    }
}
