package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.economy.ContractAuthority;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Stores the default values for a player's rank.
 * This shouldn't be used to get a Member's values, instead, use the equivalent method in {@link Member}
 */
public interface MemberRank extends ContractAuthority, PermissionHolder<MemberPermission>, SocietyElement {
    /**
     * Retrieves the unique identifier of this member rank.
     * @return The identifier as a string.
     */
    String getIdentifier();

    /**
     * Retrieves the parent rank of this rank.
     * If there is no parent, then this is the society's leaders' rank.
     * @return The parent if it exists, otherwise {@link Optional#empty()}.
     */
    Optional<MemberRank> getParent();

    /**
     * Retrieves the children ranks of this rank.
     * @return The children as an unmodifiable string-indexed map.
     */
    Map<String, MemberRank> getChildren();

    /**
     * Retrieves the members that have this rank.
     * @return The members as an unmodifiable UUID-indexed map.
     */
    Map<UUID, Member> getMembers();

    /**
     * Retrieves the default title for this rank.
     * The value must follow the same conditions as a society name.
     * @return The title, as a Text object.
     * @see Society#getName()
     */
    Text getTitle();

    /**
     * Sets the default title for this rank.
     * @param newTitle The new default title, as a Text object.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setTitle(Text newTitle, Cause cause);

    /**
     * Retrieves the description of this rank.
     * @return The description, as a Text object.
     */
    Text getDescription();

    /**
     * Sets the description of this rank.
     * @param newDescription The new description as a Text object.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setDescription(Text newDescription, Cause cause);

    // TODO Add method to view and edit the payments

    /**
     * Creates a new rank builder with this rank as the parent.
     * @return The created builder.
     * @see Society#rankBuilder()
     */
    Builder rankBuilder();

    /**
     * Creates a new member builder with this rank as the rank.
     * @return The created builder.
     */
    Member.Builder memberBuilder();

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
         * Sets the created rank's title.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder title(Text title);

        /**
         * Sets the created rank's description.
         * This parameter defaults to {@link Text#EMPTY}
         * @return This object for chaining.
         */
        Builder description(Text description);

        /**
         * Adds the given arguments to the list of contracts to impose upon each member with this rank.
         * @param name The name of the transfer.
         * @param currency The currency of the transfer.
         * @param amount The amount to transfer from the society to the member (negative to have the member transfer to the society).
         * @param interval The interval of the transfer.
         * @param unit The unit of the interval.
         * @return This object for chaining.
         */
        Builder addPayment(String name, Currency currency, BigDecimal amount, long interval, TimeUnit unit);

        /**
         * Sets the created rank's given permission to the given value.
         * All permissions default to {@link PermissionState#NONE} if the created rank has a parent, or {@link PermissionState#TRUE} if it doesn't.
         * @return This object for chaining.
         */
        Builder permission(MemberPermission permission, PermissionState value);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends MemberRank> build(Cause cause);
    }
}
