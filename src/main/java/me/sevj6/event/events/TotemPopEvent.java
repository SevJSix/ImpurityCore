package me.sevj6.event.events;

import me.sevj6.event.bus.Event;
import org.bukkit.entity.Player;

public class TotemPopEvent extends Event {

    private final Player larper;

    public TotemPopEvent(Player larper) {
        this.larper = larper;
    }

    public Player getLarper() {
        return larper;
    }
}
