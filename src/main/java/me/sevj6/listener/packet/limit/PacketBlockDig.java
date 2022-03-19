package me.sevj6.listener.packet.limit;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.listener.packet.PacketLimit;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class PacketBlockDig extends ViolationManager implements SevListener, Instance {

    public PacketBlockDig() {
        super(PacketLimit.getIncrementation(PacketPlayInBlockDig.class), PacketLimit.getDecrementation(PacketPlayInBlockDig.class));
    }

    @SevHandler
    public void onIncoming(PacketEvent.ClientToServer event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof PacketPlayInBlockDig) {
            Player player = event.getPlayer();
            PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig) packet;
            PacketPlayInBlockDig.EnumPlayerDigType enumPlayerDigType = packetPlayInBlockDig.c();
            if (enumPlayerDigType.name().equalsIgnoreCase("SWAP_HELD_ITEMS")) {
                increment(player.getUniqueId());
                if (getVLS(player.getUniqueId()) > PacketLimit.getMaxVLS(PacketPlayInBlockDig.class)) {
                    event.setCancelled(true);
                    PlayerUtil.kickPlayerAsync(player, "Kicked for attempted offhand crash");
                }
            }
        }
    }
}
