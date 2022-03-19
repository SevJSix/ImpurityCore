package me.sevj6.listener.pvp;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PlayerPlaceCrystalEvent;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Bukkit;

public class CrystalAura implements SevListener {

    private final Setting<Boolean> doBreak = Setting.getBoolean("Utility.pvp.crystal_aura.break_crystal");

    @SevHandler
    public void onCrystal(PlayerPlaceCrystalEvent event) {
        if (doBreak.getValue()) {
            if (event.getPlayer().isOp()) {
                handleTask(() -> {
                    event.addCrystal(true, false);
                });
            } else {
                handleTask(event::explodeCrystal);
            }
        }
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }
}
