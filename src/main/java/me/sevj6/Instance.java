package me.sevj6;

import me.sevj6.util.fileutil.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

public interface Instance {
    Impurity plugin = Impurity.getPlugin();
    FileConfiguration config = plugin.getConfig();
    ConfigManager fileConfig = ConfigManager.getInstance();
}
