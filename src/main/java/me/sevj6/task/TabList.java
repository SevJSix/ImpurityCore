package me.sevj6.task;

import me.sevj6.Instance;
import me.sevj6.task.scheduler.ScheduledTask;
import me.sevj6.task.scheduler.TaskForce;
import me.sevj6.util.MessageUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

public class TabList implements TaskForce, Instance {

    @ScheduledTask
    public void updateTab() {
        if (Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                String header = String.join("\n", fileConfig.getTablist().getStringList("TabList.Header"));
                String footer = String.join("\n", fileConfig.getTablist().getStringList("TabList.Footer"));
                BaseComponent h = new TextComponent(MessageUtil.parseTab(header, player));
                BaseComponent f = new TextComponent(MessageUtil.parseTab(footer, player));
                player.setPlayerListHeaderFooter(h, f);
            });
        }
    }
}
