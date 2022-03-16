package me.sevj6.listener.packet;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.TimerUtil;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.PacketPlayInAutoRecipe;

/**
 * @author SevJ6
 */

public class PacketAutoRecipe implements SevListener, Instance {

    TimerUtil delay = new TimerUtil();
    Configuration exploit = fileConfig.getExploits();

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInAutoRecipe && exploit.getBoolean("AutoRecipeLagFix.Enabled")) {
            if (delay.hasReached(exploit.getLong("AutoRecipeLagFix.delay"))) delay.reset();
            else event.setCancelled(true);
        }
    }
}
