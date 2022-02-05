package me.sevj6.listeners.patches;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;

public class BlockPerChunkLimit implements Listener, Instance {

    private final List<Material> blocks = Arrays.asList(
            Material.STANDING_BANNER, Material.WALL_BANNER, Material.ENCHANTMENT_TABLE, Material.SIGN,
            Material.WALL_SIGN, Material.HOPPER, Material.DROPPER, Material.DISPENSER, Material.BREWING_STAND,
            Material.BEACON, Material.SIGN_POST, Material.ENDER_CHEST, Material.FLOWER_POT, Material.SKULL,
            Material.SLIME_BLOCK, Material.CHEST, Material.TRAPPED_CHEST
    );

    @EventHandler
    public void chunkBanPlace(BlockPlaceEvent event) {
        Material material = event.getBlock().getType();
        if (blocks.contains(material)) {
            Chunk chunk = event.getBlock().getChunk();
            if (Utils.countBlockPerChunk(chunk, material) > 100) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                MessageUtil.sendMessage(player, "&a" + material.toString().toLowerCase().replace("_", " ") + "'s &r&3are limited to " +
                        PluginUtil.config().getInt("AntiChunkBan.amount-per-chunk") + " per chunk.");
            }
        }
    }
}