package me.sevj6.util.fileutil;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author DoubleCheck
 * modified by SevJ6
 */

public class Configuration extends YamlConfiguration {

    private final String resourceName;
    private final Plugin plugin;
    private final boolean dataFolder;

    public Configuration(String resourceName, Plugin plugin, boolean dataFolder) {
        this.resourceName = resourceName;
        this.plugin = plugin;
        this.dataFolder = dataFolder;
        try {
            loadConfiguration();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Plugin getPlugin() {
        return plugin;
    }

    private void loadConfiguration() throws FileNotFoundException {
        try {
            File file;
            if (dataFolder) {
                File dataFolder = new File(plugin.getDataFolder() + "/data");
                if (!dataFolder.exists()) dataFolder.mkdirs();
            }
            file = new File(plugin.getDataFolder(), resourceName);
            if (!file.exists()) {
                InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
                if (is == null) throw new FileNotFoundException("Resource " + resourceName);
                Files.copy(is, file.toPath());
                is.close();
            }
            load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try {
            File file = new File(plugin.getDataFolder(), resourceName);
            save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            loadConfiguration();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void write(String objPath, Object value) {
        this.set(objPath, value);
        this.saveConfig();
        this.reloadConfig();
    }
}
