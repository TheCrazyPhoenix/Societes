package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.permission.PermissionState;

/**
 * Base event for when a permission is modified.
 */
public interface ChangePermissionEvent extends ChangeSocietyElementEvent {
    /**
     * Retrieves the permission modified by this event.
     * @return The modified permission as an enum object.
     */
    Enum<?> getChangedPermission();

    /**
     * Retrieves the permission's new value
     * @return The new value as a permission state.
     */
    PermissionState getNewValue();
}
