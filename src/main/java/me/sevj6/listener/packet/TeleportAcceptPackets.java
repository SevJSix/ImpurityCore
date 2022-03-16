package me.sevj6.listener.packet;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.ViolationManager;
import net.minecraft.server.v1_12_R1.PacketPlayInTeleportAccept;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class TeleportAcceptPackets extends ViolationManager implements SevListener {

    public TeleportAcceptPackets() {
        super(1, 10);
    }

    @SevHandler
    public void on(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInTeleportAccept) {
            increment(event.getPlayer().getUniqueId());
            if (getVLS(event.getPlayer().getUniqueId()) > 30) {
                Location location = event.getPlayer().getLocation();
                event.setCancelled(true);
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                    event.getPlayer().teleport(location);
                });
            }
        }
    }
}
