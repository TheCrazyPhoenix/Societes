package com.github.thecrazyphoenix.societies.api.society;

import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Stores the default values for a player's rank.
 * This shouldn't be used to get a Member's values, instead, use the equivalent method in {@link Member}
 */
public interface MemberRank extends Taxable, PermissionHolder<MemberPermission> {
    /**
     * Retrieves the parent rank of this rank.
     * If there is no parent, then this is the society's leaders' rank.
     * @return The parent if it exists, otherwise {@link Optional#empty()}.
     */
    Optional<MemberRank> getParent();

    /**
     * Sets the parent rank of this rank.
     * @param newParent The new parent. If null, this rank will be set to have no parent.
     */
    void setParent(MemberRank newParent);

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
     */
    void setTitle(Text newTitle);

    /**
     * Retrieves the description of this rank.
     * @return The description, as a Text object.
     */
    Text getDescription();

    /**
     * Sets the description of this rank.
     * @param newDescription The new description as a Text object.
     */
    void setDescription(Text newDescription);

    @Override
    default Account getAccount() {
        throw new UnsupportedOperationException("attempt to get member rank account");
    }
}
