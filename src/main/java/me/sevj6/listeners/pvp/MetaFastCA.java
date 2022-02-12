package me.sevj6.listeners.pvp;

import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import net.minecraft.server.v1_12_R1.EntityHuman;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class MetaFastCA implements Listener {

    private boolean isPlayerHoldingCrystal(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.HAND) {
            return player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.END_CRYSTAL;
        } else if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == Material.END_CRYSTAL;
        }
        return false;
    }

    private boolean isValid(PlayerInteractEvent event) {
        return event.getPlayer() != null && event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                (event.getClickedBlock().getType() == Material.OBSIDIAN || event.getClickedBlock().getType() == Material.BEDROCK)
                && isPlayerHoldingCrystal(event);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isValid(event)) return;
        Location crystalPos = event.getClickedBlock().getLocation().clone().add(0, 1, 0);
        EnderCrystal first = crystalPos.getNearbyEntitiesByType(EnderCrystal.class, 0.5, 0.5, 0.5).stream().findFirst().orElse(null);
        if (first == null) return;
        EntityEnderCrystal crystal = ((CraftEnderCrystal) first).getHandle();
        EntityHuman human = ((CraftPlayer) event.getPlayer()).getHandle();
        crystal.damageEntity(DamageSource.playerAttack(human), 1.0F);
    }
}
