package com.github.thecrazyphoenix.societies.api.permission;

import org.spongepowered.api.event.cause.Cause;

/**
 * Models an object that can have permissions.
 * @param <T> The type of permissions the object can have.
 */
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

    /**
     * Retrieves a permission for this object.
     * This method should only be used for storing this object.
     * @param permission The permission value to use.
     * @return The permission state this container stores.
     */
    PermissionState getPermission(T permission);
}
