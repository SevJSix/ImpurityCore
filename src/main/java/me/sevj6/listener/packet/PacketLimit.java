package me.sevj6.listener.packet;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;

import java.util.UUID;

public class PacketLimit extends ViolationManager implements SevListener, Instance {

    private final int MAX_VLS = PacketUtil.getMaxVls();

    public PacketLimit() {
        super(PacketUtil.getIncrementation(), PacketUtil.getDecrementation());
    }

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        UUID uuid = event.getPlayer().getUniqueId();
        increment(uuid);
        if (getVLS(uuid) > MAX_VLS) {
            remove(uuid);
            PlayerUtil.kickPlayerAsync(event.getPlayer(), PacketUtil.getKickMessage());
        }
    }
}
