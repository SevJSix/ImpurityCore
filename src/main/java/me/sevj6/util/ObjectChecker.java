package me.sevj6.util;

import me.sevj6.listeners.illegals.wrapper.Check;
import net.minecraft.server.v1_12_R1.IInventory;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Container;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectChecker<T> extends Check {

    private final Object object;
    private final T type;

    public ObjectChecker(T obj) {
        this.type = obj;
        this.object = obj;
    }

    public T getType() {
        return type;
    }

    public List<Block> getBlocksInRadius(int radius) {
        try {
            Method locationM = this.type.getClass().getDeclaredMethod("getLocation");
            locationM.setAccessible(true);
            Location loc = (Location) locationM.invoke(this.object);
            List<Block> blocks = new ArrayList<>();
            for (int x = loc.getBlockX() - radius; x <= loc.getBlockX() + radius; x++) {
                for (int y = loc.getBlockY() - radius; y <= loc.getBlockY() + radius; y++) {
                    for (int z = loc.getBlockZ() - radius; z <= loc.getBlockZ() + radius; z++) {
                        Location l = new Location(loc.getWorld(), x, y, z);
                        blocks.add(l.getBlock());
                    }
                }
            }
            return blocks;
        } catch (Throwable t) {
            // couldnt get location
        }
        return null;
    }

    public List<ItemStack> getNearbyItemDrops(int radius) {
        try {
            Method locationM = this.type.getClass().getDeclaredMethod("getLocation");
            locationM.setAccessible(true);
            Location loc = (Location) locationM.invoke(this.object);
            return loc.getNearbyEntitiesByType(Item.class, radius).stream().map(Item::getItemStack).collect(Collectors.toList());
        } catch (Throwable t) {
            // couldnt get location
        }
        return null;
    }

    public Inventory getInventory() {
        try {
            if (this.object instanceof Player) {
                Player player = (Player) this.object;
                return player.getInventory();
            }
            if (this.object instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) this.object;
                if (itemStack.getItemMeta() instanceof BlockStateMeta) {
                    BlockStateMeta blockStateMeta = (BlockStateMeta) itemStack.getItemMeta();
                    if (blockStateMeta.getBlockState() instanceof Container) {
                        Container container = (Container) blockStateMeta.getBlockState();
                        return container.getInventory();
                    }
                }
            } else {
                Method stateM = this.type.getClass().getDeclaredMethod("getState");
                stateM.setAccessible(true);
                BlockState blockState = (BlockState) stateM.invoke(this.object);
                if (blockState instanceof Container) {
                    Container container = (Container) blockState;
                    return container.getInventory();
                }
            }
        } catch (Throwable t) {
            // cannot get inventory of object
        }
        return null;
    }

    public List<NBTTagCompound> getTags() {
        Inventory inventory = getInventory();
        if (inventory != null) {
            IInventory nmsInv = ((CraftInventory) getInventory()).getInventory();
            return nmsInv.getContents().stream().map(net.minecraft.server.v1_12_R1.ItemStack::getTag).collect(Collectors.toList());
        }
        return null;
    }

    public boolean hasKey(String key) {
        List<NBTTagCompound> compounds = getTags();
        if (compounds != null) {
            for (NBTTagCompound compound : compounds) {
                if (compound.hasKey(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getTagSize() {
        List<NBTTagCompound> tags = getTags();
        if (tags != null) {
            return tags.toString().length();
        }
        return -1;
    }

    public boolean isTagSizeTooBig() {
        if (getTagSize() == -1) return false;
        return getTagSize() >= 100000;
    }

    public NBTTagCompound getCompound() {
        try {
            Object o = getNMSObj();
            Method compound = o.getClass().getDeclaredMethod("getTag");
            compound.setAccessible(true);
            return (NBTTagCompound) compound.invoke(o);
        } catch (Throwable t) {
            // cant get compound
        }
        return null;
    }

    public void setCompound(NBTTagCompound compound) {
        try {
            Object o = getNMSObj();
            Method setCompound = o.getClass().getDeclaredMethod("setTag", NBTTagCompound.class);
            setCompound.setAccessible(true);
            setCompound.invoke(o, compound);
        } catch (Throwable t) {
            // cant set compound
        }
    }

    public Object getNMSObj() {
        try {
            Method method = this.type.getClass().getDeclaredMethod("getHandle");
            method.setAccessible(true);
            return method.invoke(this.object);
        } catch (Throwable t) {
            // cant get obj
        }
        return null;
    }

    public void updateItemStackState() {
        try {
            Method itemMeta = this.type.getClass().getDeclaredMethod("getItemMeta");
            itemMeta.setAccessible(true);
            Method setItemMeta = this.type.getClass().getDeclaredMethod("setItemMeta", ItemMeta.class);
            setItemMeta.setAccessible(true);
            ItemMeta meta = (ItemMeta) itemMeta.invoke(this.object);
            if (meta instanceof BlockStateMeta) {
                BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
                BlockState blockState = blockStateMeta.getBlockState();
                blockStateMeta.setBlockState(blockState);
            }
            setItemMeta.invoke(meta);
        } catch (Throwable t) {
            // cant update item state
        }
    }

    public void updateState() {
        try {
            Method update = this.type.getClass().getDeclaredMethod("update");
            update.setAccessible(true);
            update.invoke(this.object);
        } catch (Throwable t) {
            // cant update block state
        }
    }

    public boolean isBlockAndContainsBlockEntityTag() {
        try {
            return hasKey("BlockEntityTag");
        } catch (Throwable t) {
            // cannot determine block entity tag
        }
        return false;
    }

    public boolean isBlockContainingBookNBT() {
        try {
            return hasKey("generation");
        } catch (Throwable t) {
            // cannot determine nbt
        }
        return false;
    }

    public Material getMaterial() {
        try {
            Method method = this.type.getClass().getDeclaredMethod("getType");
            method.setAccessible(true);
            return (Material) method.invoke(this.object);
        } catch (Throwable t) {
            // cannot determine material
        }
        return null;
    }

    public boolean isMaterialType(Material material) {
        return material.equals(getMaterial());
    }

    public Block getBlockRelative(BlockFace face) {
        if (this.object instanceof Block) {
            Block block = (Block) this.object;
            return block.getRelative(face);
        }
        return null;
    }

    @Override
    public void check() {
        if (isTagSizeTooBig()) {
            getInventory().clear();
            updateState();
        }
    }
}
