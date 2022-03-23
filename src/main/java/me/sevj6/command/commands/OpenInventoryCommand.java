package me.sevj6.command.commands;

import me.sevj6.util.MessageUtil;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class OpenInventoryCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp() && sender instanceof Player) {
            EntityPlayer player = ((CraftPlayer) sender).getHandle();
            if (args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null && target.isOnline()) {
                    EntityPlayer entityTarget = ((CraftPlayer) target).getHandle();
                    player.openContainer(entityTarget.inventory);
                } else {
                    MessageUtil.sendMessage(sender, "&4Player is offline");
                }
            } else {
                MessageUtil.sendMessage(sender, "&4Specify a player");
            }
        }
        return true;
    }
}
