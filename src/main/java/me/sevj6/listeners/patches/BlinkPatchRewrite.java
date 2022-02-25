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
import org.bukkit.util.NumberConversions;

import java.lang.reflect.Field;

public class BlinkPatchRewrite implements NMSPacketListener {

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
                    double dist = getDistanceOn2DPlaneAxis(player.getLocation(), packetLocation);
                        if (tooFar(dist, ep)) {
                            event.setCancelled(true);
                            handleTask(() -> player.teleport(player.getLocation()));
                        }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tooFar(double dist, EntityPlayer ep) {
        boolean og = ep.onGround;
        float fd = ep.fallDistance;
        if (dist > 30 || ep.getBukkitEntity().isGliding()) return false;
        return dist > 1.5D && og
                || dist > 2.0D && !og && fd < 4.5F
                || dist > 2.5D && !og && fd < 8.5F
                || dist > 3.0D && !og && fd < 12.5F
                || dist > 3.5D && !og && fd < 20.0F;
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }

    public double getDistanceOn2DPlaneAxis(Location l1, Location l2) {
        double x1 = l1.getX();
        double x2 = l2.getX();
        double z1 = l1.getZ();
        double z2 = l2.getZ();
        double dSquared = NumberConversions.square((x2 - x1)) + NumberConversions.square((z2 - z1));
        return Math.sqrt(dSquared);
    }

    private EntityPlayer getEntityPlayer(Player player) {
        return ((CraftPlayer) player).getHandle();
    }
}
