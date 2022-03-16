package me.sevj6.listener.patches;

import me.sevj6.util.ObjectChecker;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ItemDropNBTBan implements Listener {

    @EventHandler
    public void onDrop(BlockBreakEvent event) {
        if (isContainer(event.getBlock())) {
            ObjectChecker<Block> objectChecker = new ObjectChecker<>(event.getBlock());
            if (objectChecker.isTagSizeTooBig()) {
                event.setCancelled(true);
                objectChecker.clearTag();
            }
        }
    }

    private boolean isContainer(Block block) {
        return block.getState() instanceof Container;
    }
}
