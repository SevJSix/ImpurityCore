package me.sevj6.event.events;

import me.sevj6.event.bus.Event;
import org.bukkit.entity.Player;

public class TotemPopEvent extends Event {

    private final Player player;

    public TotemPopEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
