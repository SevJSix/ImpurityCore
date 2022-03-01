package me.sevj6.event;

import me.sevj6.Impurity;
import me.sevj6.command.Command;
import me.sevj6.command.CommandHandler;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.event.events.TotemPopEvent;
import me.sevj6.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

public class ImpurityEventFactory implements Listener, SevListener {

    public static Field opCode;
    public static Field entityId;

    static {
        try {
            opCode = PacketPlayOutEntityStatus.class.getDeclaredField("b");
            opCode.setAccessible(true);
            entityId = PacketPlayOutEntityStatus.class.getDeclaredField("a");
            entityId.setAccessible(true);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private final ConcurrentHashMap<Player, String[]> completeMap = new ConcurrentHashMap<>();

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
     * Tab Completion
     */
    @SevHandler
    public void onPacket(PacketEvent.ClientToServer event) {
        if (event.getPacket() instanceof PacketPlayInTabComplete) {
            PacketPlayInTabComplete packet = (PacketPlayInTabComplete) event.getPacket();
            Player player = event.getPlayer();
            String[] rawArgs = packet.a().split(" ");
            String cmd = rawArgs[0].substring(1);
            Command command = CommandHandler.commands.stream().filter(c -> c.getName().equalsIgnoreCase(cmd)).findAny().orElse(null);
            String[] online = Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toArray(String[]::new);

            if (cmd.isEmpty()) {
                this.completeMap.putIfAbsent(player, Utils.getAllowedCommands());
                return;
            }

            if (cmd.contains(":")) {
                event.setCancelled(true);
                return;
            }

            if (Utils.getAllowedCommandsAsListWithNoPrefix().stream().noneMatch(s -> s.equalsIgnoreCase(cmd))) {
                event.setCancelled(true);
                return;
            } else if (command == null) {
                this.completeMap.putIfAbsent(player, online);
                return;
            }

            String[] completions = command.onTabComplete();
            if (completions == null || completions.length == 0) {
                this.completeMap.putIfAbsent(player, online);
            } else {
                this.completeMap.putIfAbsent(player, completions);
            }
        }
    }

    @SevHandler
    public void onPacketSend(PacketEvent.ServerToClient event) {
        if (event.getPacket() instanceof PacketPlayOutTabComplete) {
            Player player = event.getPlayer();
            if (completeMap.containsKey(player)) {
                event.setPacket(new PacketPlayOutTabComplete(completeMap.get(player)));
                completeMap.remove(player);
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
