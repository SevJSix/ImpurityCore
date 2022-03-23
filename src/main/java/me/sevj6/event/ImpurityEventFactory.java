package me.sevj6.event;

import me.sevj6.Impurity;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.*;
import me.sevj6.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class ImpurityEventFactory implements Listener, SevListener {

    public static Field opCode;
    public static Field entityId;
    public static Field velocityEntityId;

    static {
        try {
            opCode = PacketPlayOutEntityStatus.class.getDeclaredField("b");
            opCode.setAccessible(true);
            entityId = PacketPlayOutEntityStatus.class.getDeclaredField("a");
            entityId.setAccessible(true);
            velocityEntityId = PacketPlayOutEntityVelocity.class.getDeclaredField("a");
            velocityEntityId.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @author SevJ6
     * Listener to post ServerSide32kEvent
     */
    @SevHandler
    public void onPayload(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInCustomPayload) {
            PacketPlayInCustomPayload packet = (PacketPlayInCustomPayload) event.getPacket();
            String channel = packet.a();
            if (channel.equals("auto32k")) {
                Impurity.EVENT_BUS.post(new PlayerServerSide32kEvent(event.getPlayer()));
            } else if (channel.equals("manual32k")) {
                PacketDataSerializer serializer = packet.b();
                Impurity.EVENT_BUS.post(new PlayerServerSide32kEvent(event.getPlayer(), serializer));
            }
        }
    }

    /**
     * @author SevJ6
     * Listener to post AsyncDeathEvent
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Impurity.EVENT_BUS.post(new AsyncDeathEvent(event.getEntity()));
    }

    /**
     * @author SevJ6
     * Listener to post when a player attempts to use an end crystal
     */
    @SevHandler
    public void onCrystal(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInUseItem) {
            PacketPlayInUseItem packet = (PacketPlayInUseItem) event.getPacket();
            Player player = event.getPlayer();
            BlockPosition placePos = packet.a();
            if (placePos == null) return;
            boolean holdingCrystal = (packet.c().equals(EnumHand.OFF_HAND)) ? player.getInventory().getItemInOffHand().getType() == org.bukkit.Material.END_CRYSTAL : player.getInventory().getItemInMainHand().getType() == org.bukkit.Material.END_CRYSTAL;
            org.bukkit.Material typeAtPos = new Location(player.getWorld(), placePos.getX(), placePos.getY(), placePos.getZ()).getBlock().getType();
            boolean valid = holdingCrystal && (typeAtPos == org.bukkit.Material.OBSIDIAN || typeAtPos == org.bukkit.Material.BEDROCK);
            if (valid) {
                Impurity.EVENT_BUS.post(new PlayerPlaceCrystalEvent(player, placePos, packet.c()));
            }
        }
    }

    /**
     * @author SevJ6
     * Listener to post when a player tries to 32k someone
     */
    @SevHandler
    public void on32k(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
            if (packet.a() == PacketPlayInUseEntity.EnumEntityUseAction.ATTACK) {
                Player player = event.getPlayer();
                ItemStack handItem = (packet.b() == EnumHand.OFF_HAND) ? player.getInventory().getItemInOffHand() : player.getInventory().getItemInMainHand();
                if (Utils.is32k(handItem)) {
                    Entity entity = packet.a(((CraftWorld) player.getWorld()).getHandle());
                    if (entity instanceof EntityPlayer) {
                        Player victim = ((EntityPlayer) entity).getBukkitEntity();
                        if (!victim.isInvulnerable()) {
                            Impurity.EVENT_BUS.post(new PlayerAttemptUse32kEvent(handItem, player, victim, packet.b()));
                        }
                    }
                }
            }
        }
    }

    /**
     * @author SevJ6
     * Listener for totem pop event
     */
    @SevHandler
    public void onTotemPop(PacketEvent.ServerToClient event) {
        if (event.getPacket() instanceof PacketPlayOutEntityStatus) {
            PacketPlayOutEntityStatus packet = (PacketPlayOutEntityStatus) event.getPacket();
            try {
                if (opCode.getByte(packet) == 35) {
                    World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
                    Entity entity = world.getEntity(entityId.getInt(packet));
                    if (entity == null) return;
                    if (entity.getWorld() != world) return;
                    if (world.getEntity(entityId.getInt(packet)) instanceof EntityPlayer) {
                        Impurity.EVENT_BUS.post(new TotemPopEvent((Player) entity.getBukkitEntity()));
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @author SevJ6
     * netty injector for packet listeners
     */
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Utils.inject(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Utils.removeHook(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Utils.removeHook(event.getPlayer());
    }
}
