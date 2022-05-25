package me.sevj6.util.fileutil;

import lombok.Getter;
import me.sevj6.Impurity;
import me.sevj6.listener.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigManager extends Manager {

    @Getter
    public static ConfigManager configManager;
    @Getter
    private final List<Configuration> configs = new ArrayList<>();
    @Getter
    private Configuration namecolor;
    @Getter
    private Configuration settings;
    @Getter
    private Configuration playtimes;
    @Getter
    private Configuration totempops;

    public ConfigManager(Impurity plugin) {
        super(plugin);
        configManager = this;
    }

    @Override
    public void init() {
        settings = new Configuration("settings.yml", plugin, false);
        namecolor = new Configuration("data/namecolor.yml", plugin, true);
        totempops = new Configuration("data/totempops.yml", plugin, true);
        playtimes = new Configuration("data/playtimes.yml", plugin, true);
        configs.addAll(Arrays.asList(settings, namecolor, totempops, playtimes));
    }

}
