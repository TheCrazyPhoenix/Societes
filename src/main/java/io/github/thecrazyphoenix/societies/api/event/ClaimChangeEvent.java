package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.Claim;

import java.math.BigDecimal;

/**
 * Base event for when information related to a claim changes (new, destroy, rezone, member claims)
 */
public interface ClaimChangeEvent extends SocietyElementChangeEvent {
    /**
     * Retrieves the claim affected by this event.
     * This object will represent the old state except for the {@link Create} event.
     * @return The affected claim.
     */
    Claim getClaim();

    /**
     * Called when a claim is created.
     */
    interface Create extends ClaimChangeEvent, SocietyElementChangeEvent.Create {}

    /**
     * Called when a claim is destroyed (i.e. removed or entirely stolen)
     */
    interface Destroy extends ClaimChangeEvent, SocietyElementChangeEvent.Destroy {}

    /**
     * Called when a claim changes in volume (i.e. modified size or partially stolen).
     */
    interface ChangeVolume extends ClaimChangeEvent {
        /**
         * Retrieves the new volume of the modified claim.
         * @return The new volume in blocks (i.e. cubic metres)
         */
        int getNewVolume();
    }

    /**
     * Called when a claim's land value changes.
     */
    interface ChangeLandValue extends ClaimChangeEvent {
        /**
         * Retrieves the new land value of the claim.
         * @return The new land value per block.
         */
        BigDecimal newLandValue();
    }

    /**
     * Called when a claim's land tax rate changes.
     */
    interface ChangeLandTax extends ClaimChangeEvent {
        /**
         * Retrieves the new land tax rate of the claim.
         * @return The new land tax rate per block per day
         */
        BigDecimal newLandTax();
    }
}
