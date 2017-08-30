package com.github.thecrazyphoenix.societies.api.event;

import com.github.thecrazyphoenix.societies.api.land.MemberClaim;

/**
 * Base event for when a member claim is modified.
 */
public interface MemberClaimChangeEvent extends ClaimChangeEvent {
    /**
     * Retrieves the member claim affected by this event.
     * This member claim represents its old state, except for the Create subtype.
     * @return The member claim.
     */
    MemberClaim getMemberClaim();

    /**
     * Called when a member claim is created.
     */
    interface Create extends MemberClaimChangeEvent {}

    /**
     * Called when a member claim is destroyed.
     * Member claims should not be destroyable unless the owner is inactive or nobody owns it.
     */
    interface Destroy extends MemberClaimChangeEvent {}

    /**
     * Called when a member buys a member claim from a society.
     */
    interface Buy extends MemberClaimChangeEvent {}

    /**
     * Called when a member sells a member claim to a society.
     * When this event occurs, the member claim is available for repurchase or destruction.
     */
    interface Sell extends MemberClaimChangeEvent {}
}
