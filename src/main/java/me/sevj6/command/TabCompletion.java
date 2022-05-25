package me.sevj6.command;

import me.sevj6.Instance;
import me.sevj6.util.Utils;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class TabCompletion implements PacketListener, Instance {

    private final ConcurrentHashMap<Player, String[]> completeMap = new ConcurrentHashMap<>();

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        Packet<?> packet = event.getPacket();
        if (packet instanceof PacketPlayInTabComplete) {
            PacketPlayInTabComplete packetPlayInTabComplete = (PacketPlayInTabComplete) packet;
            String a = packetPlayInTabComplete.a().toLowerCase();
            if (a.equalsIgnoreCase("/")) {
                completeMap.putIfAbsent(event.getPlayer(), Utils.getAllowedCommands());
                return;
            }
            if (a.startsWith("/bukkit") || a.startsWith("/ver") || a.startsWith("/pl") || a.startsWith("/thiss") || a.contains(":")
                    || a.startsWith("/?") || a.startsWith("/help") || a.startsWith("/version") || a.startsWith("/icanhasbukkit") || a.startsWith("/minecraft")
                    || a.startsWith("/spigot") || a.startsWith("/paper") || a.startsWith("/bungee") || !a.contains(" ")) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        if (event.getPacket() instanceof PacketPlayOutTabComplete) {
            if (completeMap.containsKey(event.getPlayer())) {
                event.setPacket(new PacketPlayOutTabComplete(completeMap.get(event.getPlayer())));
                completeMap.remove(event.getPlayer());
            }
        }
    }
}
