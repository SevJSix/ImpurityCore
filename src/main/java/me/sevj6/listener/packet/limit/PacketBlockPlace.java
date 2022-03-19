package me.sevj6.listener.packet.limit;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.listener.packet.PacketLimit;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockPlace;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class PacketBlockPlace extends ViolationManager implements SevListener, Instance {

    public PacketBlockPlace() {
        super(PacketLimit.getIncrementation(PacketPlayInBlockPlace.class), PacketLimit.getDecrementation(PacketPlayInBlockPlace.class));
    }

    @SevHandler
    public void onIncoming(PacketEvent.ClientToServer event) {
        Packet<?> packet = event.getPacket();
        if (packet instanceof PacketPlayInBlockPlace) {
            Player player = event.getPlayer();
            increment(player.getUniqueId());
            if (getVLS(player.getUniqueId()) > PacketLimit.getMaxVLS(PacketPlayInBlockPlace.class)) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(player, "Kicked for exeeding place packets or attempted armor crash");
            }
        }
    }
}
