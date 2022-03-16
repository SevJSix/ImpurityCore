package me.sevj6.listener.misc;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilColoredName implements Listener {

    @EventHandler
    public void onRename(PrepareAnvilEvent event) {
        try {
            String name = event.getResult().getI18NDisplayName();
            name = ChatColor.translateAlternateColorCodes('&', name);
            ItemMeta meta = event.getResult().getItemMeta();
            meta.setDisplayName(name);
            event.getResult().setItemMeta(meta);
        } catch (Throwable ignored) {
        }
    }
}
