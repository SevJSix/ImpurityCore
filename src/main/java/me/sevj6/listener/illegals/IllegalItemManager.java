package me.sevj6.listener.illegals;

import me.sevj6.Impurity;
import me.sevj6.listener.illegals.check.checks.*;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class IllegalItemManager {

    public static List<Listener> illegalListeners = new ArrayList<>();
    public static Impurity plugin = Impurity.getPlugin();

    public static void init() {
        registerIllegalCheck(new BlockPlace());
        registerIllegalCheck(new EntityEnterPortal());
        registerIllegalCheck(new EntityPickupItem());
        registerIllegalCheck(new InventoryClick());
        registerIllegalCheck(new InventoryClose());
        registerIllegalCheck(new InventoryOpen());
        registerIllegalCheck(new PlayerDropItem());
        registerIllegalCheck(new PlayerPickupItem());
        registerIllegalCheck(new PlayerSwapHandItems());
        registerIllegalCheck(new BlockDispense());
        IllegalItemManager.illegalListeners.forEach(listener -> plugin.getServer().getPluginManager().registerEvents(listener, plugin));
    }

    public static void registerIllegalCheck(Listener listener) {
        IllegalItemManager.illegalListeners.add(listener);
    }
}
