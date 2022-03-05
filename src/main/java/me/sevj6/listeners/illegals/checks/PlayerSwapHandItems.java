package me.sevj6.listeners.illegals.checks;

import me.sevj6.listeners.illegals.wrapper.IllegalWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItems implements Listener {

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        new IllegalWrapper<>(Player.class, player).check();
    }
}
