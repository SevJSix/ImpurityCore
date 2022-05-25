package me.sevj6.listener;

import me.sevj6.Impurity;
import me.sevj6.command.TabCompletion;
import me.sevj6.listener.illegals.IllegalItemManager;
import me.sevj6.listener.misc.CommandWhitelist;
import me.sevj6.listener.misc.*;
import me.sevj6.listener.patches.*;
import me.sevj6.listener.pvp.Meta116BedAura;
import me.sevj6.util.EntityUtil;
import net.minecraft.server.v1_12_R1.*;

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
        // Packet Listeners
        plugin.registerPacketListener(new NocomExploit(), PacketPlayInBlockDig.class, PacketPlayOutBlockChange.class);
        plugin.registerPacketListener(new InvalidSlotClick(), PacketPlayInWindowClick.class);
        plugin.registerPacketListener(new TabCompletion(), PacketPlayInTabComplete.class, PacketPlayOutTabComplete.class);
        plugin.registerPacketListener(new MovementExploits(), PacketPlayInTeleportAccept.class, PacketPlayInFlying.class);
        plugin.registerPacketListener(new BoatFly(), PacketPlayInUseEntity.class);
        // Other initializers
        IllegalItemManager.init();
        EntityUtil.setupEntityMap();
    }
}
