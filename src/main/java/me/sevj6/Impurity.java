package me.sevj6;

import lombok.Getter;
import me.sevj6.command.CommandManager;
import me.sevj6.listener.ListenerManager;
import me.sevj6.listener.Manager;
import me.sevj6.task.AutoRestart;
import me.sevj6.task.scheduler.TaskManager;
import me.sevj6.util.Utils;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.txmc.rtmixin.RtMixin;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author SevJ6
 */

public final class Impurity extends JavaPlugin {

    public static long startTime;
    private final List<ViolationManager> violationManagers = new ArrayList<>();
    private final List<Manager> managers = new ArrayList<>();
    private Instrumentation instrumentation;
    @Getter
    private PacketEventDispatcher dispatcher;

    public static Impurity getPlugin() {
        return getPlugin(Impurity.class);
    }

    public List<ViolationManager> getViolationManagers() {
        return violationManagers;
    }

    public void registerViolationManager(ViolationManager manager) {
        if (violationManagers.contains(manager)) return;
        violationManagers.add(manager);
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @SafeVarargs
    public final void registerPacketListener(PacketListener listener, Class<? extends Packet<?>>... classes) {
        dispatcher.register(listener, classes);
    }

    @Override
    public void onLoad() {
        Instrumentation inst = RtMixin.attachAgent().orElseThrow(RuntimeException::new);
        instrumentation = inst;
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        dispatcher = new PacketEventDispatcher(this);
        Executors.newScheduledThreadPool(4).scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        initializeManagers();
        startTime = System.currentTimeMillis();
        new AutoRestart();
    }

    public void initializeManagers() {
        managers.add(new ConfigManager(this));
        managers.add(new TaskManager(this));
        managers.add(new ListenerManager(this));
        managers.add(new CommandManager(this));
        managers.forEach(Manager::init);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "Unloading all plugin data...");
        try {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                Bukkit.getOnlinePlayers().forEach(Utils::removeHook);
            }
            ConfigManager.configManager.getConfigs().forEach(Configuration::saveConfig);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        getLogger().log(Level.INFO, "Successfully unloaded ImpurityPlus");
    }
}

