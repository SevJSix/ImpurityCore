package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.check.CheckUtil;
import me.sevj6.listener.patches.DispenserExploits;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.ItemStack;

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
                    if (CheckUtil.isIllegal(stack)) {
                        DispenserExploits.clearTileEntityInventory(dispenser.getLocation());
                        break;
                    }
                }
            }
        }
    }
}
