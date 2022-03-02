package me.sevj6.util;

import me.sevj6.Impurity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Hopper;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class ServersideUtil {

    public static final int PLACE_RANGE = 3;

    public static Location redstonePos(Location dispenserLocation, EnumDirection direction) {
        if (isAir(clone(dispenserLocation, 0, 1, 0))) return clone(dispenserLocation, 0, 1, 0);

        switch (direction) {
            case NORTH:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
            case SOUTH:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                }
                break;
            case WEST:
                if (isAir(clone(dispenserLocation, 1, 0, 0))) {
                    return clone(dispenserLocation, 1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
            case EAST:
                if (isAir(clone(dispenserLocation, -1, 0, 0))) {
                    return clone(dispenserLocation, -1, 0, 0);
                } else if (isAir(clone(dispenserLocation, 0, 0, -1))) {
                    return clone(dispenserLocation, 0, 0, -1);
                } else if (isAir(clone(dispenserLocation, 0, 0, 1))) {
                    return clone(dispenserLocation, 0, 0, 1);
                }
                break;
        }
        return null;
    }

    public static boolean isAir(Location location) {
        return location.getBlock().getType() == Material.AIR;
    }

    public static Location clone(Location location, int x, int y, int z) {
        return location.clone().add(x, y, z);
    }

    public static Location getHopperLocation(Location dispenserLocation, EnumDirection direction) {
        switch (direction) {
            case EAST:
                return dispenserLocation.clone().add(1, -1, 0);
            case WEST:
                return dispenserLocation.clone().add(-1, -1, 0);
            case NORTH:
                return dispenserLocation.clone().add(0, -1, -1);
            case SOUTH:
                return dispenserLocation.clone().add(0, -1, 1);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    // this method is slightly different because there was an issue with getting the exact shulker position
    public static Location getShulkerLocation(Location dispenserLocation, EnumDirection direction) {
        double toAdd;
        double origX = dispenserLocation.getX();
        double origY = dispenserLocation.getY();
        double origZ = dispenserLocation.getZ();
        World world = dispenserLocation.getWorld();
        Location shulkerLoc;
        switch (direction) {
            case EAST:
            case WEST:
                toAdd = origX + 0.5;
                shulkerLoc = new Location(world, toAdd, origY, origZ);
                break;
            case NORTH:
            case SOUTH:
                toAdd = origZ + 0.5;
                shulkerLoc = new Location(world, origX, origY, toAdd);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
        return shulkerLoc;
    }

    public static BlockPosition getAutoPlace32kPos(Player player) {
        Location playerInitialLocation = player.getLocation();
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EnumDirection facing = entityPlayer.getDirection();
        EnumDirection opposite = facing.opposite();
        List<BlockPosition> blockPositions = new ArrayList<>();

        for (int x = playerInitialLocation.getBlockX() - PLACE_RANGE; x <= playerInitialLocation.getBlockX() + PLACE_RANGE; x++) {
            for (int y = playerInitialLocation.getBlockY() - PLACE_RANGE; y <= playerInitialLocation.getBlockY() + PLACE_RANGE; y++) {
                for (int z = playerInitialLocation.getBlockZ() - PLACE_RANGE; z <= playerInitialLocation.getBlockZ() + PLACE_RANGE; z++) {
                    Location location = new Location(playerInitialLocation.getWorld(), x, y, z);
                    if (isValidPlaceLocation(location, opposite)) {
                        blockPositions.add(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                    }
                }
            }
        }
        return blockPositions.get(ThreadLocalRandom.current().nextInt(0, blockPositions.size()));
    }

    public static boolean isValidPlaceLocation(Location location, EnumDirection direction) {
        Location obsidian = location.clone().add(0, 1, 0);
        Location dispenser = obsidian.clone().add(0, 1, 0);
        Location redstone = redstonePos(dispenser, direction);
        Location hopper = getHopperLocation(dispenser, direction);
        Location shulker = hopper.clone().add(0, 1, 0);
        return location.getBlock().getType() != Material.AIR
                && obsidian.getBlock().getType() == Material.AIR
                && obsidian.getBlock().getType() != Material.REDSTONE_BLOCK
                && dispenser.getBlock().getType() == Material.AIR
                && (redstone != null && redstone.getBlock().getType() == Material.AIR)
                && hopper.getBlock().getType() == Material.AIR
                && shulker.getBlock().getType() == Material.AIR
                && location.getNearbyPlayers(1.5).isEmpty();
    }

    /**
     * Utils for placing 32ks
     */
    public static void placeAuto32k(Player player, BlockPosition placePos) {
        if (placePos == null) {
            MessageUtil.sendMessage(player, "&cInvalid block position!");
            return;
        }
        Block block = player.getWorld().getBlockAt(placePos.getX(), placePos.getY(), placePos.getZ());
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
        shulkerLocation = ServersideUtil.getShulkerLocation(dispenserLocation, opposite);
        redstoneLocation = ServersideUtil.redstonePos(dispenserLocation, opposite);
        hopperLocation = ServersideUtil.getHopperLocation(dispenserLocation, opposite);

        if (doPlaceObsidian) {
            player.getInventory().setHeldItemSlot(obsidianSlot);
            placeBlock(player, obsidianLocation, EnumDirection.UP);
            playSoundAtLocation(Sound.BLOCK_STONE_PLACE, obsidianLocation);
        }
        if (!isNotViablePlacePos(dispenserLocation)) {
            player.getInventory().setHeldItemSlot(dispenserSlot);
            placeBlock(player, dispenserLocation, opposite);
            playSoundAtLocation(Sound.BLOCK_STONE_PLACE, dispenserLocation);
            if (dispenserLocation.getBlock() == null) {
                player.getWorld().getBlockAt(dispenserLocation).setType(Material.DISPENSER);
            }
            TileEntityDispenser tileEntityDispenser = (TileEntityDispenser) entityPlayer.world.getTileEntity(new BlockPosition(dispenserLocation.getBlockX(), dispenserLocation.getBlockY(), dispenserLocation.getBlockZ()));
            rotateDispenser(tileEntityDispenser, (CraftPlayer) player);
            IInventory dispInv = ((CraftInventory) ((Dispenser) dispenserLocation.getBlock().getState()).getInventory()).getInventory();
            entityPlayer.openContainer(dispInv);
            dispInv.setItem(5, CraftItemStack.asNMSCopy(get32kShulkerFromInv(player)));
            player.getInventory().getItem(getItemSlotFromInv(player, get32kShulkerFromInv(player).getType())).subtract();
            entityPlayer.closeInventory();
        } else {
            MessageUtil.sendMessage(player, "&cInvalid dispenser location!");
            return;
        }
        if (redstoneLocation != null && !isNotViablePlacePos(redstoneLocation)) {
            player.getInventory().setHeldItemSlot(redstoneSlot);
            placeBlock(player, redstoneLocation, EnumDirection.UP);
            playSoundAtLocation(Sound.BLOCK_METAL_PLACE, redstoneLocation);
        } else {
            MessageUtil.sendMessage(player, "&cInvalid redstone location!");
            return;
        }
        if (isNotViablePlacePos(hopperLocation)) {
            MessageUtil.sendMessage(player, "&cInvalid hopper location!");
            return;
        }

        int attempts = 0;
        while (true) {
            attempts++;
            if (shulkerLocation.getBlock().getType() != Material.AIR) {
                Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                    player.getInventory().setHeldItemSlot(hopperSlot);
                    placeBlock(player, hopperLocation, opposite);
                    playSoundAtLocation(Sound.BLOCK_METAL_PLACE, hopperLocation);
                    Hopper hopper = (Hopper) hopperLocation.getBlock().getState();
                    IInventory inventory = ((CraftInventory) hopper.getInventory()).getInventory();
                    entityPlayer.openContainer(inventory);
                }, 4L);
                break;
            }
            if (attempts > 1000) {
                break;
            }
        }
    }

    public static boolean isNotViablePlacePos(Location location) {
        Block block = location.getWorld().getBlockAt(location);
        return block != null && (block.getType().isSolid() || block.getType().isOccluding());
    }

    public static int getItemSlotFromInv(Player player, Material material) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (player.getInventory().getItem(i) == null || player.getInventory().getItem(i).getType() == Material.AIR)
                continue;
            if (player.getInventory().getItem(i).getType() == material) {
                return i;
            }
        }
        return -1;
    }

    public static org.bukkit.inventory.ItemStack get32kShulkerFromInv(Player player) {
        for (org.bukkit.inventory.ItemStack itemStack : player.getInventory().getContents()) {
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

    public static void playSoundAtLocation(Sound sound, Location location) {
        location.getNearbyPlayers(10).forEach(p -> p.playSound(location, sound, 1.2F, 0.8F));
    }

    public static void placeBlock(Player player, Location location, EnumDirection direction) {
        EntityHuman human = ((CraftPlayer) player).getHandle();
        human.getItemInMainHand().placeItem(human, human.world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), EnumHand.MAIN_HAND, direction, 255.0F, 255.0F, 255.0F);
    }

    public static void postPlace(net.minecraft.server.v1_12_R1.World world, BlockPosition blockposition, IBlockData iblockdata, EntityLiving entityliving, EnumDirection direction) {
        TileEntity tile = world.getTileEntity(blockposition);
        if (tile instanceof TileEntityDispenser) {
            world.setTypeAndData(blockposition, iblockdata.set(BlockDirectional.FACING, direction), 0);
        }
    }

    public static void rotateDispenser(TileEntity tile, CraftPlayer player) {
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
