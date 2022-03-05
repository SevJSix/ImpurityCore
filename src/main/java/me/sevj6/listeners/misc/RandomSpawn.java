package me.sevj6.listeners.misc;

import me.sevj6.util.MessageUtil;
import me.sevj6.util.ObjectChecker;
import me.sevj6.util.PluginUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author SevJ6
 */

public class RandomSpawn implements Listener {
    private final int range;
    private final String worldName;

    public RandomSpawn() {
        this.range = PluginUtil.config().getInt("RandomSpawn.spawn-range");
        this.worldName = PluginUtil.config().getString("RandomSpawn.world");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (PluginUtil.config().getBoolean("RandomSpawn.Enabled")) {
            if (event.isBedSpawn()) return;
            doTeleport(event.getPlayer(), true, event);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        ObjectChecker<Player> playerObjectChecker = new ObjectChecker<>(player);
        playerObjectChecker.check();
        playerObjectChecker.getNearbyItemDrops(20).forEach(itemStack -> {
            new ObjectChecker<>(itemStack).check();
        });

        if (!player.hasPlayedBefore()) {
            if (PluginUtil.config().getBoolean("RandomSpawn.Enabled")) {
                doTeleport(player, false, null);
                MessageUtil.sendMessage(player, "&aThis is your first time joining Impurity." +
                        "\n" +
                        "&aTo see all commands, type /help" +
                        "\n" +
                        "&aYou can join the discord here: &b" + MessageUtil.getDiscord());
            }
        }
    }

    private void doTeleport(Player player, boolean isRespawn, PlayerRespawnEvent event) {
        int attemptCount = 0;
        Location location = null;
        while (location == null) {
            attemptCount++;
            if (attemptCount == 300) {
                location = calcSpawnLocation(true);
            } else {
                location = calcSpawnLocation(false);
            }
        }
        if (!isRespawn || event == null) {
            player.teleport(location);
        } else {
            event.setRespawnLocation(location);
        }
    }

    private Location calcSpawnLocation(boolean lastAttempt) {
        int x = ThreadLocalRandom.current().nextInt(-this.range, this.range), z = ThreadLocalRandom.current().nextInt(-this.range, this.range);
        World world = Bukkit.getWorld(this.worldName);
        int y = world.getHighestBlockYAt(x, z);
        if (!lastAttempt) {
            Block blockAt = world.getBlockAt(x, y, z);
            if (blockAt.getType() == Material.LAVA
                    || blockAt.getType() == Material.STATIONARY_LAVA
                    || blockAt.getType() == Material.WATER
                    || blockAt.getType() == Material.STATIONARY_WATER
                    || blockAt.getType() == Material.CACTUS
                    || blockAt.getType() == Material.AIR) return null;
        }
        return new Location(world, x, y, z);
    }
}
