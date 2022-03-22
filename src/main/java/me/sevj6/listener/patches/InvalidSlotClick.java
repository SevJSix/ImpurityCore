package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class InvalidSlotClick implements SevListener, Instance {

    @SevHandler
    public void onIncoming(PacketEvent.ClientToServer event) {
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
}
