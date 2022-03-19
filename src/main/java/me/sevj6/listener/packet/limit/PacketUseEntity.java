package me.sevj6.listener.packet.limit;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.listener.packet.PacketLimit;
import me.sevj6.util.ViolationManager;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketUseEntity extends ViolationManager implements SevListener, Instance {
    public PacketUseEntity() {
        super(PacketLimit.getIncrementation(PacketPlayInUseEntity.class), PacketLimit.getDecrementation(PacketPlayInUseEntity.class));
    }

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
            if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                Player player = event.getPlayer();
                increment(player.getUniqueId());
                if (getVLS(player.getUniqueId()) > PacketLimit.getMaxVLS(PacketPlayInUseEntity.class)) {
                    remove(player.getUniqueId());
                    event.setCancelled(true);
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        event.getPlayer().kickPlayer("Too many attack packets sent: (" + getVLS(event.getPlayer().getUniqueId()) + " Violations)." + "\n" + "Try lowering your aura speed");
                    });
                }
            }
        }
    }
}
