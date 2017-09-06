package io.github.thecrazyphoenix.societies.api.society;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;

/**
 * Represents a cuboid.
 * Exists mainly for the purpose of compatibility between claim implementations.
 */
public interface Cuboid extends ClaimedLand {
    /**
     * Retrieves the claim this cuboid is in.
     * @return The parent of this claim.
     */
    Claim getParent();
    /**
     * Retrieves the first corner of this cuboid.
     * All coordinates in this corner must be <= to those in the second corner.
     * @return The first corner as a Vector3i.
     */
    Vector3i getFirstCorner();

    /**
     * Retrieves the second corner of this cuboid.
     * All coordinates in this corner must be >= to those in the first corner.
     * @return The second corner as a Vector3i.
     */
    Vector3i getSecondCorner();

    /**
     * Attempts to destroy this object.
     * @param cause The cause of the construction of the object.
     * @return True if the object was destroyed, false if the event was cancelled.
     */
    boolean destroy(Cause cause);

    /**
     * Enables the construction of a new object.
     */
    interface Builder {
        /**
         * Sets the created cuboid's corners.
         * The created cuboid may not have the same corners, but it must represent the same region of space.
         * This parameter is mandatory.
         * @return This object for chaining.
         */
        Builder corners(Vector3i corner1, Vector3i corner2);

        /**
         * Constructs and registers the object.
         * @param cause The cause of the construction of the object.
         * @return The created object, or {@link Optional#empty()} if the creation event was cancelled.
         * @throws IllegalStateException If mandatory parameters have not been set.
         */
        Optional<? extends Cuboid> build(Cause cause);
    }
}
