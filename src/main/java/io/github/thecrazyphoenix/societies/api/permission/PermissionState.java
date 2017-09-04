package io.github.thecrazyphoenix.societies.api.permission;

/**
 * Stores the different states permissions can be in.
 */
public enum PermissionState {
    /**
     * The associated permission is set.
     */
    TRUE,

    /**
     * The associated permission is unset.
     */
    FALSE,

    /**
     * The associated permission is unaltered (i.e. inherited from rank).
     * By default, the permission is FALSE.
     */
    NONE
}
