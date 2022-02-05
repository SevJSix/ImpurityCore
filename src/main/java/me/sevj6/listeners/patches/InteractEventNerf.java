package me.sevj6.listeners.patches;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.sevj6.Instance;
import me.sevj6.util.TimerUtil;
import me.sevj6.util.ViolationManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author Sevj6
 */

public class InteractEventNerf extends ViolationManager implements Listener, Instance {

    TimerUtil timer = new TimerUtil();
    TimerUtil chestLimit = new TimerUtil();
    TimerUtil echestLimit = new TimerUtil();
    TimerUtil leverLimit = new TimerUtil();

    public InteractEventNerf() {
        super(1, 1);
    }

    @EventHandler
    public void onMap(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        PlayerInventory i = player.getInventory();
        if (i.getItemInMainHand().getType() == Material.EMPTY_MAP || i.getItemInOffHand().getType() == Material.EMPTY_MAP) {
            if (timer.hasReached(5000)) timer.reset();
            else event.setCancelled(true);
        } else if (isType(event, Material.CHEST)) {
            Block block = event.getClickedBlock();
            NBTTileEntity nbtTileEntity = new NBTTileEntity(block.getState());
            String s = nbtTileEntity.toString();
            if (s.contains("BlockEntityTag")) {
                if (chestLimit.hasReached(200)) {
                    chestLimit.reset();
                    increment(player.getUniqueId());
                    if (getVLS(player.getUniqueId()) > 5 || Bukkit.getTPS()[0] < 19 && getVLS(player.getUniqueId()) > 3) {
                        event.setCancelled(true);
                        player.kickPlayer("Slow down while interacting with containers with large NBT Tags");
                    }
                } else event.setCancelled(true);
            } else if (s.contains("generation") && s.length() > 1000) {
                if (chestLimit.hasReached(200)) {
                    chestLimit.reset();
                    increment(player.getUniqueId());
                    if (getVLS(player.getUniqueId()) > 1) {
                        event.setCancelled(true);
                        player.kickPlayer("Kicked for sending and receiving too large book NBT data");
                    }
                } else event.setCancelled(true);
            }
        } else if (isType(event, Material.ENDER_CHEST)) {
            if (echestLimit.hasReached(150)) echestLimit.reset();
            else event.setCancelled(true);
        } else if (isType(event, Material.LEVER)) {
            if (leverLimit.hasReached(70)) {
                leverLimit.reset();
                boolean isLagAttempt = false;
                Block block = event.getClickedBlock();
                for (BlockFace face : BlockFace.values()) {
                    Block firstRelative = block.getRelative(face);
                    Block secondRelative = firstRelative.getRelative(face);
                    if (firstRelative.getType() == Material.REDSTONE_WIRE || secondRelative.getType() == Material.REDSTONE_WIRE) {
                        isLagAttempt = true;
                        break;
                    }
                }
                if (isLagAttempt) {
                    increment(player.getUniqueId());
                    if (getVLS(player.getUniqueId()) > 6) {
                        event.setCancelled(true);
                        player.kickPlayer("Kicked for exceedingly fast lever interaction (possible lag attempt?)");
                    }
                }
            } else event.setCancelled(true);
        }
    }


    private boolean isType(PlayerInteractEvent event, Material type) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getClickedBlock() == null) return false;
        return event.getClickedBlock().getType() == type;
    }
}
