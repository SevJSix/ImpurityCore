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

public class BlinkPatchRewrite implements NMSPacketListener {

    public static final HashMap<Player, Location> lastPacketLocationMap;

    static {
        lastPacketLocationMap = new HashMap<>();
    }

    private Field hasPos;
    private Field packetX;
    private Field packetY;
    private Field packetZ;

    public BlinkPatchRewrite() {
        try {
            this.hasPos = PacketPlayInFlying.class.getDeclaredField("hasPos");
            this.hasPos.setAccessible(true);
            this.packetX = PacketPlayInFlying.class.getDeclaredField("x");
            this.packetX.setAccessible(true);
            this.packetY = PacketPlayInFlying.class.getDeclaredField("y");
            this.packetY.setAccessible(true);
            this.packetZ = PacketPlayInFlying.class.getDeclaredField("z");
            this.packetZ.setAccessible(true);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Impurity.getPlugin(), lastPacketLocationMap::clear, 20L, (20L * 30L));
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
                    Location packetLocation = new Location(player.getWorld(), packetX.getDouble(packet), packetY.getDouble(packet), packetZ.getDouble(packet), ep.yaw, ep.pitch);
                    if (BlinkPatchRewrite.lastPacketLocationMap.containsKey(player)) {
                        double dist = BlinkPatchRewrite.lastPacketLocationMap.get(player).distance(packetLocation);
                        if (tooFar(dist, ep)) {
                            handleTask(() -> player.teleport(BlinkPatchRewrite.lastPacketLocationMap.get(player)));
                        }
                    }
                    put(player, packetLocation, BlinkPatchRewrite.lastPacketLocationMap.containsKey(player));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tooFar(double dist, EntityPlayer ep) {
        boolean og = ep.onGround;
        float fd = ep.fallDistance;
        if (dist > 30) return false;
        return dist > 1.0D && og
                || dist > 1.0D && !og && fd < 4.5F
                || dist > 1.5D && !og && fd < 8.5F
                || dist > 2.0D && !og && fd < 12.5F
                || dist > 3.0D && !og && fd < 20.0F;
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }

    private EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }

    public void put(Player player, Location location, boolean replace) {
        if (replace) {
            BlinkPatchRewrite.lastPacketLocationMap.replace(player, location);
        } else {
            BlinkPatchRewrite.lastPacketLocationMap.put(player, location);
        }
    }
}
