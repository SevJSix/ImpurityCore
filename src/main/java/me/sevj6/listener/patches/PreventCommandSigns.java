package me.sevj6.listener.patches;

import me.sevj6.Instance;
import me.sevj6.util.fileutil.Setting;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntitySign;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author SevJ6
 */

public class PreventCommandSigns implements Listener, Instance {

    private final Setting<List<String>> badNBTTags = Setting.getStringList("blacklisted_tags");
    private final Setting<Boolean> checkBadTiles = Setting.getBoolean("prevent_abusive_tiles");
    List<Material> signs = Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN);

    public boolean checkTag(String tag) {
        for (String s : badNBTTags.getValue()) {
            return tag.contains(s);
        }
        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (checkBadTiles.getValue()) {
            if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) return;
            if (!signs.contains(event.getClickedBlock().getType())) return;
            Sign sign = (Sign) event.getClickedBlock().getState();
            TileEntitySign s = (TileEntitySign) ((CraftWorld) sign.getWorld()).getHandle().getTileEntity(new BlockPosition(sign.getX(), sign.getY(), sign.getZ()));
            if (s != null) {
                NBTTagCompound signComp = s.d();
                if (checkTag(signComp.toString())) {
                    event.setCancelled(true);
                    sign.getBlock().breakNaturally();
                }
            }
        }
    }
}
