package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.SevHandler;
import me.sevj6.event.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import net.minecraft.server.v1_12_R1.PacketPlayInSetCreativeSlot;
import org.bukkit.GameMode;

public class PacketCreativeSlot implements SevListener, Instance {

    @SevHandler
    public void on(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInSetCreativeSlot) {
            if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(event.getPlayer(), "You sent a creative slot packet whilst in survival mode");
            }
        }
    }
}
