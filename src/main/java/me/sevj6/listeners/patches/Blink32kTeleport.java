package me.sevj6.listeners.patches;

import me.sevj6.Impurity;
import me.sevj6.event.NMSEventHandler;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.event.events.PacketEvent;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Blink32kTeleport implements NMSPacketListener {

    @NMSEventHandler
    public void onPacket(PacketEvent.Incoming event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
            if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) {
                Bukkit.getScheduler().runTask(Impurity.getPlugin(), () -> {
                    check(event);
                });
            }
        }
    }

    public void check(PacketEvent.Incoming event) {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Entity entity = packet.a(((CraftWorld) location.getWorld()).getHandle());
        if (entity instanceof EntityPlayer) {
            EntityPlayer target = (EntityPlayer) entity;
            EntityPlayer attacker = ((CraftPlayer) player).getHandle();
            if ((!(attacker.activeContainer instanceof ContainerHopper) || !attacker.activeContainer.getBukkitView().getType().equals(InventoryType.HOPPER)) && isPlayerHoldingIllegalSuperWeapon(player, packet)) {
                if (attacker.activeContainer != null) player.closeInventory();
                revert(player, packet);
                event.setCancelled(true);
                return;
            }
            if (player.isGliding() || isPlayerHoldingIllegalSuperWeapon(player, packet)) {
                if (attacker.activeContainer != null) player.closeInventory();
                revert(player, packet);
                player.setGliding(false);
                event.setCancelled(true);
                return;
            }
            Location targetLocation = new Location(location.getWorld(), target.locX, target.locY, target.locZ);
            if (location.distance(targetLocation) > 7.5 && isPlayerHoldingIllegalSuperWeapon(player, packet)) {
                if (attacker.activeContainer != null) player.closeInventory();
                revert(player, packet);
                event.setCancelled(true);
                return;
            }
            if (attacker.activeContainer != null) {
                Inventory topInv = attacker.activeContainer.getBukkitView().getTopInventory();
                Inventory bottomInv = attacker.activeContainer.getBukkitView().getBottomInventory();
                double topInvDist = topInv.getLocation().distance(player.getLocation());
                double bottomInvDist = bottomInv.getLocation().distance(player.getLocation());
                double topInvDistToTargetEntity = topInv.getLocation().distance(targetLocation);
                double bottomInvDistToTargetEntity = bottomInv.getLocation().distance(targetLocation);
                boolean tooFar = topInvDist > 7.5 || bottomInvDist > 7.5 || topInvDistToTargetEntity > 10.0 || bottomInvDistToTargetEntity > 10.0 || !attacker.activeContainer.checkReachable;
                if (tooFar) {
                    if (attacker.activeContainer != null) player.closeInventory();
                    revert(player, packet);
                    event.setCancelled(true);
                }
            }
        }
    }

    public ItemStack getItemBasedFromUsedHand(Player player, PacketPlayInUseEntity packet) {
        return (packet.b().equals(EnumHand.OFF_HAND)) ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
    }

    public boolean isPlayerHoldingIllegalSuperWeapon(Player player, PacketPlayInUseEntity packet) {
        if (getItemBasedFromUsedHand(player, packet) == null) return false;
        return getItemBasedFromUsedHand(player, packet).getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > 50);
    }

    public void revert(Player player, PacketPlayInUseEntity packet) {
        ItemStack itemStack = getItemBasedFromUsedHand(player, packet);
        if (itemStack == null) return;
        itemStack.getEnchantments().entrySet().stream().filter(e -> e.getValue() > 50).forEach(e -> {
            itemStack.removeEnchantment(e.getKey());
            itemStack.addEnchantment(e.getKey(), e.getKey().getMaxLevel());
        });
    }
}
