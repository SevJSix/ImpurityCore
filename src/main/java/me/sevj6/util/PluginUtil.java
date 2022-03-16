package me.sevj6.util;

import me.sevj6.Impurity;
import me.sevj6.event.ImpurityEventFactory;
import me.sevj6.event.bus.SevListener;
import me.sevj6.listeners.dupe.LavaDupe;
import me.sevj6.listeners.dupe.PistonDupe;
import me.sevj6.listeners.dupe.SalC1Dupe;
import me.sevj6.listeners.misc.*;
import me.sevj6.listeners.packet.*;
import me.sevj6.listeners.patches.*;
import me.sevj6.listeners.playtimes.PlaytimeListeners;
import me.sevj6.listeners.pvp.Auto32k;
import me.sevj6.listeners.pvp.CrystalAura;
import me.sevj6.listeners.pvp.Meta116BedAura;
import me.sevj6.listeners.pvp.TotemPopNotify;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class PluginUtil extends Utils implements Data {

    public static void registerEventListeners() {
        plugin.getLogger().info("Registering Events...");
        PluginManager pl = plugin.getServer().getPluginManager();
        getBukkitListeners().forEach(listener -> pl.registerEvents(listener, plugin));
        getNMSPacketListeners().forEach(nmsPacketListener -> Impurity.EVENT_BUS.subscribe(nmsPacketListener));
    }

    public static void setupEntityMap() {
        if (config().getBoolean("EntityLimit.Enabled")) {
            entityMap.put(EntityType.WITHER, config().getInt("EntityLimit.wither"));
            entityMap.put(EntityType.DROPPED_ITEM, config().getInt("EntityLimit.item-drops"));
            entityMap.put(EntityType.ENDER_CRYSTAL, config().getInt("EntityLimit.end-crystal"));
            entityMap.put(EntityType.ARMOR_STAND, config().getInt("EntityLimit.armor-stand"));
            entityMap.put(EntityType.PRIMED_TNT, config().getInt("EntityLimit.primed-tnt"));
            entityMap.put(EntityType.WITHER_SKULL, config().getInt("EntityLimit.wither-skull"));
            entityMap.put(EntityType.FALLING_BLOCK, config().getInt("EntityLimit.falling-block"));
        }
    }

    public static FileConfiguration config() {
        return plugin.getConfig();
    }

    public static List<SevListener> getNMSPacketListeners() {
        List<SevListener> nmsListeners = new ArrayList<>();
        nmsListeners.add(new ImpurityEventFactory());
        nmsListeners.add(new TotemPopStatistic());
        nmsListeners.add(new PacketAutoRecipe());
        nmsListeners.add(new PacketBlockDig());
        nmsListeners.add(new PacketBlockPlace());
        nmsListeners.add(new PacketWindowClick());
        nmsListeners.add(new PacketPlayFlying());
        nmsListeners.add(new NocomExploit());
        nmsListeners.add(new BoatFly());
        nmsListeners.add(new PacketCreativeSlot());
        nmsListeners.add(new TeleportAcceptPackets());
        nmsListeners.add(new NBTLimitBan());
        nmsListeners.add(new AuraSpeedLimit());
        nmsListeners.add(new MovementExploits());
        nmsListeners.add(new Auto32k());
        nmsListeners.add(new CrystalAura());
        nmsListeners.add(new SuperweaponExploits());
        nmsListeners.add(new TotemPopNotify());
        return nmsListeners;
    }

    public static List<Listener> getBukkitListeners() {
        List<Listener> bukkitListeners = new ArrayList<>();
        bukkitListeners.add(new ImpurityEventFactory());
        bukkitListeners.add(new AnvilColoredName());
        bukkitListeners.add(new PlayerListener());
        bukkitListeners.add(new GreenText());
        bukkitListeners.add(new BurrowPatchRewrite());
        bukkitListeners.add(new BowOppStoppa());
        bukkitListeners.add(new BlockPhysicsLag());
        bukkitListeners.add(new BlockPerChunkLimit());
        bukkitListeners.add(new GodmodePatch());
        bukkitListeners.add(new RedstoneEvents());
        bukkitListeners.add(new PreventCommandSigns());
        bukkitListeners.add(new TeleportCoordLog());
        bukkitListeners.add(new InteractEventNerf());
        bukkitListeners.add(new EndGatewayCrashPatch());
        bukkitListeners.add(new BoatFly());
        bukkitListeners.add(new DispenserExploits());
        bukkitListeners.add(new WitherSkullRemover());
        bukkitListeners.add(new EntityThroughPortalLag());
        bukkitListeners.add(new ElytraFlyExploits());
        bukkitListeners.add(new ThrownEntityDelete());
        bukkitListeners.add(new NoEndPortalGrief());
        bukkitListeners.add(new CommandWhitelist());
        bukkitListeners.add(new PistonDupe());
        bukkitListeners.add(new RandomSpawn());
        bukkitListeners.add(new SalC1Dupe());
        bukkitListeners.add(new NameColorJoinListener());
        bukkitListeners.add(new Meta116BedAura());
        bukkitListeners.add(new LavaDupe());
        bukkitListeners.add(new PlaytimeListeners(plugin));
        bukkitListeners.add(new ArmorStandAiDisable());
        bukkitListeners.add(new Auto32k());
        bukkitListeners.add(new SuperweaponExploits());
        bukkitListeners.add(new AntiSpam());
        bukkitListeners.add(new ItemDropNBTBan());
        return bukkitListeners;
    }
}
