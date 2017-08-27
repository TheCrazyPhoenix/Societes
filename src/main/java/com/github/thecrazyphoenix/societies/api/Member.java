package com.github.thecrazyphoenix.societies.api;

import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.rank.MemberRank;
import com.github.thecrazyphoenix.societies.api.rank.Taxable;
import org.spongepowered.api.entity.living.player.User;
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
     * Retrieves the rank of this Member.
     * @return This Member's rank.
     */
    MemberRank getRank();

    /**
     * Retrieves the default title for this rank.
     * @return The title, as a Text object.
     */
    Text getTitle();

    /**
     * Sets the default title for this rank.
     * @param newTitle The new title, as a Text object.
     */
    void setTitle(Text newTitle);
}
