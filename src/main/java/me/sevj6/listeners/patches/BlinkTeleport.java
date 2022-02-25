package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;

public class BlinkTeleport implements NMSPacketListener {

    public static final HashMap<Player, Location> lastPacketLocationMap;

    static {
        lastPacketLocationMap = new HashMap<>();
    }

    private Field hasPos;
    private Field packetX;
    private Field packetY;
    private Field packetZ;

    public BlinkTeleport() {
        try {
            this.hasPos = PacketPlayInFlying.class.getDeclaredField("hasPos");
            this.hasPos.setAccessible(true);
            this.packetX = PacketPlayInFlying.class.getDeclaredField("x");
            this.packetX.setAccessible(true);
            this.packetY = PacketPlayInFlying.class.getDeclaredField("y");
            this.packetY.setAccessible(true);
            this.packetZ = PacketPlayInFlying.class.getDeclaredField("z");
            this.packetZ.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @NMSEventHandler
    public void onMove(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInFlying) {
            PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
            try {
                if (hasPos.getBoolean(packet)) {
                    Player player = event.getPlayer();
                    EntityPlayer ep = getEntityPlayer(player);
                    Location packetLocation = new Location(player.getWorld(), packetX.getDouble(packet), packetY.getDouble(packet), packetZ.getDouble(packet), player.getLocation().getYaw(), player.getLocation().getPitch());
                    boolean wasCancelled = false;
                    if (BlinkTeleport.lastPacketLocationMap.containsKey(player)) {
                        if (packetLocation.distance(BlinkTeleport.lastPacketLocationMap.get(player)) > 5 && (!ep.onGround) ? ep.fallDistance < 12 : ep.onGround) {
                            event.setCancelled(true);
                            player.teleport(BlinkTeleport.lastPacketLocationMap.get(player));
                            BlinkTeleport.lastPacketLocationMap.remove(player);
                            wasCancelled = true;
                        }
                    }
                    if (!wasCancelled) {
                        put(player, packetLocation, BlinkTeleport.lastPacketLocationMap.containsKey(player));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public void put(Player player, Location location, boolean replace) {
        if (replace) {
            BlinkTeleport.lastPacketLocationMap.replace(player, location);
        } else {
            BlinkTeleport.lastPacketLocationMap.put(player, location);
        }
        Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
            BlinkTeleport.lastPacketLocationMap.remove(player);
        }, (20L * 12L));
    }
}
