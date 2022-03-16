package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * @author SevJ6
 */

public class RedstoneEvents extends Utils implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRedstone(BlockRedstoneEvent event) {
        if (getRawTps() < Instance.config.getDouble("Exploits.redstone-tps-disable")) event.setNewCurrent(0);
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent event) {
        Location location = event.getBlock().getLocation();
        if ((long) location.getNearbyEntitiesByType(EnderCrystal.class, 1, 1, 1).size() > 0) {
            event.setCancelled(true);
        }
        if (PluginUtil.config().getBoolean("Exploits.piston-events")) {
            if (Utils.getRawTps() < Instance.config.getDouble("Exploits.piston-tps-disable")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPiston(BlockPistonRetractEvent event) {
        if (PluginUtil.config().getBoolean("Exploits.piston-events")) {
            if (Utils.getRawTps() < Instance.config.getDouble("Exploits.piston-tps-disable")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Chunk chunk = block.getChunk();
        Material material = block.getType();

        switch (material) {
            case REDSTONE_WIRE:
                if (Utils.countBlockPerChunk(chunk, Material.REDSTONE_WIRE) > 40) {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(player, "&bredstone wire's &3are limited to 40 per chunk.");
                }
                break;
            case REDSTONE_COMPARATOR:
                if (Utils.countBlockPerChunk(chunk, Material.REDSTONE_COMPARATOR) > 40) {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(player, "&bredstone comparators's &3are limited to 40 per chunk.");
                }
                break;
            case DIODE_BLOCK_OFF:
                if (Utils.countBlockPerChunk(chunk, Material.DIODE_BLOCK_OFF) > 30) {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(player, "&bredstone repeater's &3are limited to 30 per chunk.");
                }
                break;
            case OBSERVER:
                if (Utils.countBlockPerChunk(chunk, Material.OBSERVER) > 35) {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(player, "&bobservers's are &3limited to 35 per chunk.");
                }
                break;
            case REDSTONE_LAMP_OFF:
                if (Utils.countBlockPerChunk(chunk, Material.REDSTONE_LAMP_OFF) > 70) {
                    event.setCancelled(true);
                    MessageUtil.sendMessage(player, "&bredstone lamp's &3are limited to 70 per chunk.");
                }
                break;
        }
    }
}
