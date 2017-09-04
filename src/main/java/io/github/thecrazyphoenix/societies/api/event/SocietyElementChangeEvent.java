package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.SocietyElement;

/**
 * Base event for when a society element's data is modified
 */
public interface SocietyElementChangeEvent extends SocietyChangeEvent {
    /**
     * Retrieves the element modified by this event.
     * This will always return the element whose data is modified even if other elements are associated with this event.
     * This represents its old state for all events except {@link Create}.
     * @return The modified element.
     */
    SocietyElement getElement();

    /**
     * Called when a society element is created.
     */
    interface Create extends SocietyElementChangeEvent {}

    /**
     * Called when a society element is destroyed.
     */
    interface Destroy extends SocietyElementChangeEvent {}
}
