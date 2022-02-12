package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpenInventory extends Command {
    public OpenInventory(Impurity plugin) {
        super("inv", "&4Usage: &c/inv <player>", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.isOp() && sender instanceof Player) {
            EntityPlayer player = ((CraftPlayer) sender).getHandle();
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    EntityPlayer entityTarget = ((CraftPlayer) target).getHandle();
                    player.openContainer(entityTarget.inventory);
                }
            }
        }
    }
}
