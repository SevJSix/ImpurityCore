package me.sevj6.listener.pvp;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PlayerServerSide32kEvent;
import me.sevj6.listener.patches.SuperweaponExploits;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.ServersideUtil;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class Auto32k implements SevListener, Listener {

    public static final HashMap<Player, Inventory> inventoryHashMap = new HashMap<>();
    private final Setting<Boolean> serverSideA32k = Setting.getBoolean("serverside_auto32k.enabled");
    private final Setting<Boolean> whitelistOnly = Setting.getBoolean("serverside_auto32k.whitelist_only");
    private final Setting<List<String>> whitelist = Setting.getStringList("serverside_auto32k.whitelist");

    @SevHandler
    public void onAuto32k(PlayerServerSide32kEvent event) {
//        if (serverSideA32k.getValue()) {
//            if (whitelistOnly.getValue()) {
//                if (whitelist.getValue().contains(event.getPlayer().getName())) {
        doPlace(event);
//                }
//            } else {
//                doPlace(event);
//            }
    }
//    }

    private void doPlace(PlayerServerSide32kEvent event) {
        if (event.getPlacePos() == null) {
            event.setCancelled(true);
            MessageUtil.sendMessage(event.getPlayer(), "&cInvalid BlockPos");
        } else {
            handleTask(() -> {
                ServersideUtil.placeAuto32k(event.getPlayer(), event.getPlacePos());
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
