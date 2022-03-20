package me.sevj6.listener.misc;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.TotemPopEvent;

import java.util.UUID;

public class TotemPopStatistic implements SevListener, Instance {

    @SevHandler
    public void onPop(TotemPopEvent event) {
        UUID uuid = event.getLarper().getUniqueId();
        if (totempops.contains(uuid.toString())) {
            totempops.write(uuid.toString(), (totempops.getLong(uuid.toString()) + 1L));
        } else {
            totempops.write(uuid.toString(), 1L);
        }
    }
}
