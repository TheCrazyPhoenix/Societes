package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.SubSociety;

public interface SubSocietyChangeEvent extends SocietyChangeEvent {
    @Override
    SubSociety getSociety();

    /**
     * Called when a sub-society is created or a society becomes a sub-society.
     */
    interface Create extends SubSocietyChangeEvent, TaxableChangeEvent.Create {}

    /**
     * Called when a sub-society is destroyed or becomes independent.
     */
    interface Destroy extends SubSocietyChangeEvent, TaxableChangeEvent.Destroy {}
}
