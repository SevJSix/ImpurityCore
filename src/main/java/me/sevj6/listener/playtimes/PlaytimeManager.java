package me.sevj6.listener.playtimes;

import me.sevj6.Impurity;
import me.sevj6.util.Utils;
import me.sevj6.util.fileutil.Configuration;

import java.util.UUID;

public class PlaytimeManager {

    private final Configuration playtimes;
    private Impurity plugin;

    public PlaytimeManager(Impurity plugin) {
        playtimes = new Configuration("playtimes.yml", plugin);
    }

    public String getFormattedPlaytime(UUID player) {
        if (playtimes.contains(player.toString())) {
            return Utils.getFormattedInterval(playtimes.getLong(player.toString()));
        } else return "none";
    }

    public Configuration getPlaytimes() {
        return playtimes;
    }
}
