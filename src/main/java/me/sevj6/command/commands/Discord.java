package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Discord extends Command {
    public Discord(Impurity plugin) {
        super("discord", "&4Usage: &c/discord", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        MessageUtil.sendClickableMessage(player, "&b&lImpurity Official Discord. (Click Here)", "&3Click to Join the Discord", MessageUtil.getDiscord(), ClickEvent.Action.OPEN_URL);
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
