package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;

/**
 * Contains information between a society and one of its sub-societies.
 */
public interface SubSociety extends Taxable, PermissionHolder<SocietyPermission> {
    /**
     * Retrieves the society wrapped by this object.
     * The owner can be retrieved using {@link #getSociety()}
     * @return The society wrapped by this object.
     */
    Society toSociety();
}
