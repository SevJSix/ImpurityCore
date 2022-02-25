package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;

public class LongJumpFix implements Listener {

    public static final HashMap<Player, Location> lastLocationMap = new HashMap<>();

    public LongJumpFix() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Impurity.getPlugin(), lastLocationMap::clear, 20L, (20L * 30L));
    }

    public void put(Player player, Location location, boolean replace) {
        if (replace) {
            LongJumpFix.lastLocationMap.replace(player, location);
        } else {
            LongJumpFix.lastLocationMap.put(player, location);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        double dist = event.getFrom().distance(event.getTo());
        Player player = event.getPlayer();
        EntityPlayer ep = ((CraftPlayer) event.getPlayer()).getHandle();
        if (tooFar(dist, ep)) event.setTo(event.getFrom());
    }

    private boolean tooFar(double dist, EntityPlayer ep) {
        boolean og = ep.onGround;
        float fd = ep.fallDistance;
        return dist > 1.0D && og
                || dist > 1.0D && !og && fd < 4.5F
                || dist > 1.5D && !og && fd < 8.5F
                || dist > 2.0D && !og && fd < 12.5F
                || dist > 3.0D && !og && fd < 20.0F;
    }
}