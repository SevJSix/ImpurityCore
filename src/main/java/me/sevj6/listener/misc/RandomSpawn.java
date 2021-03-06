package me.sevj6.listener.misc;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.util.MessageUtil;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.fileutil.Setting;
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
import java.util.logging.Level;

/**
 * @author SevJ6
 */

public class RandomSpawn implements Listener, Instance {

    private final Setting<Boolean> enabled = Setting.getBoolean("random_spawn.enabled");
    private final Setting<String> worldName = Setting.getString("random_spawn.world");
    private final Setting<Integer> range = Setting.getInt("random_spawn.range");

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        if (enabled.getValue()) {
            if (event.isBedSpawn()) return;
            doTeleport(event.getPlayer(), true, event);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        boolean isInsideChunkban = PlayerUtil.checkForTooLargeNBTAroundPlayer(player);
        if (isInsideChunkban) {
            Impurity.getPlugin().getLogger().log(Level.WARNING, "Possible chunkban at " + player.getLocation());
        }

        if (!player.hasPlayedBefore()) {
            if (enabled.getValue()) {
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
        int x = ThreadLocalRandom.current().nextInt(-this.range.getValue(), this.range.getValue()), z = ThreadLocalRandom.current().nextInt(-this.range.getValue(), this.range.getValue());
        World world = Bukkit.getWorld(this.worldName.getValue());
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
