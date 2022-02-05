package me.sevj6;

import me.sevj6.command.CommandHandler;
import me.sevj6.event.EventBus;
import me.sevj6.event.NMSPacketListener;
import me.sevj6.listeners.meta.MetaManager;
import me.sevj6.listeners.meta.MetaType;
import me.sevj6.listeners.playtimes.PlaytimeManager;
import me.sevj6.util.PluginUtil;
import me.sevj6.util.Utils;
import me.sevj6.util.ViolationManager;
import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
    private List<Listener> bukkitListeners;
    private List<NMSPacketListener> nmsPacketListeners;
    private MetaManager metaManager;
    private DayOfWeek day;

    public static Impurity getPlugin() {
        return getPlugin(Impurity.class);
    }

    public PlaytimeManager getPlaytimeManager() {
        return playtimeManager;
    }

    public MetaManager getMetaManager() {
        return metaManager;
    }

    public List<Listener> getBukkitListeners() {
        return bukkitListeners;
    }

    public List<NMSPacketListener> getNmsPacketListeners() {
        return nmsPacketListeners;
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

        // initialize
        startTime = System.currentTimeMillis();
        violationManagers = new ArrayList<>();
        service = Executors.newScheduledThreadPool(4);
        playtimeManager = new PlaytimeManager(this);
        nmsPacketListeners = PluginUtil.setUpNMSPacketListeners();
        bukkitListeners = PluginUtil.setUpBukkitListeners();

        //register
        PluginUtil.startBukkitSchedulers();
        PluginUtil.setupEntityMap();
        PluginUtil.registerEventListeners();
        new CommandHandler(this);

        if (Bukkit.getOnlinePlayers().size() > 0) Bukkit.getOnlinePlayers().forEach(Utils::inject);

        LocalDateTime time = LocalDateTime.now();
        day = time.getDayOfWeek();
        MetaType type;
        switch (time.getDayOfWeek()) {
            case MONDAY:
                type = MetaType.NO_OFFHAND_32K;
                break;
            case TUESDAY:
                type = MetaType.OFFHAND_32K;
                break;
            case WEDNESDAY:
                type = MetaType.FAST_CA;
                break;
            case THURSDAY:
                type = MetaType.NO_32k;
                break;
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                type = MetaType.ALL_METAS;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + time.getDayOfWeek());
        }

        metaManager = new MetaManager(type);

        // setup violation manager
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);
        getLogger().log(Level.ALL, "ImpurityPlus loaded. Version" + this.getDescription().getVersion() + " by SevJ6");
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

    public String metaTablistPlaceholder() {
        String placeHolder = null;
        switch (getDay()) {
            case MONDAY:
                placeHolder = "Mainhand Only With 32k's Enabled";
                break;
            case TUESDAY:
                placeHolder = "Offhand Allowed With 32k's Enabled";
                break;
            case WEDNESDAY:
                placeHolder = "Fast Server-Side CrystalAura Meta Enabled";
                break;
            case THURSDAY:
                placeHolder = "32k's Disabled, Mainhand Only";
                break;
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                placeHolder = "All Metas Enabled (32k's, FastCA, Offhand)";
                break;
        }
        return placeHolder;
    }

    public DayOfWeek getDay() {
        return day;
    }
}

