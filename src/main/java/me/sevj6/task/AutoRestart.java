package me.sevj6.task;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.task.scheduler.ScheduledTask;
import me.sevj6.task.scheduler.TaskForce;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.TimerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class AutoRestart implements TaskForce, Instance {

    TimerUtil time = new TimerUtil();

    private void broadCast(String msg) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', MessageUtil.getPrefix() + msg));
    }

    @ScheduledTask
    public void restart() {
        long millis = (System.currentTimeMillis() - Impurity.startTime);
        if (millis < 43200000) return;
        Thread t = new Thread(() -> {
            broadCast("&eServer restarting in 1 minute...");
            time.delay(30000);
            broadCast("&eServer restarting in 30 seconds...");
            time.delay(25000);
            broadCast("&eServer restarting in 5 seconds...");
            time.delay(1000);
            broadCast("&eServer restarting in 4 seconds...");
            time.delay(1000);
            broadCast("&eServer restarting in 3 seconds...");
            time.delay(1000);
            broadCast("&eServer restarting in 2 seconds...");
            time.delay(1000);
            broadCast("&eServer restarting in 1 seconds...");
            time.delay(1000);
            if (Bukkit.getOnlinePlayers().size() > 0) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(ChatColor.GOLD + "Server restarting... rejoin soon"));
                });
            }
            time.delay(500);
            Bukkit.shutdown();
        });
        t.start();
    }
}
