package me.sevj6.util;

import me.sevj6.Impurity;
import me.sevj6.Instance;
import me.sevj6.event.ImpurityEventFactory;
import me.sevj6.event.bus.SevListener;
import me.sevj6.listener.dupe.LavaDupe;
import me.sevj6.listener.dupe.PistonDupe;
import me.sevj6.listener.dupe.SalC1Dupe;
import me.sevj6.listener.misc.*;
import me.sevj6.listener.packet.limit.*;
import me.sevj6.listener.patches.*;
import me.sevj6.listener.pvp.Auto32k;
import me.sevj6.listener.pvp.CrystalAura;
import me.sevj6.listener.pvp.Meta116BedAura;
import me.sevj6.listener.pvp.TotemPopNotify;
import me.sevj6.util.fileutil.Setting;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class PluginUtil extends Utils implements Instance {

    private static final Setting<Integer> animals = Setting.getInt("entities.animals");
    private static final Setting<Integer> mobs = Setting.getInt("entities.mobs");
    private static final Setting<Integer> withers = Setting.getInt("entities.withers");
    private static final Setting<Integer> ender_crystals = Setting.getInt("entities.ender_crystals");
    private static final Setting<Integer> armor_stands = Setting.getInt("entities.armor_stands");
    private static final Setting<Integer> primed_tnt = Setting.getInt("entities.primed_tnt");
    private static final Setting<Integer> wither_skulls = Setting.getInt("entities.wither_skulls");
    private static final Setting<Integer> falling_block = Setting.getInt("entities.falling_block");
    private static final Setting<Integer> dropped_items = Setting.getInt("entities.dropped_items");

    public static void registerEventListeners() {
        plugin.getLogger().info("Registering Events...");
        PluginManager pl = plugin.getServer().getPluginManager();
        getBukkitListeners().forEach(listener -> pl.registerEvents(listener, plugin));
        getNMSPacketListeners().forEach(nmsPacketListener -> Impurity.EVENT_BUS.subscribe(nmsPacketListener));
    }

    public static void setupEntityMap() {
        if (config.getBoolean("EntityLimit.Enabled")) {
            for (EntityType neutralEntity : Utils.neutralEntities) {
                entityMap.put(neutralEntity, animals.getValue());
            }
            for (EntityType hostileEntity : Utils.hostileEntities) {
                entityMap.put(hostileEntity, mobs.getValue());
            }
            entityMap.put(EntityType.WITHER, withers.getValue());
            entityMap.put(EntityType.DROPPED_ITEM, dropped_items.getValue());
            entityMap.put(EntityType.ENDER_CRYSTAL, ender_crystals.getValue());
            entityMap.put(EntityType.ARMOR_STAND, armor_stands.getValue());
            entityMap.put(EntityType.PRIMED_TNT, primed_tnt.getValue());
            entityMap.put(EntityType.WITHER_SKULL, wither_skulls.getValue());
            entityMap.put(EntityType.FALLING_BLOCK, falling_block.getValue());
        }
    }

    public static List<SevListener> getNMSPacketListeners() {
        List<SevListener> nmsListeners = new ArrayList<>();
        nmsListeners.add(new ImpurityEventFactory());
        nmsListeners.add(new TotemPopStatistic());
        nmsListeners.add(new PacketAutoRecipe());
        nmsListeners.add(new PacketBlockDig());
        nmsListeners.add(new PacketBlockPlace());
        nmsListeners.add(new PacketWindowClick());
        nmsListeners.add(new PacketFlyPhase());
        nmsListeners.add(new NocomExploit());
        nmsListeners.add(new BoatFly());
        nmsListeners.add(new CreativeSetSlotPacket());
        nmsListeners.add(new TeleportAcceptPackets());
        nmsListeners.add(new NBTLimitBan());
        nmsListeners.add(new PacketUseEntity());
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
        bukkitListeners.add(new JoinQuitListener());
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
