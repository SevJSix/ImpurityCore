package me.sevj6.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancel);
}
