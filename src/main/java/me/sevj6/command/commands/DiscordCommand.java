package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            MessageUtil.sendClickableMessage(player, "&b&lImpurity Official Discord. (Click Here)", "&3Click to Join the Discord", MessageUtil.getDiscord(), ClickEvent.Action.OPEN_URL);
        }
        return true;
    }
}
