package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GodmodePatch implements Listener, Instance {

    private final Setting<Boolean> godmode = Setting.getBoolean("movement.godmode");

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (godmode.getValue()) {
            if (event.getPlayer().isInsideVehicle()) {
                Player player = event.getPlayer();
                Vehicle vehicle = (Vehicle) player.getVehicle();
                if (vehicle != null) {
                    Chunk playerChunk = player.getChunk();
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
    }
}
