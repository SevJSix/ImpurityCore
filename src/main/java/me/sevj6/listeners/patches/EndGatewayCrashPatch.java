package me.sevj6.listeners.patches;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

/**
 * @author 254n_m
 * Credit L2X9CoreRewrite
 * slightly modified by SevJ6
 */

public class EndGatewayCrashPatch implements Listener, Instance {
    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        if (config.getBoolean("Exploits.end-gateway-crash-fix")) {
            Vehicle vehicle = event.getVehicle();
            if (!(vehicle.getLocation().getWorld().getEnvironment() == World.Environment.THE_END)) return;
            if (isNearGateway(vehicle) && vehicle.getPassengers().isEmpty()) {
                vehicle.remove();
                MessageUtil.log("&3Prevented an entity in " + Utils.formatLocation(vehicle.getLocation()) + "&r&3 from going through a gateway");
            } else if (isNearGateway(vehicle)) {
                vehicle.getPassengers().forEach(entity -> {
                    if (entity instanceof Player) {
                        Player player = (Player) entity;
                        vehicle.remove();
                        player.kickPlayer(config.getString("Exploits.gateway-crash-kick-msg"));
                        MessageUtil.log("&3Prevented an entity in " + Utils.formatLocation(vehicle.getLocation()) + "&r&3 from going through a gateway. Attempt by&r&a " + player.getName());
                    }
                });
            }
        }
    }

    private boolean isNearGateway(Vehicle vehicle) {
        Location location = vehicle.getLocation();
        for (BlockFace face : BlockFace.values()) {
            if (nearGateWay(face, 1, location) ||
                    nearGateWay(face, 2, location) ||
                    nearGateWay(face, 3, location)) {
                return true;
            }
        }
        return false;
    }

    private boolean nearGateWay(BlockFace face, int dist, Location location) {
        return location.getWorld().getBlockAt(location).getRelative(face, dist).getType() == Material.END_GATEWAY;
    }
}
