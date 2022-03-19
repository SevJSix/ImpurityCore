package me.sevj6;

import me.sevj6.command.CommandHandler;
import me.sevj6.event.bus.EventBus;
import me.sevj6.event.bus.SevListener;
import me.sevj6.listener.ListenerManager;
import me.sevj6.listener.Manager;
import me.sevj6.task.scheduler.TaskManager;
import me.sevj6.util.Utils;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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
    public void onEnable() {
        initializeManagers();
        if (Bukkit.getOnlinePlayers().size() > 0) Bukkit.getOnlinePlayers().forEach(Utils::inject);
        Executors.newScheduledThreadPool(4).scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        startTime = System.currentTimeMillis();
    }

    public void initializeManagers() {
        managers.add(new ConfigManager(this));
        managers.add(new ListenerManager(this));
        managers.add(new TaskManager(this));
        managers.forEach(Manager::init);
        new CommandHandler(this);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "Unloading all plugin data...");
        try {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                Bukkit.getOnlinePlayers().forEach(Utils::removeHook);
            }
            ConfigManager.configManager.getConfigs().forEach(Configuration::saveConfig);
            this.reloadConfig();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        getLogger().log(Level.INFO, "Successfully unloaded ImpurityPlus");
    }
}

