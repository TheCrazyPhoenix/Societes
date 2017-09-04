package io.github.thecrazyphoenix.societies.api.permission;

/**
 * Stores the list of permissions a sub-society can have
 */
public enum SocietyPermission {
    /**
     * Allows the sub-society to view the society's members.
     */
    VIEW_MEMBERS,

    /**
     * Allows the sub-society to view the society's sub-societies.
     */
    VIEW_SUBSOCIETIES,

    /**
     * Allows the sub-society to change its identity, such as its name.
     */
    CHANGE_IDENTITY,

    /**
     * Allows the sub-society to freely claim territory inside the society's territory.
     */
    MANAGE_TERRITORY
}
