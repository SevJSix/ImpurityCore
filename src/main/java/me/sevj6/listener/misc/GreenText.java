package me.sevj6.listener.misc;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GreenText implements Listener, Instance {

    private final Setting<Boolean> greenText = Setting.getBoolean("green_text");
    private final Setting<Boolean> colorCodes = Setting.getBoolean("translate_color_codes");

    @EventHandler
    public void on(AsyncPlayerChatEvent event) {
        if (greenText.getValue()) {
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
                    if (colorCodes.getValue()) {
                        event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                    }
                    break;
            }
        }
    }

    private void handleMsg(AsyncPlayerChatEvent event, ChatColor color) {
        event.setMessage(color + event.getMessage());
    }
}
