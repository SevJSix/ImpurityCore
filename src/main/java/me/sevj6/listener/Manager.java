package me.sevj6.listener;

import me.sevj6.Impurity;

public abstract class Manager {

    public Impurity plugin;

    public Manager(Impurity plugin) {
        this.plugin = plugin;
    }

    public abstract void init();
}
