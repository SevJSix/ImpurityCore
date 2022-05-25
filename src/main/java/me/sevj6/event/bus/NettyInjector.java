package me.sevj6.event.bus;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.sevj6.Impurity;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class NettyInjector extends ChannelDuplexHandler {

    @Getter
    private final Player player;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketEvent.ClientToServer clientToServer = new PacketEvent.ClientToServer((Packet<?>) msg, player);
        Impurity.EVENT_BUS.post(clientToServer);
        if (!clientToServer.isCancelled()) super.channelRead(ctx, clientToServer.getPacket());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketEvent.ServerToClient serverToClient = new PacketEvent.ServerToClient((Packet<?>) msg, player);
        Impurity.EVENT_BUS.post(serverToClient);
        if (!serverToClient.isCancelled()) super.write(ctx, serverToClient.getPacket(), promise);
    }
}
