package me.sevj6.command.commands;

import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class NameColorCommand implements TabExecutor, Instance {

    private final String AVAILABLE_COLORS = "&4dark_red &cred &6gold &eyellow &2dark_green &agreen &baqua &3dark_aqua &1dark_blue &9blue &dlight_purple &5dark_purple &7gray &8dark_gray &0black&r &lbold&r &mstrikethrough&r &nunderline&r &oitalic&r random reset";
    private final List<ChatColor> colors = Arrays.asList(ChatColor.values());
    private final String[] tabCompletions = new String[]{"dark_red", "red", "gold", "yellow",
            "dark_green", "green aqua", "dark_aqua", "dark_blue",
            "blue", "light_purple", "dark_purple", "gray", "dark_gray",
            "black", "bold", "strikethrough", "underline", "italic", "random", "reset", "codes"};

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                StringBuilder builder = new StringBuilder();
                switch (args[0]) {
                    case "codes":
                        if (ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', args[1])).equals(player.getName())) {
                            if (args[1].contains("&k")) {
                                MessageUtil.sendMessage(player, "&4You cannot use obfuscated text in your name!");
                            } else {
                                setDisplayName(player, args[1]);
                            }
                        } else {
                            MessageUtil.sendMessage(player, "&4Invalid color code or username processing. Please type /nc for help");
                        }
                        break;
                    case "reset":
                        player.setDisplayName(null);
                        namecolor.set(String.valueOf(player.getUniqueId()), "null");
                        namecolor.saveConfig();
                        namecolor.reloadConfig();
                        MessageUtil.sendMessage(player, "&6Your username has been reset.");
                        break;
                    case "random":
                        char[] chars = player.getName().toCharArray();
                        for (int i = 0; i < chars.length; i++) {
                            if (i == colors.size()) i = 0;
                            int index = ThreadLocalRandom.current().nextInt(colors.size());
                            builder.append(colors.get(index)).append(chars[i]);
                        }
                        setDisplayName(player, builder.toString());
                        break;
                    default:
                        try {
                            for (String arg : args) {
                                ChatColor c = ChatColor.valueOf(arg.toUpperCase().replace("magic", ""));
                                builder.append(c);
                            }
                            setDisplayName(player, builder + player.getName());
                        } catch (IllegalArgumentException ignored) {
                            sendTutorial(player);
                        }
                        break;
                }
            } else {
                sendTutorial(player);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Arrays.asList(tabCompletions);
    }

    private void setDisplayName(Player player, String name) {
        name = name.replace("§k", "") + ChatColor.RESET;
        MessageUtil.sendMessage(player, "&3Your name is now: &r" + name);
        player.setDisplayName(name);
        namecolor.set(String.valueOf(player.getUniqueId()), name);
        namecolor.saveConfig();
        namecolor.reloadConfig();
    }

    public void sendTutorial(Player player) {
        MessageUtil.sendMessage(player, "&5-----------------------------------------------------");
        MessageUtil.sendMessage(player, "&r&oColor Formats:&r");
        MessageUtil.sendMessage(player, AVAILABLE_COLORS);
        MessageUtil.sendMessage(player, "");
        MessageUtil.sendMessage(player, "&r&oManual formatting:&r");
        MessageUtil.sendMessage(player, "&c/nc codes <formatted_name>");
        String playerName = player.getName();
        int index = (playerName.length() / 2);
        String firstPart = playerName.substring(0, index);
        String secondPart = playerName.substring(index);
        String formattedName = "&4&l" + firstPart + "&a&l" + secondPart;
        player.sendMessage("Example: /nc codes " + formattedName);
        MessageUtil.sendMessage(player, "Output: " + formattedName + "&r");
        MessageUtil.sendMessage(player, "&5-----------------------------------------------------");
    }
}
