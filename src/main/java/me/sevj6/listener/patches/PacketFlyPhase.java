package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.PacketPlayInFlying;
import net.minecraft.server.v1_12_R1.PacketPlayInTeleportAccept;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author SevJ6
 * TODO: make this patch less dogshit LMAO
 */

public class PacketFlyPhase implements SevListener, Instance {
    private final List<Material> whitelist = Arrays.asList(Material.SIGN, Material.SIGN_POST, Material.WALL_SIGN, Material.GOLD_PLATE,
            Material.IRON_PLATE, Material.WOOD_PLATE, Material.STONE_PLATE, Material.BED, Material.BED_BLOCK, Material.SKULL,
            Material.TRAP_DOOR, Material.FENCE, Material.FENCE_GATE, Material.IRON_FENCE, Material.COBBLE_WALL, Material.BANNER,
            Material.STANDING_BANNER, Material.WALL_BANNER, Material.WOODEN_DOOR, Material.WOOD_DOOR, Material.BIRCH_DOOR, Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR, Material.IRON_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.STAINED_GLASS_PANE, Material.THIN_GLASS,
            Material.FENCE, Material.IRON_FENCE, Material.BIRCH_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE, Material.JUNGLE_FENCE,
            Material.NETHER_FENCE, Material.SPRUCE_FENCE, Material.PISTON_EXTENSION, Material.PISTON_MOVING_PIECE, Material.PISTON_BASE);
    private final Set<Player> set = new HashSet<>();

    private final Setting<Boolean> packetFlyPatch = Setting.getBoolean("packetfly_patch.enabled");

    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (packetFlyPatch.getValue()) {
            if (event.getPacket() instanceof PacketPlayInTeleportAccept) {
                Player player = event.getPlayer();
                ItemStack item = (player.getInventory().getItemInOffHand().getType() == Material.CHORUS_FRUIT) ? player.getEquipment().getItemInOffHand() : (player.getInventory().getItemInMainHand().getType() == Material.CHORUS_FRUIT) ? player.getEquipment().getItemInMainHand() : null;
                if (item != null) {
                    set.add(player);
                }
            }
        }
    }

    @SevHandler
    public void onMove(PacketEvent.ClientToServer event) {
        if (packetFlyPatch.getValue()) {
            if (event.getPacket() instanceof PacketPlayInFlying) {
                Player player = event.getPlayer();
                if (set.contains(player)) {
                    set.remove(player);
                    return;
                }
                PacketPlayInFlying packet = (PacketPlayInFlying) event.getPacket();
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (hasPos(packet)) {
                        if (checkPacket(packet, player)) {
                            cancel(event);
                            return;
                        }
                        Location originalLocation = player.getLocation();
                        Location packetLocation = new Location(
                                player.getWorld(),
                                packet.a(originalLocation.getX()),
                                packet.b(originalLocation.getY()),
                                packet.c(originalLocation.getZ())
                        );
                        if (originalLocation.getBlockX() != packetLocation.getBlockX() || originalLocation.getBlockZ() != packetLocation.getBlockZ()) {
                            Block blockTo = packetLocation.getBlock();
                            double y = packetLocation.getY();
                            int blockY = packetLocation.getBlockY();
                            Material faceType = blockTo.getLocation().clone().add(0, 1, 0).getBlock().getType();

                            Material matTo = blockTo.getType();

                            boolean isValidType = whitelist.contains(blockTo.getType()) || whitelist.contains(faceType) || (matTo != Material.WOODEN_DOOR
                                    && matTo != Material.IRON_DOOR && matTo != Material.FENCE && matTo != Material.THIN_GLASS
                                    && matTo != Material.STAINED_GLASS_PANE && matTo != Material.ACACIA_DOOR && matTo != Material.BIRCH_DOOR
                                    && matTo != Material.DARK_OAK_DOOR && matTo != Material.JUNGLE_DOOR && matTo != Material.SPRUCE_DOOR && matTo != Material.WOOD_DOOR
                                    && matTo != Material.ACACIA_FENCE && matTo != Material.BIRCH_FENCE && matTo != Material.IRON_FENCE && matTo != Material.DARK_OAK_FENCE
                                    && matTo != Material.JUNGLE_FENCE && matTo != Material.NETHER_FENCE && matTo != Material.PISTON_EXTENSION && matTo != Material.PISTON_MOVING_PIECE);

                            boolean isToBlockFull = (blockTo.getType().isSolid() || faceType.isSolid())
                                    && (y - blockY == 0.0 || y - blockY < 0.225);

                            if (isToBlockFull && !isValidType) {
                                cancel(event);
                            }
                        }
                    }
                });

            }
        }
    }

    private boolean hasPos(PacketPlayInFlying packet) {
        try {
            Field field = PacketPlayInFlying.class.getDeclaredField("hasPos");
            field.setAccessible(true);
            return (boolean) field.get(packet);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    private boolean checkPacket(PacketPlayInFlying packet, Player player) {
        Location originalLocation = player.getLocation();
        double originalY = originalLocation.getY();
        double packetY = packet.b(originalY);
        return Math.abs(packetY - originalY) > 25.0D;
    }

    private void cancel(PacketEvent event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        Location l = player.getLocation().clone();
        l.setYaw(player.getLocation().getYaw());
        l.setPitch(player.getLocation().getPitch());
        player.teleport(l);
    }
}
