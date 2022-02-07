package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Plugins extends Command {
    public Plugins(Impurity plugin) {
        super("impurityplugins", "&4Usage: &c/plugins", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder builder = new StringBuilder();
        List<Plugin> plugins = Arrays.stream(Bukkit.getServer().getPluginManager().getPlugins()).collect(Collectors.toList());
        builder.append("&3Plugins " + "&r(&b").append(plugins.size()).append("&r)&3: ");
        for (int i = 0; i < plugins.size(); i++) {
            if (i == (plugins.size() - 1)) {
                builder.append("&6").append(plugins.get(i).getName()).append("&r");
            } else {
                builder.append("&6").append(plugins.get(i).getName()).append("&r, ");
            }
        }
        sendMessage(sender, builder + "\n" +
                "&3The servers main core plugin is open source and free for use.");

        sendMessage(sender, "\n" + "&b&nhttps://github.com/SevJSix/ImpurityCore&r");
    }
}
