package me.sevj6.event.events;

import me.sevj6.event.bus.Cancellable;
import me.sevj6.event.bus.Event;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

public class PacketEvent extends Event implements Cancellable {
    private final Player player;
    private boolean cancel;
    private Packet<?> packet;

    public PacketEvent(Packet<?> packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

    @Override
    public boolean isCancelled() {
        return cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    public static class ClientToServer extends PacketEvent {

        public ClientToServer(Packet<?> packet, Player player) {
            super(packet, player);
        }
    }

    public static class ServerToClient extends PacketEvent {

        public ServerToClient(Packet<?> packet, Player player) {
            super(packet, player);
        }
    }
}
