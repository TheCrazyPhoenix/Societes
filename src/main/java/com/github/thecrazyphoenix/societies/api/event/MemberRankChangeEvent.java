package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.text.Text;

/**
 * Base event for when a member rank's title, description or permissions change.
 */
public interface MemberRankChangeEvent extends SocietyChangeEvent {
    /**
     * Retrieves the member rank affected by this event.
     * This will always represent the old state of the member rank, except for the {@link Create} event.
     * @return The modified member rank.
     */
    MemberRank getMemberRank();

    /**
     * Called when a member rank is created.
     */
    interface Create extends MemberRankChangeEvent, TaxableChangeEvent.Create {}

    /**
     * Called when a member rank is destroyed.
     */
    interface Destroy extends MemberRankChangeEvent, TaxableChangeEvent.Destroy {}

    /**
     * Called when a member rank's title is changed.
     */
    interface ChangeTitle extends MemberRankChangeEvent {
        /**
         * Retrieves the member rank's new title.
         * @return The new title as a Text object.
         */
        Text getNewTitle();
    }

    /**
     * Called when a member rank's description is changed.
     */
    interface ChangeDescription extends MemberRankChangeEvent {
        /**
         * Retrieves the member rank's new description.
         */
        Text getNewDescription();
    }

    /**
     * Called when a member rank's permissions change.
     * Claim permission changes will not call this event.
     */
    interface ChangePermission extends MemberRankChangeEvent, PermissionChangeEvent {
        /**
         * Retrieves the permission affected by this event.
         * @return The permission as a MemberPermission.
         */
        @Override
        MemberPermission getChangedPermission();
    }

    /**
     * Called when a member rank's claim permissions change.
     */
    interface ChangeClaimPermission extends MemberRankChangeEvent, ClaimChangeEvent, PermissionChangeEvent {
        /**
         * Retrieves the permission affected by this event.
         * @return The permission as a MemberPermission.
         */
        @Override
        ClaimPermission getChangedPermission();
    }
}
