package me.sevj6;

import me.sevj6.util.fileutil.ConfigManager;
import me.sevj6.util.fileutil.Configuration;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public interface Instance {
    Impurity plugin = Impurity.getPlugin();
    ConfigManager fileConfig = ConfigManager.configManager;
    Configuration settings = fileConfig.getSettings();
    Configuration playtimes = fileConfig.getPlaytimes();
    Configuration namecolor = fileConfig.getNamecolor();
    Configuration totempops = fileConfig.getTotempops();
    HashMap<EntityType, Integer> entityMap = new HashMap<>();


}
