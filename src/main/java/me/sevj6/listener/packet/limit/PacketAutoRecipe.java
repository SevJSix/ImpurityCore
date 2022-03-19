package me.sevj6.listener.packet.limit;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.listener.packet.PacketLimit;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import net.minecraft.server.v1_12_R1.PacketPlayInAutoRecipe;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class PacketAutoRecipe extends ViolationManager implements SevListener, Instance {

    public PacketAutoRecipe() {
        super(PacketLimit.getIncrementation(PacketPlayInAutoRecipe.class), PacketLimit.getDecrementation(PacketPlayInAutoRecipe.class));
    }

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInAutoRecipe) {
            Player player = event.getPlayer();
            increment(player.getUniqueId());
            if (getVLS(player.getUniqueId()) > PacketLimit.getMaxVLS(PacketPlayInAutoRecipe.class)) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(player, "Too many recipe packets sent");
                remove(player.getUniqueId());
            }
        }
    }
}
