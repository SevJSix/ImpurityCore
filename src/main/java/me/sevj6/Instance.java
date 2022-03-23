package me.sevj6;

import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import java.lang.reflect.Method;
import java.util.HashMap;

public interface Instance {
    Impurity plugin = Impurity.getPlugin();
    ConfigManager fileConfig = ConfigManager.configManager;
    Configuration settings = fileConfig.getSettings();
    Configuration playtimes = fileConfig.getPlaytimes();
    Configuration namecolor = fileConfig.getNamecolor();
    Configuration totempops = fileConfig.getTotempops();
    HashMap<EntityType, Integer> entityMap = new HashMap<>();

    default void sendMsg(Object sender, String message) {
        try {
            Method method = sender.getClass().getDeclaredMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(sender, ChatColor.translateAlternateColorCodes('&', message));
        } catch (Throwable ignored) {
        }
    }
}
