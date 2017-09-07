package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.SubSociety;

public interface SubSocietyChangeEvent extends AccountHolderChangeEvent {
    /**
     * Retrieves the sub-society affected by this event.
     * This represents the old state of the sub-society, except for the Create event.
     * @return The sub-society.
     */
    SubSociety getSubSociety();

    /**
     * Called when a sub-society is created.
     */
    interface Create extends SubSocietyChangeEvent, AccountHolderChangeEvent.Create {}

    /**
     * Called when a sub-society is destroyed (i.e. deleted or the society becomes independent)
     */
    interface Destroy extends SubSocietyChangeEvent, AccountHolderChangeEvent.Destroy {}

    interface ChangePermission extends SubSocietyChangeEvent, PermissionChangeEvent {}
}
