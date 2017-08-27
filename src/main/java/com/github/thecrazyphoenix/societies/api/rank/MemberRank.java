package com.github.thecrazyphoenix.societies.api.rank;

import com.github.thecrazyphoenix.societies.api.Member;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Stores the default values for a player's rank.
 * This shouldn't be used to get a Member's values, instead, use the equivalent method in {@link Member}
 */
public interface MemberRank extends Taxable {
    /**
     * Retrieves the parent rank of this rank.
     * If there is no parent, then this is the society's leaders' rank.
     * @return The parent if it exists, otherwise {@link Optional#empty()}.
     */
    Optional<MemberRank> getParent();

    /**
     * Retrieves the default title for this rank.
     * @return The title, as a Text object.
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

    /**
     * Checks if the given permission is granted for this rank.
     * @param permission The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    boolean hasPermission(MemberPermission permission);

    /**
     * Sets a permission for this rank.
     * @param permission The permission to set.
     * @param newState The value the permission should be changed to.
     */
    void setPermission(MemberPermission permission, PermissionState newState);
}
