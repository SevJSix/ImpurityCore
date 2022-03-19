package me.sevj6.util;

import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import me.sevj6.Instance;
import me.sevj6.event.bus.NettyInjector;
import me.sevj6.util.fileutil.ConfigManager;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Utils implements Instance {

    private static final DecimalFormat format = new DecimalFormat("#.##");

    public static String[] getAllowedCommands() {
        return config.getStringList("CommandWhitelist.command-list").stream().map(s -> "/" + s).toArray(String[]::new);
    }

    public static List<String> getAllowedCommandsAsListWithNoPrefix() {
        return config.getStringList("CommandWhitelist.command-list");
    }

    public static boolean is32k(ItemStack itemStack) {
        return itemStack.getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > 50);
    }

    public static List<EntityType> neutralEntities = Arrays.asList(EntityType.LLAMA, EntityType.WOLF, EntityType.IRON_GOLEM, EntityType.BAT, EntityType.OCELOT, EntityType.CHICKEN, EntityType.COW, EntityType.DONKEY, EntityType.HORSE, EntityType.MUSHROOM_COW, EntityType.MULE, EntityType.PARROT, EntityType.PIG, EntityType.RABBIT, EntityType.SHEEP, EntityType.SKELETON_HORSE, EntityType.SNOWMAN, EntityType.SQUID, EntityType.VILLAGER);
    public static List<EntityType> hostileEntities = Arrays.asList(EntityType.CAVE_SPIDER, EntityType.ENDERMAN, EntityType.SPIDER, EntityType.BLAZE, EntityType.CREEPER, EntityType.ELDER_GUARDIAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HUSK, EntityType.MAGMA_CUBE, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.SLIME, EntityType.STRAY, EntityType.VEX, EntityType.VINDICATOR, EntityType.WITCH, EntityType.ZOMBIE, EntityType.ZOMBIE_VILLAGER);

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

    public static Integer getMin(Integer[] inputArray) {
        int minValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] < minValue) {
                minValue = inputArray[i];
            }
        }
        return minValue;
    }

    public static Integer getMax(Integer[] inputArray) {
        int maxValue = inputArray[0];
        for (int i = 1; i < inputArray.length; i++) {
            if (inputArray[i] > maxValue) {
                maxValue = inputArray[i];
            }
        }
        return maxValue;
    }

    public static String getFormattedPlaytime(UUID player) {
        if (ConfigManager.configManager.getPlaytimes().contains(player.toString())) {
            return Utils.getFormattedInterval(ConfigManager.configManager.getPlaytimes().getLong(player.toString()));
        } else return "none";
    }

    public static void fixLightUpdateQueueing() {
        int time = 300;
        String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            Class<?> lightQueue = Class.forName("net.minecraft.server." + ver + ".PaperLightingQueue");
            Field maxTimeF = lightQueue.getDeclaredField("MAX_TIME");
            maxTimeF.setAccessible(true);
            modifiersField.setInt(maxTimeF, maxTimeF.getModifiers() & ~Modifier.FINAL);
            maxTimeF.set(null, (long) (time / 20 * 1.15));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}