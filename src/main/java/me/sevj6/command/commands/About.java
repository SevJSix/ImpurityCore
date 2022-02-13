package me.sevj6.command.commands;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class About extends Command {
    public About(Impurity plugin) {
        super("serverinfo", "&4/about", plugin);
    }

    public static String getJoinDate(Object object) {
        DateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        String jd = null;
        if (object instanceof Player) {
            Player player = (Player) object;
            Date date = new Date(player.getFirstPlayed());
            jd = dateFormat.format(date);
        } else if (object instanceof OfflinePlayer) {
            OfflinePlayer player = (OfflinePlayer) object;
            Date date = new Date(player.getFirstPlayed());
            jd = dateFormat.format(date);
        }
        return jd;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        MessageUtil.sendMessage(sender, "&3Impurity.me is a server with a history of map resets. The map has been reset a total of 7 times throughout it's history." +
                " Impurity is known mostly for it's unique pvp meta with 32k weapons. Although the map has been reset multiple times, we do not plan on ever resetting it again." +
                " You can join the community discord at " + MessageUtil.getDiscord());
        String link = getPasteBinLink((Player) sender);
        MessageUtil.sendMessage(sender, "&bYou can view all of the players who have ever joined at this link: " + link);
    }

    private String getPasteBinLink(Player player) {
        try {
            URL url = new URL("https://pastebin.com/api/api_post.php");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            Map<String, String> arguments = new HashMap<>();

            arguments.put("api_dev_key", "o5b8hmW9AaYXlZvq1S0q-I-lVYQZrkcg");
            arguments.put("api_option", "paste");
            StringBuilder sb = new StringBuilder();
            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                sb.append(offlinePlayer.getName()).append(" - Joined ").append(getJoinDate(offlinePlayer)).append("\n");
            }
            arguments.put("api_paste_code", sb.toString());
            arguments.put("api_paste_name", "Unique Joins On Impurity Requested By " + player.getName());
            StringJoiner sj = new StringJoiner("&");
            for (Map.Entry<String, String> entry : arguments.entrySet())
                sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                        + URLEncoder.encode(entry.getValue(), "UTF-8"));
            byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(out);
            InputStream is = http.getInputStream();
            return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        } catch (IOException urlException) {
            urlException.printStackTrace();
        }
        return ChatColor.RED + "PASTEBIN LINK ERROR RESPONSE 422, report this to SevJ6#2521";
    }
}
