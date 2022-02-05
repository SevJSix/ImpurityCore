package me.sevj6.dev.serversided;

import me.sevj6.util.MessageUtil;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Hopper;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author SevJ6
 * @since 12/29/21 / 2:57 AM
 */
public class ServersidedAuto32k {

    public void doPlace(Player player, BlockPosition pos) {
        if (pos == null) {
            MessageUtil.sendMessage(player, "&cInvalid block position!");
            return;
        }
        Block block = player.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EnumDirection facing = entityPlayer.getDirection();
        Location looking = block.getLocation();
        EnumDirection opposite = facing.opposite();

        if (block.getType() == Material.AIR) {
            MessageUtil.sendMessage(player, "&cCannot place on air!");
            return;
        }

        if (isNotViablePlacePos(looking.clone().add(0, 1, 0))) {
            MessageUtil.sendMessage(player, "&cYou cannot place there!");
            return;
        }

        if (get32kShulkerFromInv(player) == null) {
            MessageUtil.sendMessage(player, "&cMissing 32k shulker!");
            return;
        }

        ShulkerBox shulkerFromInv = (ShulkerBox) ((BlockStateMeta) get32kShulkerFromInv(player).getItemMeta()).getBlockState();
        List<net.minecraft.server.v1_12_R1.ItemStack> nmsItemList = new ArrayList<>();
        for (ItemStack shulkerItem : shulkerFromInv.getInventory().getContents()) {
            nmsItemList.add(CraftItemStack.asNMSCopy(shulkerItem));
        }
        net.minecraft.server.v1_12_R1.ItemStack[] nmsItemArray = nmsItemList.toArray(new net.minecraft.server.v1_12_R1.ItemStack[0]);

        //check if players have the materials needed for auto32k
        int obsidianSlot = getItemSlotFromInv(player, Material.OBSIDIAN);
        int dispenserSlot = getItemSlotFromInv(player, Material.DISPENSER);
        int redstoneSlot = getItemSlotFromInv(player, Material.REDSTONE_BLOCK);
        int hopperSlot = getItemSlotFromInv(player, Material.HOPPER);

        if (obsidianSlot == -1) {
            MessageUtil.sendMessage(player, "&cMissing obsidian!");
            return;
        } else if (dispenserSlot == -1) {
            MessageUtil.sendMessage(player, "&cMissing dispenser!");
            return;
        } else if (redstoneSlot == -1) {
            MessageUtil.sendMessage(player, "&cMissing redstone!");
            return;
        } else if (hopperSlot == -1) {
            MessageUtil.sendMessage(player, "&cMissing hopper!");
            return;
        }

        //place locations
        Location obsidianLocation;
        Location dispenserLocation;
        Location redstoneLocation = null;
        Location hopperLocation;
        Location shulkerLocation;
        boolean doPlaceObsidian = false;

        Block hopperAttemptBlock = null;

        switch (opposite) {
            case EAST:
                hopperAttemptBlock = looking.getWorld().getBlockAt(looking.clone().add(1, 0, 0));
                break;
            case WEST:
                hopperAttemptBlock = looking.getWorld().getBlockAt(looking.clone().add(-1, 0, 0));
                break;
            case NORTH:
                hopperAttemptBlock = looking.getWorld().getBlockAt(looking.clone().add(0, 0, -1));
                break;
            case SOUTH:
                hopperAttemptBlock = looking.getWorld().getBlockAt(looking.clone().add(0, 0, 1));
                break;
        }

        if (Objects.requireNonNull(hopperAttemptBlock).getType() == Material.AIR) {
            obsidianLocation = looking;
        } else {
            obsidianLocation = looking.clone().add(0, 1, 0);
            doPlaceObsidian = true;
        }

        dispenserLocation = obsidianLocation.clone().add(0, 1, 0);

        if (isNotViablePlacePos(dispenserLocation.clone().add(0, 1, 0))) {
            switch (opposite) {
                case EAST:
                case WEST:
                    redstoneLocation = dispenserLocation.clone().add(0, 0, -1);
                    break;
                case NORTH:
                case SOUTH:
                    redstoneLocation = dispenserLocation.clone().add(1, 0, 0);
                    break;
            }
        } else {
            redstoneLocation = dispenserLocation.clone().add(0, 1, 0);
        }

        switch (opposite) {
            case EAST:
                hopperLocation = dispenserLocation.clone().add(1, -1, 0);
                break;
            case WEST:
                hopperLocation = dispenserLocation.clone().add(-1, -1, 0);
                break;
            case NORTH:
                hopperLocation = dispenserLocation.clone().add(0, -1, -1);
                break;
            case SOUTH:
                hopperLocation = dispenserLocation.clone().add(0, -1, 1);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + opposite);
        }

