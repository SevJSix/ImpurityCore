package me.sevj6.mixin;

import me.sevj6.event.TileEntityCreateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    @EventHandler
    public void onTileEntityCreate(TileEntityCreateEvent event) {
        System.out.println("created a tile entity");
        System.out.println(event.getCompound());
    }
}
