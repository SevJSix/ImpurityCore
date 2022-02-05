package me.sevj6.listeners.misc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GreenText implements Listener {

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        char begginingChar = event.getMessage().toCharArray()[0];
        switch (begginingChar) {
            case '>':
                handleMsg(event, ChatColor.GREEN);
                break;
            case '#':
                handleMsg(event, ChatColor.YELLOW);
                break;
            case '`':
                handleMsg(event, ChatColor.BLUE);
                break;
            default:
                event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                break;
        }
    }

    private void handleMsg(AsyncPlayerChatEvent event, ChatColor color) {
        event.setMessage(color + event.getMessage());
    }
}
