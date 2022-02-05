package me.sevj6.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerUtil extends Utils {

    public static int getPlayerPing(Player player) {
        try {
            String a = Bukkit.getServer().getClass().getPackage().getName().substring(23);
            Class<?> b = Class.forName("org.bukkit.craftbukkit." + a + ".entity.CraftPlayer");
            Object c = b.getMethod("getHandle").invoke(player);
            int d = (Integer) c.getClass().getDeclaredField("ping").get(c);
            if (!plugin.getConfig().getBoolean("disableNegative"))
                d = Math.max(d, 0);
            return d;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getJoinDate(Object object) {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String jd = null;
        if (object instanceof Player) {
            Player player = (Player) object;
            Date date = new Date(player.getFirstPlayed());
            jd = dateFormat.format(date);
        } else if (object instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) object;
            Date date = new Date(player.getFirstPlayed());
            jd = dateFormat.format(date);
        }
        return jd;
    }

    public static void kickPlayerAsync(Player player, String reason) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.kickPlayer(ChatColor.translateAlternateColorCodes('&', reason));
        });
    }
}
