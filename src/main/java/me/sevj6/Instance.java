package me.sevj6;

import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public interface Instance {
    Impurity plugin = Impurity.getPlugin();
    FileConfiguration config = plugin.getConfig();
    ConfigManager fileConfig = ConfigManager.configManager;
    Configuration settings = fileConfig.getSettings();
    Configuration playtimes = fileConfig.getPlaytimes();
    Configuration tablist = fileConfig.getTablist();
    Configuration exploits = fileConfig.getExploits();
    Configuration namecolor = fileConfig.getNamecolor();
    Configuration totempops = fileConfig.getTotempops();
    HashMap<EntityType, Integer> entityMap = new HashMap<>();


}
