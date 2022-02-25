package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Blink32kTeleport implements NMSPacketListener, Listener {

    public static final HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.HOPPER) return;
        Player player = (Player) event.getPlayer();
        put(player, event.getInventory(), inventoryHashMap.containsKey(player));
    }

    public void put(Player player, Inventory inventory, boolean replace) {
        if (replace) {
            Blink32kTeleport.inventoryHashMap.replace(player, inventory);
        } else {
            Blink32kTeleport.inventoryHashMap.put(player, inventory);
        }
        Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
            Blink32kTeleport.inventoryHashMap.remove(player);
        }, (20L * 9L));
    }

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
            if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                Player player = event.getPlayer();
                Location attackerLocation = player.getLocation();
                Entity entity = packet.a(((CraftWorld) attackerLocation.getWorld()).getHandle());
                if (entity instanceof EntityPlayer) {
                    EntityPlayer target = (EntityPlayer) entity;
                    EntityPlayer attacker = ((CraftPlayer) player).getHandle();
                    Location targetLocation = new Location(attackerLocation.getWorld(), target.locX, target.locY, target.locZ);

                    // Check for blink 32k tp
                    if (inventoryHashMap.containsKey(player) && inventoryHashMap.get(player).getType() == InventoryType.HOPPER) {
                        Inventory inventory = inventoryHashMap.get(player);
                        double attackerDistanceToHopper = inventory.getLocation().distance(player.getLocation());
                        double victimDistanceToHopper = inventory.getLocation().distance(targetLocation);
                        boolean tooFar = attackerDistanceToHopper > 8.0D
                                || victimDistanceToHopper > 12.5D
                                || !attacker.activeContainer.checkReachable
                                || attackerLocation.distance(targetLocation) > 7.5;
                        if (tooFar) {
                            event.setCancelled(true);
                            handleTask(() -> {
                                if (attacker.activeContainer != null) player.closeInventory();
                                revert(player);
                            });
                            inventoryHashMap.remove(player);
                            return;
                        }
                    }

                    // Stop free roaming with 32ks
                    if (player.getOpenInventory().getType() != InventoryType.HOPPER && isPlayerHoldingIllegalSuperWeapon(player)) {
                        event.setCancelled(true);
                        handleTask(() -> {
                            if (attacker.activeContainer != null) player.closeInventory();
                            revert(player);
                        });
                        return;
                    }

                    // Stop people from flying with an elytra with 32ks
                    if (player.isGliding() && isPlayerHoldingIllegalSuperWeapon(player)) {
                        event.setCancelled(true);
                        handleTask(() -> {
                            if (attacker.activeContainer != null) player.closeInventory();
                            revert(player);
                            player.setGliding(false);
                        });
                    }
                }
            }
        }
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }

    public ItemStack getActiveItem(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    public boolean isPlayerHoldingIllegalSuperWeapon(Player player) {
        if (getActiveItem(player) == null) return false;
        return getActiveItem(player).getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > 50);
    }

    public void revert(Player player) {
        ItemStack itemStack = getActiveItem(player);
        if (itemStack == null) return;
        itemStack.getEnchantments().entrySet().stream().filter(e -> e.getValue() > e.getKey().getMaxLevel()).forEach(e -> {
            itemStack.removeEnchantment(e.getKey());
            itemStack.addEnchantment(e.getKey(), e.getKey().getMaxLevel());
        });
    }
}
