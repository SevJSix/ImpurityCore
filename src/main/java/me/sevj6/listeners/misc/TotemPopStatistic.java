package me.sevj6.listeners.misc;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.TotemPopEvent;
import me.sevj6.util.fileutil.Configuration;

import java.util.UUID;

public class TotemPopStatistic implements NMSPacketListener {

    public static Configuration config;

    static {
        TotemPopStatistic.config = new Configuration("totempops.yml", Impurity.getPlugin());
    }

    @NMSEventHandler
    public void onPop(TotemPopEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (config.contains(uuid.toString())) {
            config.set(uuid.toString(), (config.getLong(uuid.toString()) + 1L));
        } else {
            config.set(uuid.toString(), 1L);
        }
        config.saveConfig();
        config.reloadConfig();
    }
}
