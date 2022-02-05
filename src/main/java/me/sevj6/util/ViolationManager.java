package me.sevj6.util;

import me.sevj6.Impurity;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 254n_m
 * Credit L2X9RebootCore
 */

public abstract class ViolationManager {
    private final ConcurrentHashMap<UUID, Integer> map;
    private final int addAmount;
    private final int reduceAmount;


    public ViolationManager(int addAmount, int reduceAmount) {
        this.addAmount = addAmount;
        this.reduceAmount = reduceAmount;
        map = new ConcurrentHashMap<>();
        Impurity.getPlugin().registerViolationManager(this);
    }

    public void decrementAll() {
        map.forEach((key, val) -> {
            if (val <= 0) {
                map.remove(key);
                return;
            }
            map.replace(key, val - reduceAmount);
        });
    }

    public void increment(UUID uuid) {
        if (!map.containsKey(uuid)) {
            map.put(uuid, 0);
        } else map.replace(uuid, map.get(uuid) + addAmount);
    }

    public int getVLS(UUID id) {
        return map.getOrDefault(id, -1);
    }

    public void remove(UUID id) {
        map.remove(id);
    }
}
