package me.sevj6.util.statstics;

import me.sevj6.Instance;
import me.sevj6.util.Utils;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayerStat implements Instance {

    private final long totalPlayTime;
    private final long daysSinceJoined;
    private final long lastPlayed;
    private final int deaths;
    private final int kills;
    private final long totemPops;
    private OfflinePlayer player;

    public PlayerStat(OfflinePlayer player) {
        this.totalPlayTime = player.getPlayer().getStatistic(Statistic.PLAY_ONE_TICK) * 50L;
        long since = System.currentTimeMillis() - player.getFirstPlayed();
        this.daysSinceJoined = TimeUnit.MILLISECONDS.toDays(since);
        this.lastPlayed = System.currentTimeMillis() - player.getLastPlayed();
        this.deaths = player.getPlayer().getStatistic(Statistic.DEATHS);
        this.kills = player.getPlayer().getStatistic(Statistic.PLAYER_KILLS);
        this.totemPops = (!totempops.contains(player.getUniqueId().toString())) ? 0 : totempops.getLong(player.getUniqueId().toString());
    }

    public String getTotemPops() {
        return String.valueOf(this.totemPops);
    }

    public String getTotalPlaytime() {
        return Utils.getFormattedInterval(this.totalPlayTime);
    }

    public String getLastPlayed() {
        return String.valueOf(TimeUnit.MILLISECONDS.toDays(this.lastPlayed));
    }

    public String getDaysSinceFirstJoined() {
        return String.valueOf(this.daysSinceJoined);
    }

    public String getTotalKills() {
        return String.valueOf(this.kills);
    }

    public String getTotalDeaths() {
        return String.valueOf(this.deaths);
    }

    public Player getPlayer() {
        return this.player.getPlayer();
    }

    public OfflinePlayer getOfflinePlayer() {
        return this.player;
    }
}
