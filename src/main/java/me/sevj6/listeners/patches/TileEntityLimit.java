package me.sevj6.listeners.patches;

import me.sevj6.util.MessageUtil;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TileEntityLimit implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        net.minecraft.server.v1_12_R1.Chunk chunk = ((CraftChunk) event.getBlockPlaced().getChunk()).getHandle();
        if (chunk.getTileEntities().size() > 512) {
            event.setCancelled(true);
            MessageUtil.sendMessage(event.getPlayer(), "&3Too many tile entities in this chunk.");
        }
    }
}
