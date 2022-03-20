package me.sevj6.listener.illegals.check.checks;

import me.sevj6.listener.illegals.wrapper.IllegalWrapper;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItems implements Listener {

    private final Setting<Boolean> enabled = Setting.getBoolean("events.PlayerSwapHandItems");

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent event) {
        if (enabled.getValue()) {
            Player player = event.getPlayer();
            new IllegalWrapper<>(Player.class, player).check();
        }
    }
}
