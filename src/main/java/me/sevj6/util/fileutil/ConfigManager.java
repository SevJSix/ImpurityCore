package me.sevj6.util.fileutil;

import me.sevj6.Instance;

import java.util.ArrayList;
import java.util.List;

public class ConfigManager implements Instance {

    private final List<Configuration> configs = new ArrayList<>();
    private Configuration namecolor;
    private Configuration tablist;
    private Configuration exploits;

    public static ConfigManager getInstance() {
        ConfigManager manager = null;
        try {
            manager = ConfigManager.class.newInstance();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return manager;
    }


    public List<Configuration> getConfigs() {
        return configs;
    }

    public Configuration getTablist() {
        return tablist;
    }

    public Configuration getExploits() {
        return exploits;
    }

    public Configuration getNamecolor() {
        return namecolor;
    }

    public void init() {
        namecolor = new Configuration("namecolor.yml", plugin);
        tablist = new Configuration("tablist.yml", plugin);
        exploits = new Configuration("packets.yml", plugin);
        configs.add(namecolor);
        configs.add(tablist);
        configs.add(exploits);
    }
}
