package com.github.thecrazyphoenix.societies.api.permission;

public interface PermissionHolder<T extends Enum<T>> {
    /**
     * Checks if the given permission is granted for this object.
     * This method will automatically inherit permissions using an algorithm of the implementation's choice.
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    boolean hasPermission(MemberPermission permission);

    /**
     * Sets a permission for this object.
     * @param permission The permission to set.
     * @param newState The value the permission should be changed to.
     */
    void setPermission(MemberPermission permission, PermissionState newState);
}
