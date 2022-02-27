package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.SevHandler;
import me.sevj6.event.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInBlockDig;
import org.bukkit.entity.Player;

/**
 * @author SevJ6
 */

public class PacketBlockDig extends ViolationManager implements SevListener, Instance {
    Configuration exploit = fileConfig.getExploits();

    public PacketBlockDig() {
        super(fileConfig.getExploits().getInt("BlockDig.incrementVLS"), fileConfig.getExploits().getInt("BlockDig.decrementVLS"));
    }

    @SevHandler
    public void onIncoming(PacketEvent.Incoming event) {
        Packet<?> packet = event.getPacket();
        Player player = event.getPlayer();
        if (exploit.getBoolean("Packets.Enabled") && packet instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packetPlayInBlockDig = (PacketPlayInBlockDig) packet;
            PacketPlayInBlockDig.EnumPlayerDigType enumPlayerDigType = packetPlayInBlockDig.c();
            if (enumPlayerDigType.name().equalsIgnoreCase("SWAP_HELD_ITEMS")) {
                increment(player.getUniqueId());
                if (getVLS(player.getUniqueId()) > exploit.getInt("BlockDig.maxVLS")) {
                    event.setCancelled(true);
                    PlayerUtil.kickPlayerAsync(player, "Kicked for attempted offhand crash");
                }
            }
        }
    }
}
