package me.sevj6.event.events;

import me.sevj6.event.bus.Event;
import org.bukkit.entity.Player;

public class AsyncDeathEvent extends Event {

    private final Player player;

    public AsyncDeathEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
