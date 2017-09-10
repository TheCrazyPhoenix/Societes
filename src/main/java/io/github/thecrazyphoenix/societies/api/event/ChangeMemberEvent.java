package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.society.Member;
import io.github.thecrazyphoenix.societies.api.society.MemberRank;
import org.spongepowered.api.text.Text;

/**
 * Base event for when a member is created (i.e. joins), is destroyed (i.e. leaves), changes rank, changes permissions (except through inheritance)
 * Tax and salary change will not trigger this event.
 *
 * @see ChangeAccountHolderEvent
 */
public interface ChangeMemberEvent extends ChangeAccountHolderEvent, ChangeSocietyElementEvent {
    /**
     * Retrieves the member modified by this event.
     * This object represents the member before its modified state, except for the {@link Create} event.
     * @return The modified member.
     */
    Member getMember();

    /**
     * Called when a member is created, i.e. when a player joins a society.
     */
    interface Create extends ChangeMemberEvent, ChangeAccountHolderEvent.Create {}

    /**
     * Called when a member is destroyed, i.e. when a player leaves a society, be it by choice or by force.
     */
    interface Destroy extends ChangeMemberEvent, ChangeAccountHolderEvent.Destroy {}

    /**
     * Called when a member's rank is changed.
     */
    interface ChangeRank extends ChangeMemberEvent {
        /**
         * Retrieves the player's new rank.
         * @return The rank the player will become.
         */
        MemberRank getNewRank();
    }

    /**
     * Called when a player's title is changed.
     * Changes in member rank titles will never trigger this event.
     */
    interface ChangeTitle extends ChangeMemberEvent {
        /**
         * Retrieves the player's new title.
         * @return The new title as a Text object.
         */
        Text getNewTitle();
    }

    /**
     * Called when a member's permissions are changed.
     * This will not be called if the member's rank or their rank's permissions are changed, even if the inherited permissions change.
     * This will not be called if the member's claim-specific permissions are changed. {@see ChangeClaimPermission}
     */
    interface ChangePermission extends ChangeMemberEvent, ChangePermissionEvent {
        /**
         * Retrieves the permission affected by this event.
         * @return The permission as a MemberPermission.
         */
        @Override
        MemberPermission getChangedPermission();
    }

    /**
     * Called when a member's permissions inside a member claim are changed.
     */
    interface ChangeClaimPermission extends ChangeMemberEvent, ChangeMemberClaimEvent, ChangePermissionEvent {
        /**
         * Retrieves the permission affected by this event.
         * @return The permission as a ClaimPermission.
         */
        @Override
        ClaimPermission getChangedPermission();
    }
}
