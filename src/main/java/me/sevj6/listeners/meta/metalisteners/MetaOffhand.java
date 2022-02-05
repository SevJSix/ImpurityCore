package me.sevj6.listeners.meta.metalisteners;

import me.sevj6.Impurity;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class MetaOffhand implements Listener {


    private boolean isPlayerHoldingCrystalInOffhand(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getHand() == EquipmentSlot.OFF_HAND) {
            return player.getInventory().getItemInOffHand() != null && player.getInventory().getItemInOffHand().getType() == Material.END_CRYSTAL;
        }
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (Impurity.getPlugin().getMetaManager().getSettings().isMainhandOnly()) {
            if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.BEDROCK || event.getClickedBlock().getType() == Material.OBSIDIAN)) {
                if (!isPlayerHoldingCrystalInOffhand(event)) return;
                event.setCancelled(true);
            }
        }
    }
}
