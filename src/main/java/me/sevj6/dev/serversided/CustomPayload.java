package me.sevj6.dev.serversided;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketPlayInCustomPayload;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        for (int x = playerInitialLocation.getBlockX() - PLACE_RANGE; x <= playerInitialLocation.getBlockX() + PLACE_RANGE; x++) {
            for (int y = playerInitialLocation.getBlockY() - PLACE_RANGE; y <= playerInitialLocation.getBlockY() + PLACE_RANGE; y++) {
                for (int z = playerInitialLocation.getBlockZ() - PLACE_RANGE; z <= playerInitialLocation.getBlockZ() + PLACE_RANGE; z++) {
                    Location location = new Location(playerInitialLocation.getWorld(), x, y, z);
                    if (isInvalidPlaceLocation(location)) continue;
                    return new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                }
            }
        }
        return null;
    }

    private boolean isInvalidPlaceLocation(Location location) {
        Location obsidian = location.clone().add(0, 1, 0);
        Location dispenser = obsidian.clone().add(0, 1, 0);
        Location redstone = dispenser.clone().add(0, 1, 0);
        return obsidian.getBlock().getType() != Material.AIR || dispenser.getBlock().getType() != Material.AIR || redstone.getBlock().getType() != Material.AIR
                || !location.getNearbyPlayers(0.5).isEmpty();
    }
}
