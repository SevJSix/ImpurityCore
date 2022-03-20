package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.patches.DispenserExploits;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class BlockDispense implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.BlockPlace");

    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        if (enabled.getValue()) {
            ItemStack itemStack = event.getItem();
            if (CheckUtil.isIllegal(itemStack)) {
                event.setCancelled(true);
                DispenserExploits.clearTileEntityInventory(event.getBlock().getLocation());
                return;
            }
            Block block = event.getBlock();
            if (block.getState() instanceof Dispenser) {
                Dispenser dispenser = (Dispenser) block.getState();
                for (ItemStack stack : dispenser.getInventory()) {
                    if (stack != null) {
                        if (CheckUtil.isIllegal(stack)) {
                            event.setCancelled(true);
                            DispenserExploits.clearTileEntityInventory(dispenser.getLocation());
                            break;
                        } else if (CheckUtil.isShulker(stack)) {
                            ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) stack.getItemMeta()).getBlockState();
                            for (ItemStack shulkerStack : shulkerBox.getInventory()) {
                                if (shulkerStack != null) {
                                    if (CheckUtil.isIllegal(shulkerStack)) {
                                        event.setCancelled(true);
                                        dispenser.getInventory().clear();
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
