package me.sevj6.event.events;

import me.sevj6.event.bus.Cancellable;
import me.sevj6.event.bus.Event;
import me.sevj6.util.ServersideUtil;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.entity.Player;

public class PlayerServerSide32kEvent extends Event implements Cancellable {

    private final Player player;
    private final BlockPosition placePos;
    private boolean cancelled;

    // Manual 32k
    public PlayerServerSide32kEvent(Player player, PacketDataSerializer serializer) {
        this.player = player;
        this.placePos = serializer.e();
    }

    // Auto 32k
    public PlayerServerSide32kEvent(Player player) {
        this.player = player;
        this.placePos = ServersideUtil.getAutoPlace32kPos(player);
    }

    public BlockPosition getPlacePos() {
        return placePos;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
