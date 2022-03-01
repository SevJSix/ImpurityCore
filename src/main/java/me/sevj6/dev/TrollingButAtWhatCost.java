package me.sevj6.dev;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.CrystalUtil;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class TrollingButAtWhatCost implements SevListener, Instance {

    private final List<Material> placeable = Arrays.asList(org.bukkit.Material.BEDROCK, Material.OBSIDIAN);

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (!(event.getPacket() instanceof PacketPlayInUseItem)) return;
        PacketPlayInUseItem packet = (PacketPlayInUseItem) event.getPacket();
        Player player = event.getPlayer();
        BlockPosition position = packet.a();
        Location placeLoc = new Location(player.getWorld(), position.getX(), position.getY(), position.getZ());
        Block block = placeLoc.getBlock();
        if (block == null) return;
        if (!placeable.contains(block.getType())) return;
        if (!CrystalUtil.canPlace(block.getLocation(), player)) return;
        ItemStack stack = (packet.c() == EnumHand.MAIN_HAND) ? player.getInventory().getItemInMainHand() : player.getInventory().getItemInOffHand();
        if (stack == null) return;
        if (stack.getType() != Material.END_CRYSTAL) return;
        Location location = block.getLocation();
        CraftWorld world = (CraftWorld) location.getWorld();
        Bukkit.getScheduler().runTask(plugin, () -> {
            EntityEnderCrystal crystal = (EntityEnderCrystal) world.createEntity(location.clone().add(0.5, 1.0, 0.5), EnderCrystal.class);
            crystal.setShowingBottom(false);
            crystal.setBeamTarget(null);
            world.addEntity(crystal, CreatureSpawnEvent.SpawnReason.DEFAULT);
            crystal.damageEntity(DamageSource.GENERIC, 1.0F);
            crystal.die();
        });
        if (player.getGameMode() == GameMode.SURVIVAL) stack.subtract();
    }
}
