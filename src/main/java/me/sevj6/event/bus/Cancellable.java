package me.sevj6.event.bus;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
