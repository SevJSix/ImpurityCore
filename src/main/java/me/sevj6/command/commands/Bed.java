package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bed extends Command {

    public Bed(Impurity plugin) {
        super("bed", "&4Usage: &c/bed", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (player.getBedSpawnLocation() == null) {
            MessageUtil.sendMessage(player, "&3You currently do not have a bed set.");
            return;
        }
        MessageUtil.sendMessage(player, "&3Your bed spawn location is at &r" + Utils.formatLocation(player.getBedSpawnLocation()));
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
