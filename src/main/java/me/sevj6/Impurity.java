package me.sevj6;

import me.sevj6.command.CommandHandler;
import me.sevj6.event.EventBus;
import me.sevj6.listeners.meta.MetaManager;
import me.sevj6.listeners.playtimes.PlaytimeManager;
import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * @author SevJ6
 */

public final class Impurity extends JavaPlugin implements Instance {

    public static long startTime;
    public static EventBus EVENT_BUS = new EventBus();
    private PlaytimeManager playtimeManager;
    private List<ViolationManager> violationManagers;
    private ScheduledExecutorService service;
    private MetaManager metaManager;

    public static Impurity getPlugin() {
        return getPlugin(Impurity.class);
    }

    public PlaytimeManager getPlaytimeManager() {
        return playtimeManager;
    }

    public MetaManager getMetaManager() {
        return metaManager;
    }

    public List<ViolationManager> getViolationManagers() {
        return violationManagers;
    }

    public void registerViolationManager(ViolationManager manager) {
        if (violationManagers.contains(manager)) return;
        violationManagers.add(manager);
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        fileConfig.init();
        startTime = System.currentTimeMillis();
        violationManagers = new ArrayList<>();
        playtimeManager = new PlaytimeManager(this);
        PluginUtil.startBukkitSchedulers();
        PluginUtil.setupEntityMap();
        PluginUtil.registerEventListeners();
        new CommandHandler(this);
        if (Bukkit.getOnlinePlayers().size() > 0) Bukkit.getOnlinePlayers().forEach(Utils::inject);
        metaManager = new MetaManager(MetaManager.getTypeAccordingToDay());
        service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.WARNING, "Unloading all plugin data...");
        try {
            if (Bukkit.getOnlinePlayers().isEmpty()) return;
            Bukkit.getOnlinePlayers().forEach(Utils::removeHook);
            ConfigManager.getInstance().getConfigs().forEach(Configuration::saveConfig);
            this.reloadConfig();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        getLogger().log(Level.INFO, "Successfully unloaded ImpurityPlus");
    }
}

