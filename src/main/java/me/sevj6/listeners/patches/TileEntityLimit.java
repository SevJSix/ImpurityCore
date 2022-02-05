package me.sevj6.listeners.patches;

import me.sevj6.util.MessageUtil;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntity;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TileEntityLimit implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        net.minecraft.server.v1_12_R1.Chunk chunk = ((CraftChunk) event.getBlockPlaced().getChunk()).getHandle();
        if (chunk.getTileEntities().size() > 512) {
            Block block = event.getBlockPlaced();
            TileEntity tileEntity = ((CraftWorld) block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            if (tileEntity != null) {
                event.setCancelled(true);
                MessageUtil.sendMessage(event.getPlayer(), "&3Too many tile entities in this chunk.");
            }
        }
    }
}
