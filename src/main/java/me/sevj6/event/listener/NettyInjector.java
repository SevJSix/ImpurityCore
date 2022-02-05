package me.sevj6.event.listener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.sevj6.Impurity;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.entity.Player;

public class NettyInjector extends ChannelDuplexHandler {
    private final Player player;

    public NettyInjector(Player player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PacketEvent.Incoming incoming = new PacketEvent.Incoming((Packet<?>) msg, player);
        Impurity.EVENT_BUS.post(incoming);
        if (!incoming.isCancelled()) super.channelRead(ctx, incoming.getPacket());
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        PacketEvent.Outgoing outgoing = new PacketEvent.Outgoing((Packet<?>) msg, player);
        Impurity.EVENT_BUS.post(outgoing);
        if (!outgoing.isCancelled()) super.write(ctx, outgoing.getPacket(), promise);
    }
}
