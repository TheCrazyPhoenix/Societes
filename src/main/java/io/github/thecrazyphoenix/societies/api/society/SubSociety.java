package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import org.spongepowered.api.event.cause.Cause;

import java.math.BigDecimal;
import java.util.Optional;

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

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    /**
     * Enables the construction of a new object.
     */
    interface Builder {
        /**
         * Sets the created sub-society's society (as returned by {@link SubSociety#toSociety()}).
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder subSociety(Society subSociety);

        /**
         * Sets the created sub-society's fixed tax.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder fixedTax(BigDecimal fixedTax);

        /**
         * Sets the created sub-society's salary.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder salary(BigDecimal salary);

        /**
         * Sets the created sub-society's given permission to the given value.
         * All permissions default to {@link PermissionState#NONE}.
         * @return This object for chaining.
         */
        Builder permission(SocietyPermission permission, PermissionState value);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends SubSociety> build(Cause cause);
    }
}
