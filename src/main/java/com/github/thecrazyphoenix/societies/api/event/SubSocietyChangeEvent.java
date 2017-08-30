package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.society.SubSociety;

public interface SubSocietyChangeEvent extends SocietyChangeEvent {
    /**
     * Retrieves the sub-society affected by this event.
     * This represents the old state of the sub-society, except for the Create event.
     * @return The sub-society.
     */
    SubSociety getSubSociety();

    /**
     * Called when a sub-society is created.
     */
    interface Create extends SubSocietyChangeEvent, TaxableChangeEvent.Create {}

    /**
     * Called when a sub-society is destroyed (i.e. deleted or the society becomes independent)
     */
    interface Destroy extends SubSocietyChangeEvent, TaxableChangeEvent.Destroy {}
}
