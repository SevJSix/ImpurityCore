package me.sevj6.command;

import me.sevj6.Impurity;
import net.minecraft.server.v1_12_R1.PacketPlayOutTabComplete;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

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

    public String getUsage() {
        return usage;
    }

    public void completeTab(Player player, String[] args) {
        ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.writeAndFlush(new PacketPlayOutTabComplete(args));
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
