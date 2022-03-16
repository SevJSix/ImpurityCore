package me.sevj6.listener.patches;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BlockPhysicsLag implements Listener {

    @EventHandler
    public void onPhysics(EntityChangeBlockEvent event) {
        Block block = event.getBlock();
        Entity entity = event.getEntity();
        int surrounding = entity.getLocation().getNearbyEntitiesByType(FallingBlock.class, 4, 4, 4).size();
        if (surrounding >= 5) {
            event.setCancelled(true);
            Arrays.stream(entity.getChunk().getEntities()).filter(e -> e instanceof FallingBlock).forEach(Entity::remove);
        }
        if (block.getType().hasGravity() && block.getType() != Material.ANVIL) {
            boolean hasBlocksAbove =
                    block.getRelative(BlockFace.UP).getType().hasGravity()
                            && block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().hasGravity()
                            && block.getRelative(BlockFace.UP).getRelative(BlockFace.UP).getRelative(BlockFace.UP).getType().hasGravity();
            if (hasBlocksAbove) {
                event.setCancelled(true);
                entity.remove();
                block.setType(Material.AIR);
                return;
            }
            if (surrounding >= 1) {
                event.setCancelled(true);
                entity.remove();
                entity.getWorld().dropItemNaturally(entity.getLocation(), new ItemStack(block.getType()));
                block.setType(Material.AIR);
            }
        }
    }
}
