package me.sevj6.listeners.pvp;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PlayerAttemptUse32kEvent;
import me.sevj6.event.events.PlayerServerSide32kEvent;
import me.sevj6.listeners.patches.SuperweaponExploits;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.ServersideUtil;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Auto32k implements SevListener, Listener {

    public static final HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();

    @SevHandler
    public void onAuto32k(PlayerServerSide32kEvent event) {
        if (event.getPlacePos() == null) {
            event.setCancelled(true);
            MessageUtil.sendMessage(event.getPlayer(), "&cInvalid BlockPos");
        } else {
            handleTask(() -> {
                ServersideUtil.placeAuto32k(event.getPlayer(), event.getPlacePos());
            });
        }
    }

    @SevHandler
    public void on32kHit(PlayerAttemptUse32kEvent event) {
        Player player = event.getPlayer();
        EntityPlayer attacker = ((CraftPlayer) player).getHandle();
        Player victim = event.getVictim();
        EnumHand hand = event.getHand();

        // Check for blink 32k tp
        if (inventoryHashMap.containsKey(player) && inventoryHashMap.get(player).getType() == InventoryType.HOPPER) {
            Inventory inventory = inventoryHashMap.get(player);
            double attackerDistanceToHopper = inventory.getLocation().distance(player.getLocation());
            double victimDistanceToHopper = inventory.getLocation().distance(victim.getLocation());
            boolean tooFar = attackerDistanceToHopper > 8.0D
                    || victimDistanceToHopper > 12.5D
                    || !attacker.activeContainer.checkReachable
                    || event.getAttackRange() > 6.5;
            if (tooFar) {
                event.setCancelled(true);
                handleTask(() -> {
                    if (attacker.activeContainer != null) player.closeInventory();
                    revert(player, hand);
                });
                inventoryHashMap.remove(player);
                return;
            }
        }

        // Stop free roaming with 32ks
        if (player.getOpenInventory().getType() != InventoryType.HOPPER) {
            event.setCancelled(true);
            handleTask(() -> {
                if (attacker.activeContainer != null) player.closeInventory();
                revert(player, hand);
            });
            return;
        }

        // Stop people from flying with an elytra with 32ks
        if (player.isGliding()) {
            event.setCancelled(true);
            handleTask(() -> {
                if (attacker.activeContainer != null) player.closeInventory();
                revert(player, hand);
                player.setGliding(false);
            });
        }
    }

    public void handleTask(Runnable task) {
        Bukkit.getScheduler().runTask(Impurity.getPlugin(), task);
    }

    public ItemStack getActiveItem(Player player, EnumHand hand) {
        return (hand == EnumHand.OFF_HAND) ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
    }

    public void revert(Player player, EnumHand hand) {
        ItemStack itemStack = getActiveItem(player, hand);
        if (itemStack == null) return;
        itemStack.getEnchantments().entrySet().stream().filter(e -> e.getValue() > e.getKey().getMaxLevel()).forEach(e -> {
            itemStack.removeEnchantment(e.getKey());
            itemStack.addEnchantment(e.getKey(), e.getKey().getMaxLevel());
        });
    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.HOPPER) return;
        Player player = (Player) event.getPlayer();
        put(player, event.getInventory(), inventoryHashMap.containsKey(player));
    }

    public void put(Player player, Inventory inventory, boolean replace) {
        if (replace) {
            SuperweaponExploits.inventoryHashMap.replace(player, inventory);
        } else {
            SuperweaponExploits.inventoryHashMap.put(player, inventory);
        }
        Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
            SuperweaponExploits.inventoryHashMap.remove(player);
        }, (20L * 9L));
    }
}