        if (doPlaceObsidian) {
            obsidianLocation.getBlock().setType(Material.OBSIDIAN);
            player.getInventory().getItem(obsidianSlot).subtract();
        }
        if (!isNotViablePlacePos(dispenserLocation)) {
            dispenserLocation.getBlock().setType(Material.DISPENSER);
            player.getInventory().getItem(dispenserSlot).subtract();
        } else {
            MessageUtil.sendMessage(player, "&cInvalid dispenser location!");
            return;
        }
        if (!isNotViablePlacePos(redstoneLocation)) {
            redstoneLocation.getBlock().setType(Material.REDSTONE_BLOCK);
            player.getInventory().getItem(redstoneSlot).subtract();
        } else {
            MessageUtil.sendMessage(player, "&cInvalid redstone location!");
            return;
        }
        if (!isNotViablePlacePos(hopperLocation)) {
            hopperLocation.getBlock().setType(Material.HOPPER);
            player.getInventory().getItem(hopperSlot).subtract();
        } else {
            MessageUtil.sendMessage(player, "&cInvalid hopper location!");
            return;
        }

        World world = entityPlayer.getWorld();
        BlockPosition dispenserPos = new BlockPosition(dispenserLocation.getBlockX(), dispenserLocation.getBlockY(), dispenserLocation.getBlockZ());
        TileEntityDispenser dispenser = (TileEntityDispenser) world.getTileEntity(dispenserPos);
        if (dispenser == null) return;
        rotateTileEntity(dispenser, (CraftPlayer) player);

        shulkerLocation = hopperLocation.clone().add(0, 1, 0);

