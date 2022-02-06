package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.Utils;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlinkPatch implements NMSPacketListener {

    private final HashMap<Player, Location> previousLocationMap;
    private Field xF;
    private Field yF;
    private Field zF;

    public BlinkPatch() {
        previousLocationMap = new HashMap<>();
        try {
            xF = PacketPlayInFlying.class.getDeclaredField("x");
            xF.setAccessible(true);
            yF = PacketPlayInFlying.class.getDeclaredField("y");
            yF.setAccessible(true);
            zF = PacketPlayInFlying.class.getDeclaredField("z");
            zF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInFlying) {
            PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
            try {
                double x = (double) xF.get(packet);
                double y = (double) yF.get(packet);
                double z = (double) zF.get(packet);
                Player player = event.getPlayer();
                Location location = player.getLocation();
                if (!previousLocationMap.containsKey(player)) previousLocationMap.put(player, location);
                Location packetLocation = new Location(player.getWorld(), x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
                double distance = location.distance(packetLocation);
                AtomicBoolean teleported = new AtomicBoolean(false);
                if (distance > 7 && Utils.getRawTps() > 15 && !(distance > 30) && !teleported.get()) {
                    event.setCancelled(true);
                    teleportAsync(player, location);
                    previousLocationMap.remove(player);
                    teleported.set(true);
                    Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                        teleported.set(false);
                    }, 40L);
                }
                previousLocationMap.remove(player);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void teleportAsync(Player player, Location location) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
            player.teleport(location);
        });
    }
}
