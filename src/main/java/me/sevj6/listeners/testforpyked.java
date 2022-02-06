package me.sevj6.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class testforpyked implements Listener {

    private HashMap<Inventory, Player> inventoryHashMap = new HashMap<>();

    @EventHandler
    public void test(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            Inventory inv = Bukkit.createInventory(null, 9);
            inv.setItem(0, new ItemStack(Material.IRON_BLOCK));
            inv.getItem(0).getItemMeta().setDisplayName("Player Count: " + Bukkit.getOnlinePlayers().size());
            inv.getItem(0).setItemMeta(inv.getItem(0).getItemMeta());
            if (!event.getPlayer().getOpenInventory().getBottomInventory().equals(inv)) {
                event.getPlayer().openInventory(inv);
                inventoryHashMap.putIfAbsent(inv, event.getPlayer());
            }
        }
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (inventoryHashMap.get(event.getInventory()).equals(event.getPlayer())) {
            inventoryHashMap.remove(event.getInventory());
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        inventoryHashMap.forEach((inventory, player) -> {
            inventory.setItem(0, new ItemStack(Material.IRON_BLOCK));
            inventory.getItem(0).getItemMeta().setDisplayName("Player Count: " + Bukkit.getOnlinePlayers().size());
            inventory.getItem(0).setItemMeta(inventory.getItem(0).getItemMeta());
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        inventoryHashMap.forEach((inventory, player) -> {
            inventory.setItem(0, new ItemStack(Material.IRON_BLOCK));
            inventory.getItem(0).getItemMeta().setDisplayName("Player Count: " + Bukkit.getOnlinePlayers().size());
            inventory.getItem(0).setItemMeta(inventory.getItem(0).getItemMeta());
        });
    }

}
