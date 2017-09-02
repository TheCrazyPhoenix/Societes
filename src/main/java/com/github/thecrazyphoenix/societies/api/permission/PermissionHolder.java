package com.github.thecrazyphoenix.societies.api.permission;

import org.spongepowered.api.event.cause.Cause;

public interface PermissionHolder<T extends Enum<T>> {
    /**
     * Checks if the given permission is granted for this object.
     * This method will automatically inherit permissions using an algorithm of the implementation's choice.
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    boolean hasPermission(T permission);

    /**
     * Sets a permission for this object.
     * @param permission The permission to set.
     * @param newState The value the permission should be changed to.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setPermission(T permission, PermissionState newState, Cause cause);
}
