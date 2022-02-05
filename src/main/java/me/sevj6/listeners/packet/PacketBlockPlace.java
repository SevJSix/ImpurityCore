package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockPlace;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class PacketBlockPlace extends ViolationManager implements NMSPacketListener, Instance {
    Configuration exploit = fileConfig.getExploits();

    public PacketBlockPlace() {
        super(fileConfig.getExploits().getInt("BlockPlace.incrementVLS"), fileConfig.getExploits().getInt("BlockPlace.decrementVLS"));
    }

    @NMSEventHandler
    public void onIncoming(PacketEvent.Incoming event) {
        Packet<?> packet = event.getPacket();
        if (exploit.getBoolean("Packets.Enabled") && packet instanceof PacketPlayInBlockPlace) {
            Player player = event.getPlayer();
            PacketPlayInBlockPlace packetPlayInBlockPlace = (PacketPlayInBlockPlace) packet;
            increment(player.getUniqueId());
            if (getVLS(player.getUniqueId()) > exploit.getInt("BlockPlace.maxVLS")) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(player, "Kicked for exeeding place packets or attempted armor crash");
            }
        }
    }
}
