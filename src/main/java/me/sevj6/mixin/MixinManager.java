package me.sevj6.mixin;

import me.sevj6.Impurity;
import me.sevj6.listener.Manager;

public class MixinManager extends Manager {

    public MixinManager(Impurity plugin) {
        super(plugin);
    }

    @Override
    public void init() {

    }
}
