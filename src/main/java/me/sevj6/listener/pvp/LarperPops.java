package me.sevj6.listener.pvp;

import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

public class LarperPops {

    private final ConcurrentHashMap<Player, Integer> pops = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Player, Integer> getPops() {
        return pops;
    }

    public int getTotemPops(Player player) {
        return getPops().get(player);
    }

    public boolean hasPlayerPopped(Player player) {
        return getPops().containsKey(player);
    }

    public void resetPops(Player player) {
        getPops().remove(player);
    }

    public void incrementPops(Player player) {
        if (getPops().containsKey(player)) {
            getPops().replace(player, getPops().get(player) + 1);
        } else {
            getPops().put(player, 1);
        }
    }
}
