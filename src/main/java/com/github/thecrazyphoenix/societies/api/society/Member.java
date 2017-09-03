package com.github.thecrazyphoenix.societies.api.society;

import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;

/**
 * A member of a society.
 */
public interface Member extends Taxable, PermissionHolder<MemberPermission> {
    /**
     * Retrieves the user associated with this object.
     * Several Member objects can be associated with the same user.
     * @return The associated user.
     */
    User getUser();

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
}
