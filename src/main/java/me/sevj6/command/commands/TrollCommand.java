package me.sevj6.command.commands;

import me.sevj6.Instance;
import me.sevj6.util.Larper;
import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TrollCommand implements CommandExecutor, Instance {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Larper larper = new Larper(Bukkit.getPlayer(args[0]));
            larper.troll();
            larper.getLarper().chat("> I've just been trolled (I am also a tranny)");
            sendMsg(sender, "&6You just trolled " + larper.getLarper().getName());
        } catch (Throwable t) {
            MessageUtil.sendMessage(sender, "&4Specify a player");
        }
        return true;
    }
}
