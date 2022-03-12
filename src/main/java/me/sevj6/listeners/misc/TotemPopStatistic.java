package me.sevj6.listeners.misc;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.TotemPopEvent;
import me.sevj6.util.fileutil.Configuration;

import java.util.UUID;

public class TotemPopStatistic implements SevListener {

    public static Configuration config;

    static {
        TotemPopStatistic.config = new Configuration("totempops.yml", Impurity.getPlugin());
    }

    @SevHandler
    public void onPop(TotemPopEvent event) {
        UUID uuid = event.getLarper().getUniqueId();
        if (config.contains(uuid.toString())) {
            config.write(uuid.toString(), (config.getLong(uuid.toString()) + 1L));
        } else {
            config.write(uuid.toString(), 1L);
        }
    }
}
