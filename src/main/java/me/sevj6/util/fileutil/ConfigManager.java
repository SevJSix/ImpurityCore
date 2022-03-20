package me.sevj6.util.fileutil;

import me.sevj6.Impurity;
import me.sevj6.listener.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager extends Manager {

    public static ConfigManager configManager;
    private final List<Configuration> configs = new ArrayList<>();
    private Configuration namecolor;
    private Configuration settings;
    private Configuration playtimes;
    private Configuration totempops;

    public ConfigManager(Impurity plugin) {
        super(plugin);
        configManager = this;
    }

    @Override
    public void init() {
        settings = new Configuration("settings.yml", plugin);
        namecolor = new Configuration("namecolor.yml", plugin);
        totempops = new Configuration("totempops.yml", plugin);
        playtimes = new Configuration("playtimes.yml", plugin);
        configs.addAll(Arrays.asList(settings, namecolor, totempops, playtimes));
    }

    public Configuration getPlaytimes() {
        return playtimes;
    }

    public Configuration getTotempops() {
        return totempops;
    }

    public Configuration getSettings() {
        return settings;
    }

    public List<Configuration> getConfigs() {
        return configs;
    }

    public Configuration getNamecolor() {
        return namecolor;
    }

}
