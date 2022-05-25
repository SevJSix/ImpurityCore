package me.sevj6;

import me.sevj6.command.CommandManager;
import me.sevj6.event.bus.EventBus;
import me.sevj6.event.bus.SevListener;
import me.sevj6.listener.ListenerManager;
import me.sevj6.listener.Manager;
import me.sevj6.task.AutoRestart;
import me.sevj6.task.scheduler.TaskManager;
import me.sevj6.util.Utils;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.RtMixin;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
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
    public static EventBus EVENT_BUS = new EventBus();
    private final List<ViolationManager> violationManagers = new ArrayList<>();
    private Instrumentation instrumentation;
    private final List<Manager> managers = new ArrayList<>();

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

    public void registerSevListener(SevListener sevListener) {
        EVENT_BUS.subscribe(sevListener);
    }

    public void registerBoth(Object listener) {
        getServer().getPluginManager().registerEvents((Listener) listener, this);
        EVENT_BUS.subscribe((SevListener) listener);
    }

    @Override
    public void onLoad() {
        Instrumentation inst = RtMixin.attachAgent().orElseThrow(RuntimeException::new);
        instrumentation = inst;
        RtMixin.processMixins(Impurity.class);
    }

    @Inject(info = @MethodInfo(_class = Impurity.class, name = "onEnable", rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void testGayness(CallbackInfo ci) {
        for (int i = 0; i < 20; i++) {
            System.out.println("Sev is gay");
        }
        ci.cancel();
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdirs();
        if (Bukkit.getOnlinePlayers().size() > 0) Bukkit.getOnlinePlayers().forEach(Utils::inject);
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

