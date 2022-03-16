package me.sevj6.listener.patches;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.PluginUtil;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * @author 254n_m
 * Credit GodModePatch
 * Code modified by SevJ6
 */

public class GodmodePatch implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (PluginUtil.config().getBoolean("Exploits.godmode") && event.getPlayer().isInsideVehicle()) { // TO DO: Only register this event if Exploits.godmode is true
            Player player = event.getPlayer();
            Vehicle vehicle = (Vehicle) player.getVehicle();
            Chunk playerChunk = player.getChunk();
            assert vehicle != null;
            Chunk vehicleChunk = vehicle.getChunk();
            if (!vehicleChunk.isLoaded()) vehicleChunk.load();
            if (playerChunk != vehicleChunk) {
                vehicle.eject();
                vehicle.remove();
                MessageUtil.log("&3Prevented &r&a" + player.getName() + "&r&3 from entering godmode.");
            }
        }
    }
}
