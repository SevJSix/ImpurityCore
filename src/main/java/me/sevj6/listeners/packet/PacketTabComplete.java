package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.SevHandler;
import me.sevj6.event.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.PacketPlayInTabComplete;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author SevJ6
 */

public class PacketTabComplete implements SevListener, Instance {

    private final Set<Player> players = new HashSet<>();

    Configuration exploit = fileConfig.getExploits();

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInTabComplete && exploit.getBoolean("TabCompletePluginViewingFix.Enabled")) {
            PacketPlayInTabComplete packetPlayInTabComplete = (PacketPlayInTabComplete) event.getPacket();
            String a = packetPlayInTabComplete.a().toLowerCase();
            if (
                    a.startsWith("/bukkit") ||
                            a.startsWith("/ver") ||
                            a.startsWith("/pl") ||
                            a.startsWith("/?") ||
                            a.startsWith("/help") ||
                            a.startsWith("/version") ||
                            a.startsWith("/icanhasbukkit") ||
                            a.startsWith("/minecraft") ||
                            a.startsWith("/spigot") ||
                            a.startsWith("/paper") ||
                            a.startsWith("/bungee") ||
                            a.contains(":")
            ) {
                event.setCancelled(true);
            } else if (a.equalsIgnoreCase("/")) {
                players.add(event.getPlayer());
            } else if (a.startsWith("/") && !a.contains(" ")) {
                event.setCancelled(true);
            }
        }
    }

    @SevHandler
    public void onTabOut(PacketEvent.ServerToClient event) {
        if (event.getPacket() instanceof PacketPlayOutTabComplete && exploit.getBoolean("TabCompletePluginViewingFix.Enabled")) {
            if (!players.contains(event.getPlayer())) return;
            List<String> cmdList = config.getStringList("CommandWhitelist.command-list");
            List<String> modifiedList = new ArrayList<>();
            cmdList.forEach(s -> modifiedList.add("/" + s));
            String[] newArray = modifiedList.toArray(new String[0]);
            PacketPlayOutTabComplete newPacket = new PacketPlayOutTabComplete(newArray);
            event.setPacket(newPacket);
            players.remove(event.getPlayer());
        }
    }
}
