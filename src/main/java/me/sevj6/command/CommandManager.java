package me.sevj6.command;

import me.sevj6.Impurity;
import me.sevj6.command.commands.*;
import me.sevj6.listener.Manager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends Manager {

    public static List<CommandExecutor> commands = new ArrayList<>();

    public CommandManager(Impurity plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        registerCommand("serverinfo", new ServerInfoCommand());
        registerCommand("bed", new BedCommand());
        registerCommand("copyinventory", new CopyInventoryCommand());
        registerCommand("discord", new DiscordCommand());
        registerCommand("help", new HelpCommand());
        registerCommand("joindate", new JoindateCommand());
        registerCommand("namecolor", new NameColorCommand());
        registerCommand("openinventory", new OpenInventoryCommand());
        registerCommand("ping", new PingCommand());
        registerCommand("playerstats", new PlayerStatisticsCommand());
        registerCommand("regear", new RegearCommand());
        registerCommand("playtime", new PlaytimeCommand());
        registerCommand("toptimes", new TopPlaytimesCommand());
        registerCommand("troll", new TrollCommand());
    }

    private void registerCommand(String name, CommandExecutor command) {
        commands.add(command);
        plugin.getCommand(name).setExecutor(command);
        if (command instanceof TabExecutor) {
            plugin.getCommand(name).setTabCompleter((TabCompleter) command);
        }
    }
}
