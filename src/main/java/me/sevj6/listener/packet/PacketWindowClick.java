package me.sevj6.listener.packet;

import me.sevj6.Instance;
import me.sevj6.event.bus.SevHandler;
import me.sevj6.event.bus.SevListener;
import me.sevj6.event.events.PacketEvent;
import me.sevj6.util.PlayerUtil;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.Configuration;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayInWindowClick;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author SevJ6
 */

public class PacketWindowClick extends ViolationManager implements SevListener, Instance {
    Configuration exploit = fileConfig.getExploits();

    public PacketWindowClick() {
        super(fileConfig.getExploits().getInt("WindowClick.incrementVLS"), fileConfig.getExploits().getInt("WindowClick.decrementVLS"));
    }

    @SevHandler
    public void onIncoming(PacketEvent.ClientToServer event) {
        Packet<?> packet = event.getPacket();
        if (exploit.getBoolean("Packets.Enabled") && packet instanceof PacketPlayInWindowClick) {
            Player player = event.getPlayer();
            PacketPlayInWindowClick packetPlayInWindowClick = (PacketPlayInWindowClick) packet;
            int slot = packetPlayInWindowClick.b();
            ItemStack itemStack = CraftItemStack.asBukkitCopy(packetPlayInWindowClick.e());
            if ((player.getOpenInventory() == null && slot > player.getInventory().getSize()) || slot > player.getOpenInventory().countSlots()) {
                event.setCancelled(true);
                PlayerUtil.kickPlayerAsync(player, "Invalid inventory slot clicked [" + "SlotID: " + slot + "]");
            } else if (itemStack.getType() != Material.AIR) {
                increment(player.getUniqueId());
                if (getVLS(player.getUniqueId()) > exploit.getInt("WindowClick.maxVLS")) {
                    event.setCancelled(true);
                    PlayerUtil.kickPlayerAsync(player, "Kicked for exceeding click window packets");
                }
            }
        }
    }
}
