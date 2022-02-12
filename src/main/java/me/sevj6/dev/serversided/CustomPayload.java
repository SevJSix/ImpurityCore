package me.sevj6.dev.serversided;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumDirection;
import net.minecraft.server.v1_12_R1.PacketPlayInCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class CustomPayload implements NMSPacketListener {
    private final ServersidedAuto32k serversidedAuto32k = new ServersidedAuto32k();

    private final int PLACE_RANGE = 3;

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInCustomPayload) {
            PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket();
            if (packet.a().equals("auto32k")) {
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                    BlockPosition pos = getAutoPlace32kPos(event.getPlayer());
                    serversidedAuto32k.doPlace(event.getPlayer(), pos);
                });
            }
        }
    }

    private BlockPosition getAutoPlace32kPos(Player player) {
        Location playerInitialLocation = player.getLocation();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EnumDirection facing = entityPlayer.getDirection();
        EnumDirection opposite = facing.opposite();
        for (int x = playerInitialLocation.getBlockX() - PLACE_RANGE; x <= playerInitialLocation.getBlockX() + PLACE_RANGE; x++) {
            for (int y = playerInitialLocation.getBlockY() - PLACE_RANGE; y <= playerInitialLocation.getBlockY() + PLACE_RANGE; y++) {
                for (int z = playerInitialLocation.getBlockZ() - PLACE_RANGE; z <= playerInitialLocation.getBlockZ() + PLACE_RANGE; z++) {
                    Location location = new Location(playerInitialLocation.getWorld(), x, y, z);
                    if (isValidPlaceLocation(location, opposite)) {
                        return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    }
                }
            }
        }
        return null;
    }

    private boolean isValidPlaceLocation(Location location, EnumDirection direction) {
        Location obsidian = location.clone().add(0, 1, 0);
        Location dispenser = obsidian.clone().add(0, 1, 0);
        Location redstone = dispenser.clone().add(0, 1, 0);
        Block hopper = getHopperBlock(dispenser, direction);
        return location.getBlock().getType() != Material.AIR
                && obsidian.getBlock().getType() == Material.AIR
                && dispenser.getBlock().getType() == Material.AIR
                && redstone.getBlock().getType() == Material.AIR
                && hopper.getType() == Material.AIR
                && location.getNearbyPlayers(1.5).isEmpty();
    }

    private Block getHopperBlock(Location dispenserLocation, EnumDirection direction) {
        switch (direction) {
            case EAST:
                return dispenserLocation.getWorld().getBlockAt(dispenserLocation.clone().add(1, -1, 0));
            case WEST:
                return dispenserLocation.getWorld().getBlockAt(dispenserLocation.clone().add(-1, -1, 0));
            case NORTH:
                return dispenserLocation.getWorld().getBlockAt(dispenserLocation.clone().add(0, -1, -1));
            case SOUTH:
                return dispenserLocation.getWorld().getBlockAt(dispenserLocation.clone().add(0, -1, 1));
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }
}
