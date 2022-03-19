package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.TileEntity;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPerChunkLimit implements Listener, Instance {

    private final Setting<Boolean> doBlockPerChunkLimit = Setting.getBoolean("limit_tiles_per_chunk");
    private final Setting<Integer> maxTiles = Setting.getInt("max_tiles");
    private final Setting<Integer> maxPerType = Setting.getInt("max_per_type");

    @EventHandler
    public void chunkBanPlace(BlockPlaceEvent event) {
        if (doBlockPerChunkLimit.getValue()) {
            Block block = event.getBlockPlaced();
            boolean tooManyTiles = checkChunkTiles(block);
            if (tooManyTiles) {
                event.setCancelled(true);
                MessageUtil.sendMessage(event.getPlayer(), "&3Too many tile entities in this chunk.");
                return;
            }

            TileEntity tileEntity = ((CraftWorld) block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            if (tileEntity != null) {
                net.minecraft.server.v1_12_R1.Block b = tileEntity.getBlock();
                if (!b.isTileEntity()) return;
                Material material = event.getBlock().getType();
                Chunk chunk = event.getBlock().getChunk();
                if (Utils.countBlockPerChunk(chunk, material) > maxPerType.getValue()) {
                    event.setCancelled(true);
                    Player player = event.getPlayer();
                    MessageUtil.sendMessage(player, "&a" + material.toString().toLowerCase().replace("_", " ") + "'s &r&3are limited to " +
                            maxPerType.getValue() + " per chunk.");
                }
            }
        }
    }

    private boolean checkChunkTiles(Block block) {
        if (((CraftChunk) block.getChunk()).getHandle().getTileEntities().size() > maxTiles.getValue()) {
            TileEntity tileEntity = ((CraftWorld) block.getWorld()).getHandle().getTileEntity(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            return tileEntity != null;
        }
        return false;
    }
}