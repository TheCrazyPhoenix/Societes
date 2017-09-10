package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.economy.AccountHolder;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;

/**
 * Base event for when an AccountHolder's properties change.
 */
public interface ChangeAccountHolderEvent extends Event, Cancellable {
    /**
     * Retrieves the AccountHolder affected by this event.
     * This represents the old state of the AccountHolder, except for the {@link Create} event.
     * @return The modified taxable.
     */
    AccountHolder getAccountHolder();

    /**
     * Called when an AccountHolder is created.
     */
    interface Create extends ChangeAccountHolderEvent {}

    /**
     * Called when an AccountHolder is destroyed.
     */
    interface Destroy extends ChangeAccountHolderEvent {}
}
