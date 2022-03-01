package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CopyInventory extends Command {
    public CopyInventory(Impurity plugin) {
        super("ci", "&4Usage: &c/ci", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (!player.isOp()) return;
        Player target = Bukkit.getPlayer(args[0]);
        if (target.isOnline()) {
            player.getInventory().setContents(target.getInventory().getContents());
            MessageUtil.sendMessage(player, "&3You have copied &3" + target.getName() + "'s &binventory");
        } else {
            MessageUtil.sendMessage(player, "&4That player is not online");
        }
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }

}
