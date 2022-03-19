package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Help extends Command implements Instance {
    private static final String line = ChatColor.translateAlternateColorCodes('&', config.getString("HelpCommand.lines"));

    public Help(Impurity plugin) {
        super("help", "&4Usage: &c/help", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        List<String> help = config.getStringList("HelpCommand.help-message");
        player.sendMessage(line);
        help.forEach(s -> {
            String cmd = s.split(" ")[0];
            cmd = cmd.substring(cmd.indexOf("/"));
            MessageUtil.sendClickableMessage(player, s, config.getString("HelpCommand.hover-text").replace("%cmd%", cmd), cmd, ClickEvent.Action.SUGGEST_COMMAND);
        });
        player.sendMessage(line);
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
