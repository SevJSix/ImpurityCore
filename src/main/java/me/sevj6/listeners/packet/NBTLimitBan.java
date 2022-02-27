package me.sevj6.listeners.packet;

import me.sevj6.event.SevHandler;
import me.sevj6.event.SevListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutMapChunk;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class NBTLimitBan implements SevListener {

    private final int MAX_SIZE = 2097152;
    private Field nbtF;

    public NBTLimitBan() {
        try {
            nbtF = PacketPlayOutMapChunk.class.getDeclaredField("e");
            nbtF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SevHandler
    public void onPacket(PacketEvent.ServerToClient event) {
        if (event.getPacket() instanceof PacketPlayOutMapChunk) {
            try {
                PacketPlayOutMapChunk packet = (PacketPlayOutMapChunk) event.getPacket();
                List<NBTTagCompound> tileEntities = (List<NBTTagCompound>) nbtF.get(packet);
                double dataSize = tileEntities.stream().
                        map(o -> o.toString().length()).
                        collect(Collectors.toList()).
                        stream().
                        mapToDouble(Integer::doubleValue).
                        sum();
                if (dataSize >= MAX_SIZE) tileEntities.clear();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}
