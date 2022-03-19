package me.sevj6.listener.patches;

import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.ObjectChecker;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketPlayOutSetSlot;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class NBTLimitBan implements SevListener {

    private Field itemF;

    public NBTLimitBan() {
        try {
            itemF = PacketPlayOutSetSlot.class.getDeclaredField("c");
            itemF.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @SevHandler
    public void onPacket(PacketEvent.ServerToClient event) {
        if (event.getPacket() instanceof PacketPlayOutSetSlot) {
            try {
                PacketPlayOutSetSlot packet = (PacketPlayOutSetSlot) event.getPacket();
                ItemStack itemStack = CraftItemStack.asBukkitCopy((net.minecraft.server.v1_12_R1.ItemStack) itemF.get(packet));
                ObjectChecker<ItemStack> item = new ObjectChecker<>(itemStack);
                if (item.isTagSizeTooBig()) {
                    net.minecraft.server.v1_12_R1.ItemStack copy = (net.minecraft.server.v1_12_R1.ItemStack) itemF.get(packet);
                    copy.setTag(new NBTTagCompound());
                    int index = ((CraftPlayer) event.getPlayer()).getHandle().inventory.itemInHandIndex;
                    event.setPacket(new PacketPlayOutSetSlot(index, index, copy));
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
