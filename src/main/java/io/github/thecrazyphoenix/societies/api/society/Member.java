package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

/**
 * A member of a society.
 */
public interface Member extends Taxable, PermissionHolder<MemberPermission> {
    /**
     * Retrieves the user associated with this object.
     * Several Member objects can be associated with the same user.
     * @return The associated user's UUID.
     */
    UUID getUser();

    /**
     * Retrieves the rank of this member.
     * @return This Member's rank.
     */
    MemberRank getRank();

    /**
     * Retrieves the default title for this member.
     * @return The title, as a Text object.
     */
    Text getTitle();

    /**
     * Sets the default title for this member.
     * @param newTitle The new title, as a Text object.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setTitle(Text newTitle, Cause cause);

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
         * Sets the created member's user UUID.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder user(UUID user);

        /**
         * Sets the created member's title.
         * This parameter defaults to {@link MemberRank#getTitle()} on the rank object.
         * @return This object for chaining.
         */
        Builder title(Text title);

        /**
         * Sets the created member's fixed tax.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder fixedTax(BigDecimal fixedTax);

        /**
         * Sets the created member's salary.
         * This parameter defaults to {@link BigDecimal#ZERO}
         * @return This object for chaining.
         */
        Builder salary(BigDecimal salary);

        /**
         * Sets the created member's given permission to the given value.
         * All permissions default to {@link PermissionState#NONE}.
         * @return This object for chaining.
         */
        Builder permission(MemberPermission permission, PermissionState value);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Member> build(Cause cause);
    }
}
