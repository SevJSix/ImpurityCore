package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NameColor extends Command implements Instance {
    private final Configuration NC = fileConfig.getNamecolor();
    private final String AVAILABLE_COLORS = "&4dark_red &cred &6gold &eyellow &2dark_green &agreen &baqua &3dark_aqua &1dark_blue &9blue &dlight_purple &5dark_purple &7gray &8dark_gray &0black&r &lbold&r &mstrikethrough&r &nunderline&r &oitalic&r random reset";

    public NameColor(Impurity plugin) {
        super("nc", "&4Usage: &c/nc <color>", plugin);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if (args.length > 0) {

            StringBuilder builder = new StringBuilder();
            List<Character> chars = new ArrayList<>();
            List<ChatColor> colors;

            switch (args[0].toLowerCase()) {
                case "random":
                    colors = Arrays.asList(ChatColor.values());

                    for (int i = 0; i < player.getName().length(); i++) {
                        char c = player.getName().charAt(i);
                        chars.add(c);
                    }

                    for (int i = 0; i < chars.size(); i++) {
                        if (i == colors.size()) {
                            i = 0;
                        }
                        int index = ThreadLocalRandom.current().nextInt(colors.size());
                        builder.append(colors.get(index)).append(chars.get(i));
                    }
                    setDisplayName(player, builder.toString());
                    break;
                case "codes":
                    try {
                        if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[1])).equals(player.getName())) {
                            setDisplayName(player, args[1]);
                        } else {
                            MessageUtil.sendMessage(player, "&4Invalid color code or username processing. Please type /nc for help");
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        MessageUtil.sendMessage(player, "&4Please include an input after the command arguments. i.e. /nc codes <input>");
                    }
                    break;
                case "reset":
                    player.setDisplayName(null);
                    NC.set(String.valueOf(player.getUniqueId()), "null");
                    NC.saveConfig();
                    NC.reloadConfig();
                    MessageUtil.sendMessage(player, "&6Your username has been reset.");
                    break;
                default:
                    try {
                        for (String arg : args) {
                            ChatColor c = ChatColor.valueOf(arg.toUpperCase().replace("magic", ""));
                            builder.append(c);
                        }
                        setDisplayName(player, builder + player.getName());
                    } catch (IllegalArgumentException ignored) {
                        MessageUtil.sendMessage(player, "&3Invalid color type!");
                        MessageUtil.sendMessage(player, "&bAvailable formats:" + "\n" + AVAILABLE_COLORS);
                    }
                    break;
            }
        } else {
            MessageUtil.sendMessage(player, "&bAvailable colors:" + "\n" + AVAILABLE_COLORS);
            MessageUtil.sendMessage(player, "You can also type &2/nc codes");
            player.sendMessage("Example: /nc codes &4&lSev&6&lJ6 =" + ChatColor.translateAlternateColorCodes('&', " &4&lSev&6&lJ6"));
        }
    }

    private void setDisplayName(Player player, String name) {
        name = name.replace("§k", "") + ChatColor.RESET;
        MessageUtil.sendMessage(player, "&3Your name is now: &r" + name);
        player.setDisplayName(name);
        NC.set(String.valueOf(player.getUniqueId()), name);
        NC.saveConfig();
        NC.reloadConfig();
    }
}
