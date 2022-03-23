package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Setting;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HelpCommand implements CommandExecutor {

    private final Setting<String> lines = Setting.getString("help.lines");
    private final Setting<String> hoverText = Setting.getString("help.hover_text");
    private final Setting<List<String>> message = Setting.getStringList("help.help_message");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            List<String> help = message.getValue();
            MessageUtil.sendMessage(player, lines.getValue()); // top of msg
            help.forEach(s -> {
                String cmd = s.split(" ")[0];
                cmd = cmd.substring(cmd.indexOf("/"));
                MessageUtil.sendClickableMessage(player, s, hoverText.getValue().replace("%cmd%", cmd), cmd, ClickEvent.Action.SUGGEST_COMMAND);
            });
            MessageUtil.sendMessage(player, lines.getValue()); // bottom of msg
        }
        return true;
    }
}
