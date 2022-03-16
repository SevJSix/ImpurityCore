package me.sevj6.listener.dupe;

import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author SevJ6
 */

public class PistonDupe implements Listener {

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        if (PluginUtil.config().getBoolean("PistonDupe.Enabled")
                && event.getEntity().getType() == EntityType.ITEM_FRAME
                && event.getCause() == HangingBreakEvent.RemoveCause.PHYSICS
        ) {
            if (hasTooManyPistons(event.getEntity().getChunk())) return;
            ItemFrame itemFrame = (ItemFrame) event.getEntity();
            ItemStack item = itemFrame.getItem();
            if (item != null && !(item.getType() == Material.AIR)) {
                World world = itemFrame.getWorld();
                BlockFace blockFace = itemFrame.getAttachedFace();
                Location location = itemFrame.getLocation();
                Block block = world.getBlockAt(location);
                if (!(block.getRelative(blockFace).getType() == Material.AIR)) {
                    world.dropItemNaturally(location, item);
                }
            }
        }
    }

    private boolean hasTooManyPistons(Chunk chunk) {
        return Utils.countBlockPerChunk(chunk, Material.PISTON_BASE) > 20;
    }
}
