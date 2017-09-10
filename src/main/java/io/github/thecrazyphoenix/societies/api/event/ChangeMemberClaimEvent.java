package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.MemberClaim;
import io.github.thecrazyphoenix.societies.api.society.Member;

/**
 * Base event for when a member claim is modified.
 */
public interface ChangeMemberClaimEvent extends ChangeCuboidEvent {
    /**
     * Retrieves the member claim affected by this event.
     * This member claim represents its old state, except for the Create subtype.
     * @return The member claim.
     */
    MemberClaim getMemberClaim();

    /**
     * Called when a member claim is created.
     */
    interface Create extends ChangeMemberClaimEvent, ChangeCuboidEvent.Create {}

    /**
     * Called when a member claim is destroyed.
     * Member claims should not be destroyable unless the owner is inactive or nobody owns it.
     */
    interface Destroy extends ChangeMemberClaimEvent, ChangeCuboidEvent.Destroy {}

    /**
     * Called when a member claim changes owner.
     */
    interface ChangeOwner extends ChangeMemberClaimEvent {
        /**
         * Retrieves the member claim's new owner.
         * @return The new owner.
         */
        Member getNewOwner();
    }
}
