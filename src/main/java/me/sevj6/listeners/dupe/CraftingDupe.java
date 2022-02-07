package me.sevj6.listeners.dupe;

import io.netty.util.internal.ConcurrentSet;
import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.PacketPlayInAutoRecipe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

public class CraftingDupe implements NMSPacketListener, Listener {

    private final ConcurrentSet<Player> players = new ConcurrentSet<>();
    private ConcurrentSet<Player> copy;

    public CraftingDupe() {
        copy = new ConcurrentSet<>();
    }

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInAutoRecipe) {
            Player player = event.getPlayer();
            if (player.getOpenInventory().getType() == InventoryType.CRAFTING) {
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> players.add(player));
                copy = players;
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!copy.contains(player)) return;
        int random = ThreadLocalRandom.current().nextInt(63, 127);
        ItemStack itemPickedUp = event.getItem().getItemStack();
        for (ItemStack itemStack : player.getInventory()) {
            if (itemPickedUp == itemStack) {
                itemStack.setAmount(random);
                player.updateInventory();
                players.remove(player);
                break;
            }
        }
    }
}
