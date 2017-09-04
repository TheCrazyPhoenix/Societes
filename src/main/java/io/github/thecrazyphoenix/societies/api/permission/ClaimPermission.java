package io.github.thecrazyphoenix.societies.api.permission;

/**
 * Models a permission that can be assigned for each claim and member claim.
 */
public enum ClaimPermission {
    /**
     * Allows the subject to purchase member claims.
     * This permission is ignored for societies.
     */
    BUY_LAND,

    /**
     * Allows the subject to interact with blocks and view tile-entities.
     */
    INTERACT,

    /**
     * Allows the subject to place and break blocks, and modify tile-entities.
     */
    BUILD
}
