package me.sevj6.event.listener;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.event.events.TotemPopEvent;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ListenerTotemPop implements NMSPacketListener {

    public static Field opCode;
    public static Field entityId;

    static {
        try {
            ListenerTotemPop.opCode = PacketPlayOutEntityStatus.class.getDeclaredField("b");
            ListenerTotemPop.opCode.setAccessible(true);
            ListenerTotemPop.entityId = PacketPlayOutEntityStatus.class.getDeclaredField("a");
            ListenerTotemPop.entityId.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @NMSEventHandler
    public void onTotemPop(PacketEvent.Outgoing event) {
        if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
            PacketPlayOutEntityStatus packet = (PacketPlayOutEntityStatus) event.getPacket();
            try {
                if (ListenerTotemPop.opCode.getByte(packet) == 35) {
                    World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
                    Entity entity = world.getEntity(ListenerTotemPop.entityId.getInt(packet));
                    if (entity == null) return;
                    if (entity.getWorld() != world) return;
                    if (world.getEntity(ListenerTotemPop.entityId.getInt(packet)) instanceof EntityPlayer) {
                        Impurity.EVENT_BUS.post(new TotemPopEvent((Player) entity.getBukkitEntity()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
