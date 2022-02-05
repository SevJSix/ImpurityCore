package me.sevj6.util;

import me.sevj6.Impurity;
import me.sevj6.listeners.meta.MetaManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MessageUtil extends Utils {
    public static String getPrefix() {
        return PluginUtil.config().getString("Plugin.plugin-prefix");
    }

    public static String getDiscord() {
        return PluginUtil.config().getString("Plugin.discord-link");
    }

    public static void log(String message) {
        System.out.println(ChatColor.translateAlternateColorCodes('&', getPrefix() + message));
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                sendMessage(player, message);
            }
        }
    }

    public static void sendMessage(Object sender, String message) {
        try {
            Method method = sender.getClass().getDeclaredMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(sender, ChatColor.translateAlternateColorCodes('&', message));
        } catch (Throwable t) {
            //Obj cant have messages sent to them
        }
    }

    /**
     * JSON hover text message method
     *
     * @author 254n_m
     */
    public static void sendClickableMessage(Player player, String message, String hoverText, String cmd, ClickEvent.Action action) {
        TextComponent msg = new TextComponent(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message));
        msg.setClickEvent(new ClickEvent(action, cmd));
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', hoverText))
                        .create()));
        player.spigot().sendMessage(msg);
    }

    private static String format(double tps) {
        return (tps > 18.0D ? "§a" : (tps > 16.0D ? "§e" : "§c")) + (tps > 20.0D ? "" : "") + String.format("%.2f", Math.min((double) Math.round(tps * 100.0D) / 100.0D, 20.0D));
    }

    public static String getRealTps() {
        return format(Bukkit.getServer().getTPS()[0]);
    }

    public static String parseTab(String text, Player player) {
        return ChatColor.translateAlternateColorCodes('&', text.
                replace("%tps%", getRealTps()).
                replace("%players%", String.valueOf(Bukkit.getOnlinePlayers().size())).
                replace("%ping%", String.valueOf(PlayerUtil.getPlayerPing(player))).
                replace("%uptime%", getFormattedInterval(System.currentTimeMillis() - Impurity.startTime))).
                replace("%meta%", MetaManager.getTablistPlaceholder()).
                replace("%age%", Objects.requireNonNull(getServerAge()));
    }

    public static long getSinceLaunch() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime launch = LocalDateTime.of(2021, 12, 22, 6, 30);
        return ChronoUnit.SECONDS.between(launch, now);
    }

    public static String getServerAge() {
        long seconds = getSinceLaunch();
        return calculateTime(seconds);
    }

    public static String calculateTime(long seconds) {
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        return day + " Days Old";
    }
}
