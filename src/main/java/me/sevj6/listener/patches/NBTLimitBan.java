package me.sevj6.listener.patches;

import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.ObjectChecker;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
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
        } else if (event.getPacket() instanceof PacketPlayOutTileEntityData) {
            PacketPlayOutTileEntityData packet = (PacketPlayOutTileEntityData) event.getPacket();
            try {
                Field pos = PacketPlayOutTileEntityData.class.getDeclaredField("a");
                pos.setAccessible(true);
                BlockPosition blockPosition = (BlockPosition) pos.get(packet);
                Field tag = PacketPlayOutTileEntityData.class.getDeclaredField("c");
                tag.setAccessible(true);
                NBTTagCompound tagCompound = (NBTTagCompound) tag.get(packet);
                Field integer = PacketPlayOutTileEntityData.class.getDeclaredField("b");
                integer.setAccessible(true);
                int i = (int) integer.get(packet);
                if (tagCompound.isEmpty()) return;
                TileEntity tileEntity = ((CraftWorld) event.getPlayer().getWorld()).getHandle().getTileEntity(blockPosition);
                if (tagCompound.toString().length() > 2200000) {
                    event.setPacket(new PacketPlayOutTileEntityData(blockPosition, i, new NBTTagCompound()));
                    if (tileEntity != null) {
                        NBTTagCompound emptyTag = new NBTTagCompound();
                        tileEntity.load(emptyTag);
                        tileEntity.save(emptyTag);
                        return;
                    }
                }
                Location location = new Location(event.getPlayer().getWorld(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
                if (location.getBlock().getState() instanceof Container) {
                    Container container = (Container) location.getBlock().getState();
                    ObjectChecker<Container> objectChecker = new ObjectChecker<>(container);
                    if (objectChecker.isTagSizeTooBig()) {
                        event.setCancelled(true);
                        objectChecker.check();
                    }
                }
            } catch (Throwable ignored) {
            }
        }
    }
}
