package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot;
import org.bukkit.GameMode;

public class PacketCreativeSlot implements NMSPacketListener, Instance {

    @NMSEventHandler
    public void on(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInSetCreativeSlot) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(event.getPlayer(), "You sent a creative slot packet whilst in survival mode");
            }
        }
    }
}
