package me.sevj6.dev.serversided;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
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
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author SevJ6
 * @since 12/29/21 / 2:57 AM
 */
public class ServersidedAuto32k {

    public void doPlace(Player player, BlockPosition pos) {
        int i = 0;
        if (pos == null) {
            MessageUtil.sendMessage(player, "&cInvalid block position!");
            return;
        }
        Block block = player.getWorld().getBlockAt(pos.getX(), pos.getY(), pos.getZ());
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        EnumDirection facing = entityPlayer.getDirection();
        Location looking = block.getLocation();
        EnumDirection opposite = facing.opposite();
        int initalHeldSlot = player.getInventory().getHeldItemSlot();

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

        ItemStack sword = Arrays.stream(
                ((ShulkerBox) ((BlockStateMeta) get32kShulkerFromInv(player).getItemMeta()).getBlockState()).getInventory().getContents())
                .filter(itemStack -> itemStack != null && itemStack.getType() != Material.AIR)
                .findFirst()
                .orElse(Utils.gen32k()
                );

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
        shulkerLocation = CustomPayload.getShulkerLocation(dispenserLocation, opposite);
        redstoneLocation = CustomPayload.redstonePos(dispenserLocation, opposite);
        hopperLocation = CustomPayload.getHopperLocation(dispenserLocation, opposite);

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
            rotateTileEntity(tileEntityDispenser, (CraftPlayer) player);
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
                player.getInventory().setHeldItemSlot(hopperSlot);
                placeBlock(player, hopperLocation, opposite);
                playSoundAtLocation(Sound.BLOCK_METAL_PLACE, hopperLocation);
                Hopper hopper = (Hopper) hopperLocation.getBlock().getState();
                IInventory inventory = ((CraftInventory) hopper.getInventory()).getInventory();
                entityPlayer.openContainer(inventory);
                player.getInventory().setHeldItemSlot(initalHeldSlot);
                sword.setAmount(1);
                setHandItem((CraftPlayer) player, sword);
                break;
            }
            if (attempts > 1000) {
                break;
            }
        }
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

    public void playSoundAtLocation(Sound sound, Location location) {
        location.getNearbyPlayers(10).forEach(p -> p.playSound(location, sound, 1.2F, 0.8F));
    }

    public void placeBlock(Player player, Location location, EnumDirection direction) {
        EntityHuman human = ((CraftPlayer) player).getHandle();
        human.getItemInMainHand().placeItem(human, human.world, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()), EnumHand.MAIN_HAND, direction, 255.0F, 255.0F, 255.0F);
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
