package me.sevj6.command;

import me.sevj6.Impurity;
import me.sevj6.command.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler {
    private final List<Command> commands;
    private final Impurity plugin;

    public CommandHandler(Impurity plugin) {
        commands = new ArrayList<>();
        this.plugin = plugin;
        registerCommand(new Ping(plugin));
        registerCommand(new JoinDate(plugin));
        registerCommand(new Help(plugin));
        registerCommand(new Discord(plugin));
        registerCommand(new CopyInventory(plugin));
        registerCommand(new Bed(plugin));
        registerCommand(new NameColor(plugin));
        registerCommand(new About(plugin));
        registerCommand(new TopPlaytimes(plugin));
        registerCommand(new Playtime(plugin));
    }

    public void registerCommand(Command... commands) {
        for (Command command : commands) {
            Bukkit.getServer().getCommandMap().register(command.getName(), new org.bukkit.command.Command(command.getName()) {
                @Override
                public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                    command.execute(sender, args);
                    return true;
                }
            });
            this.commands.add(command);
        }
    }

    public List<Command> getCommands() {
        return commands;
    }
}
