package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;

/**
 * Base event for when a society changes.
 * This is called when anything owned by the society changes.
 */
public interface SocietyChangeEvent extends Event, Cancellable {
    /**
     * Retrieves the modified society.
     * This is the society or sub-society which is most concerned by this event.
     * This object represents the old state for all events except {@link Create}.
     * @return The society affected by this event.
     */
    Society getSociety();

    /**
     * Called when a society is created.
     */
    interface Create extends SocietyChangeEvent {}

    /**
     * Called when a society is destroyed.
     */
    interface Destroy extends SocietyChangeEvent {}
}
