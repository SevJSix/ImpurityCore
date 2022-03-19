package me.sevj6.listener.patches;

import me.sevj6.util.ObjectChecker;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ItemDropNBTBan implements Listener {

    private final Setting<Boolean> checkNBT = Setting.getBoolean("checkNBT.enabled");
    private final Setting<Boolean> clearNBT = Setting.getBoolean("checkNBT.clear_nbt_if_tag_too_big");

    @EventHandler
    public void onDrop(BlockBreakEvent event) {
        if (checkNBT.getValue()) {
            if (isContainer(event.getBlock())) {
                ObjectChecker<Block> objectChecker = new ObjectChecker<>(event.getBlock());
                if (objectChecker.isTagSizeTooBig()) {
                    event.setCancelled(true);
                    if (clearNBT.getValue()) {
                        objectChecker.clearTag();
                    }
                }
            }
        }
    }

    private boolean isContainer(Block block) {
        return block.getState() instanceof Container;
    }
}
