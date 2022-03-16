package me.sevj6.listener.patches;

import de.tr7zw.nbtapi.NBTTileEntity;
import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author SevJ6
 */

public class PreventCommandSigns implements Listener, Instance {

    List<Material> signs = Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN);

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (config.getBoolean("Exploits.abusive-nbt-tile-entity")) {
            if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
            if (!signs.contains(event.getClickedBlock().getType())) return;
            Sign sign = (Sign) event.getClickedBlock().getState();
            NBTTileEntity tileEntity = new NBTTileEntity(sign);
            if (tileEntity.toString().contains("run_command")) {
                event.setCancelled(true);
                sign.getBlock().breakNaturally();
                MessageUtil.log("&3Removed a command sign at &r" + Utils.formatLocation(sign.getLocation()));
            }
        }
    }
}
