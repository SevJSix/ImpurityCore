package me.sevj6.event.events;

import me.sevj6.event.bus.Cancellable;
import me.sevj6.event.bus.Event;
import net.minecraft.server.v1_12_R1.EnumHand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerAttemptUse32kEvent extends Event implements Cancellable {

    private final ItemStack thirtyTwoK;
    private final Player player;
    private final Player victim;
    private final EnumHand hand;
    private boolean cancelled;

    public PlayerAttemptUse32kEvent(ItemStack thirtyTwoK, Player player, Player victim, EnumHand hand) {
        this.thirtyTwoK = thirtyTwoK;
        this.player = player;
        this.victim = victim;
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }

    public double getAttackRange() {
        return player.getLocation().distance(victim.getLocation());
    }

    public Player getVictim() {
        return victim;
    }

    public ItemStack get32k() {
        return thirtyTwoK;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
