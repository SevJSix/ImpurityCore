package me.sevj6.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class TileEntityCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private final World world;
    private final NBTTagCompound compound;
    private final Class<? extends TileEntity> type;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
