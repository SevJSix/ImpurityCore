package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.ObjectChecker;
import me.sevj6.util.TimerUtil;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @author Sevj6
 */

public class InteractEventNerf extends ViolationManager implements Listener, Instance {

    private final Setting<Integer> rateLimit = Setting.getInt("maps.rate_limit_ms");
    private final Setting<Boolean> doMapRateLimit = Setting.getBoolean("maps.enabled");
    private final Setting<Boolean> checkBlockTileSize = Setting.getBoolean("interact.block.check_tile_tag_size");
    private final Setting<Boolean> checkIfPowersRedstone = Setting.getBoolean("interact.lever_flick.check_if_powering_redstone");
    private final Setting<Boolean> checkLeverLag = Setting.getBoolean("interact.lever_flick.enabled");
    TimerUtil mapTimer = new TimerUtil();

    public InteractEventNerf() {
        super(1, 1);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = (event.getHand() == EquipmentSlot.OFF_HAND) ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
        if (doMapRateLimit.getValue()) {
            if (handItem != null) {
                if (handItem.getType().equals(Material.MAP) || handItem.getType().equals(Material.EMPTY_MAP)) {
                    if (mapTimer.hasReached(rateLimit.getValue())) {
                        mapTimer.reset();
                    } else {
                        event.setCancelled(true);
                    }
                }
            }
        }

        if (event.getClickedBlock() != null) {
            ObjectChecker<Block> block = new ObjectChecker<>(event.getClickedBlock());
            if (checkBlockTileSize.getValue()) {
                if (block.isTagSizeTooBig()) {
                    event.setCancelled(true);
                    block.check();
                    return;
                }

                if (block.isBlockContainingBookNBT() || block.getTags() != null && block.getTags().toString().contains("generation")) {
                    increment(player.getUniqueId());
                    if (getVLS(player.getUniqueId()) > 4) {
                        event.setCancelled(true);
                        player.kickPlayer("Slow down while interacting with containers with books in them");
                    }
                    return;
                }

                if (block.isBlockAndContainsBlockEntityTag()) {
                    increment(player.getUniqueId());
                    if (getVLS(player.getUniqueId()) > 6) {
                        event.setCancelled(true);
                        player.kickPlayer("Slow down while interacting with containers with large NBT Tags");
                    }
                    return;
                }
            }

            if (checkLeverLag.getValue()) {
                if (block.isMaterialType(Material.LEVER)) {
                    if (checkIfPowersRedstone.getValue()) {
                        if (block.getBlocksInRadius(4).stream().anyMatch(b -> b.getType() == Material.REDSTONE_WIRE)) {
                            increment(player.getUniqueId());
                        }
                    } else {
                        increment(player.getUniqueId());
                    }
                    if (getVLS(player.getUniqueId()) > 8) {
                        event.setCancelled(true);
                        player.kickPlayer("Kicked for exceedingly fast lever interaction (possible lag attempt?)");
                    }
                }
            }
        }
    }
}
