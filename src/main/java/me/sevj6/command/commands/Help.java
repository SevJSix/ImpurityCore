package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Setting;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Help extends Command implements Instance {

    private final Setting<String> lines = Setting.getString("help.lines");
    private final Setting<String> hoverText = Setting.getString("help.hover_text");
    private final Setting<List<String>> message = Setting.getStringList("help.help_message");

    public Help(Impurity plugin) {
        super("help", "&4Usage: &c/help", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        List<String> help = message.getValue();
        sendMessage(player, lines.getValue()); // top of msg
        help.forEach(s -> {
            String cmd = s.split(" ")[0];
            cmd = cmd.substring(cmd.indexOf("/"));
            MessageUtil.sendClickableMessage(player, s, hoverText.getValue().replace("%cmd%", cmd), cmd, ClickEvent.Action.SUGGEST_COMMAND);
        });
        sendMessage(player, lines.getValue()); // bottom of msg
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
