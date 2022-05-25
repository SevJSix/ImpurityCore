package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.PlayerUtil;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class InvalidSlotClick implements PacketListener, Instance {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        Packet<?> packet = event.getPacket();
        if (packet instanceof PacketPlayInWindowClick) {
            Player player = event.getPlayer();
            PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick) packet;
            int slot = packetPlayInWindowClick.b();
            if ((player.getOpenInventory() == null && slot > player.getInventory().getSize()) || slot > player.getOpenInventory().countSlots()) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(player, "Invalid inventory slot clicked [" + "SlotID: " + slot + "]");
            }
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing outgoing) throws Throwable {

    }
}
