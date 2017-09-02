package com.github.thecrazyphoenix.societies.api.land;

import com.flowpowered.math.vector.Vector3i;

/**
 * Represents a cuboid.
 * Exists mainly for the purpose of compatibility between claim implementations.
 */
public interface Cuboid extends ClaimedLand {
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
}
