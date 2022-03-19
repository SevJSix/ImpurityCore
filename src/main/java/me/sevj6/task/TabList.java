package me.sevj6.task;

import me.sevj6.Instance;
import me.sevj6.task.scheduler.ScheduledTask;
import me.sevj6.task.scheduler.TaskForce;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.fileutil.Setting;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

import java.util.List;

public class TabList implements TaskForce, Instance {

    private final Setting<List<String>> head = Setting.getStringList("text.Header");
    private final Setting<List<String>> foot = Setting.getStringList("text.Footer");

    @ScheduledTask
    public void updateTab() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                String header = String.join("\n", head.getValue());
                String footer = String.join("\n", foot.getValue());
                BaseComponent h = new TextComponent(MessageUtil.parseTab(header, player));
                BaseComponent f = new TextComponent(MessageUtil.parseTab(footer, player));
                player.setPlayerListHeaderFooter(h, f);
            });
        }
    }
}
