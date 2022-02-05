package me.sevj6;

import me.sevj6.util.fileutil.ConfigManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public interface Instance {
    Impurity plugin = Impurity.getPlugin();
    FileConfiguration config = plugin.getConfig();
    List<String> itemList = config.getStringList("Exploits.throwables");
    ConfigManager fileConfig = ConfigManager.getInstance();
}
