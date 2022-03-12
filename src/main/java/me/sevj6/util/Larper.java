package me.sevj6.util;

import com.destroystokyo.paper.Title;
import me.sevj6.Impurity;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Larper {

    private final Player larper;

    public Larper(Player player) {
        this.larper = player;
    }

    public Player getLarper() {
        return larper;
    }

    public void crashLarper() {
        this.larper.spawnParticle(Particle.EXPLOSION_HUGE, larper.getLocation(), Integer.MAX_VALUE, 1, 1, 1);
    }

    public void earrape() {
        Arrays.stream(Sound.values()).forEach(sound -> getLarper().playSound(larper.getLocation(), sound, 100.0F, 50.0F));
        for (EntityEffect value : EntityEffect.values()) {
            larper.playEffect(value);
        }
    }

    public void giveSevereDementia() {
        larper.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10, 100, true, true));
        larper.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 100, true, true));
        larper.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10, 4, true, true));
        larper.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 10, 100, true, true));
    }

    public void threaten() {
        larper.sendTitle(new Title(ChatColor.DARK_RED + "I AM INSIDE YOUR WALLS", ChatColor.BOLD + "SevJ6 is COMING FOR YOU", 5, 60, 5));
        for (int i = 0; i < 30; i++) {
            MessageUtil.sendMessage(larper, "&4&lFUCK YOU. YOU ARE A LARPER");
        }
    }

    public void troll() {
        EntityPlayer ep = ((CraftPlayer) larper).getHandle();
        for (BlockPosition positionsInRadius : getPositionsInRadius(7)) {
            ep.playerConnection.sendPacket(new PacketPlayOutBlockBreakAnimation(1, positionsInRadius, 1));
            ep.playerConnection.sendPacket(new PacketPlayOutBlockChange(ep.world, positionsInRadius));
            ep.playerConnection.sendPacket(new PacketPlayOutExplosion(positionsInRadius.getX(), positionsInRadius.getY(), positionsInRadius.getZ(), 50.0F, getPositionsInRadius(3), new Vec3D(positionsInRadius)));
            ep.enderTeleportTo(positionsInRadius.getX(), positionsInRadius.getY(), positionsInRadius.getZ());
            larper.playSound(larper.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100.0F, 1.0F);
        }
        earrape();
        threaten();
        giveSevereDementia();
        Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
            Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), () -> {
                MessageUtil.sendMessage(larper, ChatColor.GOLD + "YOUR PC WILL NOW EXPLODE. YOUR PC WILL NOW EXPLODE. YOUR PC WILL NOW EXPLODE. YOUR PC WILL NOW EXPLODE. ");
                larper.playSound(larper.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100.0F, 1.0F);
                Bukkit.getScheduler().runTaskLater(Impurity.getPlugin(), this::crashLarper, 20L);
            }, (20L * 5L));
        }, 35L);
    }

    public List<BlockPosition> getPositionsInRadius(int radius) {
        Location location = larper.getLocation();
        List<BlockPosition> list = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    list.add(new BlockPosition(x, y, z));
                }
            }
        }
        return list;
    }
}
