package me.sevj6.listeners.packet;

import me.sevj6.Instance;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.TimerUtil;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.PacketPlayInAutoRecipe;

/**
 * @author SevJ6
 */

public class PacketAutoRecipe implements NMSPacketListener, Instance {

    TimerUtil delay = new TimerUtil();
    Configuration exploit = fileConfig.getExploits();

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInAutoRecipe && exploit.getBoolean("AutoRecipeLagFix.Enabled")) {
            if (delay.hasReached(exploit.getLong("AutoRecipeLagFix.delay"))) delay.reset();
            else event.setCancelled(true);
        }
    }
}