        ShulkerBox shulkerBox = genShulker(shulkerLocation, player);
        TileEntityShulkerBox tileEntityShulkerBox = (TileEntityShulkerBox) world.getTileEntity(new BlockPosition(shulkerBox.getX(), shulkerBox.getY(), shulkerBox.getZ()));
        if (tileEntityShulkerBox == null) return;
        for (int i = 0; i < tileEntityShulkerBox.getSize(); i++) {
            tileEntityShulkerBox.setItem(i, nmsItemArray[i]);
        }
        tileEntityShulkerBox.setCustomName(shulkerFromInv.getCustomName());
        tileEntityShulkerBox.update();
        player.getInventory().getItem(getItemSlotFromInv(player, get32kShulkerFromInv(player).getType())).subtract();
        ItemStack bukkit32kCopy = null;
        for (net.minecraft.server.v1_12_R1.ItemStack itemStack : nmsItemArray) {
            if (itemStack == null) return;
            bukkit32kCopy = CraftItemStack.asBukkitCopy(itemStack);
        }
        if (bukkit32kCopy == null) bukkit32kCopy = gen32k();
        bukkit32kCopy.setAmount(1);
        Hopper hopper = (Hopper) hopperLocation.getBlock().getState();
        IInventory inventory = ((CraftInventory) hopper.getInventory()).getInventory();
        entityPlayer.openContainer(inventory);
        setHandItem((CraftPlayer) player, bukkit32kCopy);
    }

    private ItemStack gen32k() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 32767);
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        item.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, 32767);
        item.addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, 10);
        item.addUnsafeEnchantment(Enchantment.SWEEPING_EDGE, 3);
        item.addUnsafeEnchantment(Enchantment.DURABILITY, 32767);
        item.addUnsafeEnchantment(Enchantment.MENDING, 1);
        item.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
        meta.setDisplayName("Alpha's Stacked 32k's");
        item.setItemMeta(meta);
        return item;
    }

    private ShulkerBox genShulker(Location loc, Player player) {
        ItemStack shulkerFromInv = get32kShulkerFromInv(player);
        ItemMeta meta = shulkerFromInv.getItemMeta();
        BlockStateMeta bsm = (BlockStateMeta) meta;
        ShulkerBox itemShulker = (ShulkerBox) bsm.getBlockState();
        Block block = loc.getBlock();
        switch (itemShulker.getColor()) {
            case RED:
                block.setType(Material.RED_SHULKER_BOX);
                break;
            case BLUE:
                block.setType(Material.BLUE_SHULKER_BOX);
                break;
            case CYAN:
                block.setType(Material.CYAN_SHULKER_BOX);
                break;
            case GRAY:
                block.setType(Material.GRAY_SHULKER_BOX);
                break;
            case LIME:
                block.setType(Material.LIME_SHULKER_BOX);
                break;
            case PINK:
                block.setType(Material.PINK_SHULKER_BOX);
                break;
            case BLACK:
                block.setType(Material.BLACK_SHULKER_BOX);
                break;
            case BROWN:
                block.setType(Material.BROWN_SHULKER_BOX);
                break;
            case GREEN:
                block.setType(Material.GREEN_SHULKER_BOX);
                break;
            case WHITE:
                block.setType(Material.WHITE_SHULKER_BOX);
                break;
            case ORANGE:
                block.setType(Material.ORANGE_SHULKER_BOX);
                break;
            case PURPLE:
                block.setType(Material.PURPLE_SHULKER_BOX);
                break;
            case SILVER:
                block.setType(Material.SILVER_SHULKER_BOX);
                break;
            case YELLOW:
                block.setType(Material.YELLOW_SHULKER_BOX);
                break;
            case MAGENTA:
                block.setType(Material.MAGENTA_SHULKER_BOX);
                break;
            case LIGHT_BLUE:
                block.setType(Material.LIGHT_BLUE_SHULKER_BOX);
                break;
        }
        ShulkerBox shulkerBox = (ShulkerBox) block.getState();
        shulkerBox.update(true, true);
        return shulkerBox;
    }

    private boolean isNotViablePlacePos(Location location) {
        Block block = location.getWorld().getBlockAt(location);
        return block != null && (block.getType().isSolid() || block.getType().isOccluding());
    }

    private void setHandItem(CraftPlayer player, ItemStack itemStack) {
        PlayerInventory inventory = player.getInventory();
        if (inventory.getItemInMainHand() == null) {
            player.getInventory().setItemInMainHand(itemStack);
        } else {
            if (inventory.firstEmpty() == -1) {
                net.minecraft.server.v1_12_R1.ItemStack toThrow = CraftItemStack.asNMSCopy(inventory.getItem(inventory.getHeldItemSlot()));
                player.getHandle().drop(toThrow, true);
            } else {
                int slot = inventory.firstEmpty();
                inventory.setItem(slot, inventory.getItemInMainHand());
            }
            inventory.setItemInMainHand(itemStack);
        }
    }

    public ItemStack get32kShulkerFromInv(Player player) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null) {
                if (itemStack.hasItemMeta()) {
                    if (itemStack.getItemMeta() instanceof BlockStateMeta && ((BlockStateMeta) itemStack.getItemMeta()).getBlockState() instanceof ShulkerBox) {
                        ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState();
                        for (ItemStack shulkerStack : shulkerBox.getInventory().getContents()) {
                            if (shulkerStack != null) {
                                if (shulkerStack.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > 50) {
                                    return itemStack;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public int getItemSlotFromInv(Player player, Material material) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR)
                continue;
            if (player.getInventory().getItem(i).getType() == material) {
                return i;
            }
        }
        return -1;
    }

    public void postPlace(World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, EnumDirection direction) {
        TileEntity tile = world.getTileEntity(blockposition);
        if (tile instanceof TileEntityDispenser) {
            world.setTypeAndData(blockposition, iblockdata.set(BlockDirectional.FACING, direction), 0);
        }
    }

    private void rotateTileEntity(TileEntity tile, CraftPlayer player) {
        EnumDirection direction = player.getHandle().getDirection();
        try {
            Method blockDataMethod = net.minecraft.server.v1_12_R1.Block.class.getDeclaredMethod("w", IBlockData.class);
            blockDataMethod.setAccessible(true);
            if (tile instanceof TileEntityDispenser) {
                BlockTileEntity blockTileEntity = (BlockTileEntity) tile.getBlock();
                BlockDispenser dispenser = (BlockDispenser) blockTileEntity;
                IBlockData data;
                IBlockData newData = dispenser.getBlockData().set(BlockDirectional.FACING, direction.opposite()).set(BlockStateBoolean.of("triggered"), false);
                blockDataMethod.invoke(dispenser, newData);
                data = dispenser.getPlacedState(tile.getWorld(), tile.getPosition(), direction.opposite(), 0.0F, 0.0F, 0.0F, 0, player.getHandle());
                postPlace(tile.getWorld(), tile.getPosition(), data, player.getHandle(), direction.opposite());
            }
            tile.update();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
