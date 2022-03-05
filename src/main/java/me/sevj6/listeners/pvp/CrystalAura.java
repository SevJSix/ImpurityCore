package me.sevj6.listeners.pvp;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PlayerPlaceCrystalEvent;
import org.bukkit.Bukkit;

public class CrystalAura implements SevListener {

    @SevHandler
    public void onCrystal(PlayerPlaceCrystalEvent event) {
        if (event.getPlayer().isOp()) {
            handleTask(() -> {
                event.addCrystal(true, false);
            });
        } else {
            handleTask(event::explodeCrystal);
        }
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }
}
