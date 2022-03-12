package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.Larper;
import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class Troll extends Command {
    public Troll(Impurity plugin) {
        super("troll", "fag", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            Larper larper = new Larper(Bukkit.getPlayer(args[0]));
            larper.troll();
            larper.getLarper().chat("> I've just been trolled (I am also a tranny)");
            sendMessage(sender, "&6You just trolled " + larper.getLarper().getName());
        } catch (Throwable t) {
            MessageUtil.sendMessage(sender, "&4Specify a player");
        }
    }

    @Override
    public String[] onTabComplete() {
        return new String[0];
    }
}
