package me.sevj6.dev.serversided;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CustomPayload implements NMSPacketListener {
    private final ServersidedAuto32k serversidedAuto32k = new ServersidedAuto32k();

    private final int PLACE_RANGE = 3;

    public static Location redstonePos(Location dispenserLocation, EnumDirection direction) {
        if (isAir(clone(dispenserLocation, 0, 1, 0))) return clone(dispenserLocation, 0, 1, 0);

        switch (direction) {
            case NORTH:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
            case SOUTH:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                }
                break;
            case WEST:
                if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
            case EAST:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
        }
        return null;
    }

    public static boolean isAir(Location location) {
        return location.getBlock().getType() == Material.AIR;
    }

    public static Location clone(Location location, int x, int y, int z) {
        return location.clone().add(x, y, z);
    }

    public static Block getHopperBlock(Location dispenserLocation, EnumDirection direction) {
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

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInCustomPayload) handlePacket(event);
    }

    protected void handlePacket(PacketEvent.Incoming event) {
        PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket();
        String channel = packet.a();
        if (channel.equals("auto32k")) {
            Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                BlockPosition pos = getAutoPlace32kPos(event.getPlayer());
                serversidedAuto32k.doPlace(event.getPlayer(), pos);
            });
        } else if (channel.equals("manual32k")) {
            Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                PacketDataSerializer serializer = packet.b();
                BlockPosition pos = serializer.e();
                serversidedAuto32k.doPlace(event.getPlayer(), pos);
            });
        }
    }

    private BlockPosition getAutoPlace32kPos(Player player) {
        List<BlockPosition> possible = allValidLocations(player);
        int rand = ThreadLocalRandom.current().nextInt(0, possible.size());
        return possible.get(rand);
    }

    public List<BlockPosition> allValidLocations(Player player) {
        Location playerInitialLocation = player.getLocation();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EnumDirection facing = entityPlayer.getDirection();
        EnumDirection opposite = facing.opposite();
        List<BlockPosition> blockPositions = new ArrayList<>();

        for (int x = playerInitialLocation.getBlockX() - PLACE_RANGE; x <= playerInitialLocation.getBlockX() + PLACE_RANGE; x++) {
            for (int y = playerInitialLocation.getBlockY() - PLACE_RANGE; y <= playerInitialLocation.getBlockY() + PLACE_RANGE; y++) {
                for (int z = playerInitialLocation.getBlockZ() - PLACE_RANGE; z <= playerInitialLocation.getBlockZ() + PLACE_RANGE; z++) {
                    Location location = new Location(playerInitialLocation.getWorld(), x, y, z);
                    if (isValidPlaceLocation(location, opposite)) {
                        blockPositions.add(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    }
                }
            }
        }
        return blockPositions;
    }

    private boolean isValidPlaceLocation(Location location, EnumDirection direction) {
        Location obsidian = location.clone().add(0, 1, 0);
        Location dispenser = obsidian.clone().add(0, 1, 0);
        Location redstone = redstonePos(dispenser, direction);
        Block hopper = getHopperBlock(dispenser, direction);
        Location shulker = hopper.getLocation().clone().add(0, 1, 0);
        return location.getBlock().getType() != Material.AIR
                && obsidian.getBlock().getType() == Material.AIR
                && dispenser.getBlock().getType() == Material.AIR
                && (redstone != null && redstone.getBlock().getType() == Material.AIR)
                && hopper.getType() == Material.AIR
                && shulker.getBlock().getType() == Material.AIR
                && location.getNearbyPlayers(1.5).isEmpty();
    }
}
