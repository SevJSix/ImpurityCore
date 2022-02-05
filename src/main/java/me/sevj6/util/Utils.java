package me.sevj6.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.sevj6.Instance;
import me.sevj6.event.listener.NettyInjector;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class Utils implements Instance {

    private static final DecimalFormat format = new DecimalFormat("#.##");

    public static String formatLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        return "&r&b" + world.getName()
                + " &r&3X:&r&b " + format.format(x)
                + " &r&3Y:&r&b " + format.format(y)
                + " &r&3Z:&r&b " + format.format(z);
    }

    public static void inject(Player player) {
        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName().concat("PL"), new NettyInjector(player));
    }

    public static void removeHook(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName().concat("PL"));
        });
    }

    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    public static double getRawTps() {
        return Bukkit.getTPS()[0];
    }

    public static int countBlockPerChunk(Chunk chunk, Material material) {
        int count = 0;
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                for (int y = 0; y < 256; ++y) {
                    if (chunk.getBlock(x, y, z).getType() != material) continue;
                    ++count;
                }
            }
        }
        return count;
    }

}