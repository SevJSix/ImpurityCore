package me.sevj6.listeners.patches;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.ViolationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class AntiSpam extends ViolationManager implements Listener {

    private final ConcurrentHashMap<Player, String> lastMessage = new ConcurrentHashMap<>();

    public AntiSpam() {
        super(1, 3);
    }

    public boolean isSimilar(String str_1, String str_2) {
        Pattern pattern = Pattern.compile(str_1);
        return str_2.matches(str_2);
    }

    public boolean isRegex(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]*$");
        return pattern.matcher(str).matches();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        if (lastMessage.containsKey(player)) {
            if (isSimilar(message, lastMessage.get(player))) {
                for (int i = 0; i < 3; i++) {
                    increment(player.getUniqueId());
                }
            } else if (isRegex(message)) {
                increment(player.getUniqueId());
            }
            lastMessage.replace(player, message);
        } else {
            lastMessage.put(player, message);
        }
        if (getVLS(player.getUniqueId()) > 3) {
            event.setCancelled(true);
            MessageUtil.sendMessage(player, "&4You were detected spamming. Stop it nigga.");
        }
    }
}
