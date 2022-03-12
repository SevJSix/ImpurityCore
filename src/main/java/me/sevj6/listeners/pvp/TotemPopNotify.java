package me.sevj6.listeners.pvp;

import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.TotemPopEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class TotemPopNotify implements SevListener, Listener {

    private final HashMap<Player, Integer> pops = new HashMap<>();

    @SevHandler
    public void onPop(TotemPopEvent event) {
        Player player = event.getLarper();
        if (pops.containsKey(player)) {
            pops.replace(player, pops.get(player) + 1);
            player.sendActionBar(ChatColor.RED + "You popped " + ChatColor.GRAY + pops.get(player) + ChatColor.RED + " totems");
        } else {
            pops.put(player, 1);
            player.sendActionBar(ChatColor.RED + "You popped a totem!");
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        pops.remove(event.getEntity());
    }
}
