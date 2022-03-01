package me.sevj6.command;

import me.sevj6.Impurity;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Method;

public abstract class Command {
    protected final Impurity plugin;
    private final String name;
    private final String usage;

    public Command(String name, String usage, Impurity plugin) {
        this.name = name;
        this.usage = usage;
        this.plugin = plugin;
    }

    public abstract void execute(CommandSender sender, String[] args);

    public abstract String[] onTabComplete();

    public String getName() {
        return name;
    }

    protected void sendMessage(Object sender, String message) {
        try {
            Method method = sender.getClass().getDeclaredMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(sender, ChatColor.translateAlternateColorCodes('&', message));
        } catch (Throwable ignored) {
        }
    }
}
