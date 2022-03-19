package me.sevj6.listener.pvp;

import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.AsyncDeathEvent;
import me.sevj6.event.events.TotemPopEvent;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TotemPopNotify extends LarperPops implements SevListener {

    private final Setting<Boolean> doNotify = Setting.getBoolean("totem_pop_notify.enabled");
    private final Setting<Boolean> doBroadcast = Setting.getBoolean("totem_pop_notfiy.broadcast_on_death");

    @SevHandler
    public void onPop(TotemPopEvent event) {
        if (doNotify.getValue()) {
            Player player = event.getLarper();
            incrementPops(player);
            player.sendActionBar(ChatColor.RED + "You popped " + ChatColor.GRAY + getTotemPops(player) + ChatColor.RED + " totems");
        }
    }

    @SevHandler
    public void onDeath(AsyncDeathEvent event) {
        if (doBroadcast.getValue()) {
            Player player = event.getPlayer();
            if (hasPlayerPopped(player)) {
                Bukkit.broadcastMessage(ChatColor.DARK_AQUA + player.getName() + ChatColor.DARK_RED + " died after popping " + ChatColor.GOLD + getTotemPops(player) + ChatColor.DARK_RED + " totems");
                resetPops(player);
            }
        }
    }
}
