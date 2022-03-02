package me.sevj6.event.events;

import me.sevj6.event.bus.Cancellable;
import me.sevj6.event.bus.Event;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PlayerPlaceCrystalEvent extends Event implements Cancellable {

    private final Player player;
    private final BlockPosition blockPos;
    private final EntityEnderCrystal enderCrystal;
    private final EnumHand hand;
    public World world;
    private boolean cancelled;

    public PlayerPlaceCrystalEvent(Player player, BlockPosition blockPos, EnumHand hand) {
        this.player = player;
        this.world = ((CraftWorld) player.getWorld()).getHandle();
        this.blockPos = blockPos;
        this.hand = hand;
        this.enderCrystal = new Location(player.getWorld(), blockPos.getX(), (blockPos.getY() + 1), blockPos.getZ())
                .getNearbyEntitiesByType(EnderCrystal.class, 0.5, 1, 0.5)
                .stream().map(crystal -> ((CraftEnderCrystal) crystal).getHandle()).findAny().orElse(null);
    }

    public EnumHand getHand() {
        return hand;
    }

    public EntityEnderCrystal getEnderCrystal() {
        return enderCrystal;
    }

    public void explodeCrystal() {
        if (enderCrystal != null) {
            enderCrystal.damageEntity(DamageSource.playerAttack(((CraftPlayer) player).getHandle()), 5.0F);
        }
    }

    public void addCrystal(boolean explode, boolean deletePlacedCrystal) {
        if (deletePlacedCrystal && this.enderCrystal != null) this.enderCrystal.die();
        EntityEnderCrystal newCrystal = new EntityEnderCrystal(world, (this.blockPos.getX() + 0.5), (this.blockPos.getY() + 1), (this.blockPos.getZ() + 0.5));
        newCrystal.setShowingBottom(false);
        newCrystal.setBeamTarget(null);
        world.addEntity(newCrystal, CreatureSpawnEvent.SpawnReason.CUSTOM);
        if (explode) newCrystal.damageEntity(DamageSource.playerAttack(((CraftPlayer) player).getHandle()), 5.0F);
    }

    public void addCrystalToPos(BlockPosition blockPos, boolean explode, boolean deletePlacedCrystal) {
        if (deletePlacedCrystal && this.enderCrystal != null) this.enderCrystal.die();
        EntityEnderCrystal newCrystal = new EntityEnderCrystal(world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        newCrystal.setShowingBottom(false);
        newCrystal.setBeamTarget(null);
        world.addEntity(newCrystal, CreatureSpawnEvent.SpawnReason.CUSTOM);
        if (explode) newCrystal.damageEntity(DamageSource.playerAttack(((CraftPlayer) player).getHandle()), 5.0F);
    }

    public Player getPlayer() {
        return player;
    }

    public BlockPosition getBlockPos() {
        return blockPos;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }
}
