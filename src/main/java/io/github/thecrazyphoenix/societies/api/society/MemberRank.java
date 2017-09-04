package io.github.thecrazyphoenix.societies.api.society;

import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Stores the default values for a player's rank.
 * This shouldn't be used to get a Member's values, instead, use the equivalent method in {@link Member}
 */
public interface MemberRank extends Taxable, PermissionHolder<MemberPermission> {
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
     * Sets the parent rank of this rank.
     * @param newParent The new parent. If null, this rank will be set to have no parent.
     * @param cause The cause of this modification.
     * @return True if the modification took place, false otherwise.
     */
    boolean setParent(MemberRank newParent, Cause cause);

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

    @Override
    default Account getAccount() {
        throw new UnsupportedOperationException("attempt to get member rank account");
    }
}
