package me.sevj6.listener;

import me.sevj6.Impurity;
import me.sevj6.event.ImpurityEventFactory;
import me.sevj6.listener.dupe.LavaDupe;
import me.sevj6.listener.dupe.PistonDupe;
import me.sevj6.listener.dupe.SalC1Dupe;
import me.sevj6.listener.illegals.IllegalItemManager;
import me.sevj6.listener.misc.*;
import me.sevj6.listener.packet.limit.*;
import me.sevj6.listener.patches.*;
import me.sevj6.listener.pvp.Auto32k;
import me.sevj6.listener.pvp.CrystalAura;
import me.sevj6.listener.pvp.Meta116BedAura;
import me.sevj6.listener.pvp.TotemPopNotify;
import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;

public class ListenerManager extends Manager {

    public ListenerManager(Impurity plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        // Bukkit Listeners
        plugin.registerListener(new Meta116BedAura());
        plugin.registerListener(new PlaytimeListeners(plugin));
        plugin.registerListener(new AntiSpam());
        plugin.registerListener(new ArmorStandAiDisable());
        plugin.registerListener(new BlockPerChunkLimit());
        plugin.registerListener(new BlockPhysicsLag());
        plugin.registerListener(new BowOppStoppa());
        plugin.registerListener(new BurrowPatchRewrite());
        plugin.registerListener(new DispenserExploits());
        plugin.registerListener(new ElytraFlyExploits());
        plugin.registerListener(new EndGatewayCrashPatch());
        plugin.registerListener(new EntityThroughPortalLag());
        plugin.registerListener(new GodmodePatch());
        plugin.registerListener(new InteractEventNerf());
        plugin.registerListener(new ItemDropNBTBan());
        plugin.registerListener(new NoEndPortalGrief());
        plugin.registerListener(new PreventCommandSigns());
        plugin.registerListener(new RedstoneEvents());
        plugin.registerListener(new TeleportCoordLog());
        plugin.registerListener(new ThrownEntityDelete());
        plugin.registerListener(new AnvilColoredName());
        plugin.registerListener(new CommandWhitelist());
        plugin.registerListener(new GreenText());
        plugin.registerListener(new JoinQuitListener());
        plugin.registerListener(new PlayerListener());
        plugin.registerListener(new RandomSpawn());
        plugin.registerListener(new WitherSkullRemover());
        plugin.registerListener(new LavaDupe());
        plugin.registerListener(new PistonDupe());
        plugin.registerListener(new SalC1Dupe());

        // Sev Listeners
        plugin.registerSevListener(new TotemPopStatistic());
        plugin.registerSevListener(new NBTLimitBan());
        plugin.registerSevListener(new NocomExploit());
        plugin.registerSevListener(new PacketAutoRecipe());
        plugin.registerSevListener(new PacketBlockDig());
        plugin.registerSevListener(new PacketBlockPlace());
        plugin.registerSevListener(new CreativeSetSlotPacket());
        plugin.registerSevListener(new PacketFlyPhase());
        plugin.registerSevListener(new PacketWindowClick());
        plugin.registerSevListener(new TeleportAcceptPackets());
        plugin.registerSevListener(new PacketUseEntity());
        plugin.registerSevListener(new TotemPopNotify());
        plugin.registerSevListener(new CrystalAura());

        // Register both as Sev Listener and Bukkit Listener
        plugin.registerBoth(new ImpurityEventFactory());
        plugin.registerBoth(new Auto32k());
        plugin.registerBoth(new MovementExploits());
        plugin.registerBoth(new SuperweaponExploits());
        plugin.registerBoth(new BoatFly());

        // Other initializers
        IllegalItemManager.init();
        Utils.fixLightUpdateQueueing();
        PluginUtil.setupEntityMap();
    }
}
