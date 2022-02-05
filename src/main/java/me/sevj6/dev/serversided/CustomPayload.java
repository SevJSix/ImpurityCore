package me.sevj6.dev.serversided;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayInCustomPayload;
import org.bukkit.Bukkit;

public class CustomPayload implements NMSPacketListener {
    private final ServersidedAuto32k serversidedAuto32k = new ServersidedAuto32k();

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInCustomPayload) {
            PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket();
            if (packet.a().equals("auto32k")) {
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                    PacketDataSerializer serializer = packet.b();
                    BlockPosition pos = serializer.e();
                    serversidedAuto32k.doPlace(event.getPlayer(), pos);
                });
            }
        }
    }
}
