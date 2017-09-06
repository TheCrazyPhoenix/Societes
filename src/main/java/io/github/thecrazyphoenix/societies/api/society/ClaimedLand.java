package io.github.thecrazyphoenix.societies.api.society;

import com.flowpowered.math.vector.Vector3i;

public interface ClaimedLand {
    /**
     * Checks if the given block is inside the region defined by this cuboid (corners included).
     * @param block The block to check.
     * @return True if it is within the cuboid, false otherwise.
     */
    boolean isClaimed(Vector3i block);

    /**
     * Retrieves the volume of this cuboid.
     * @return The volume of the cuboid in blocks (i.e. cubic metres).
     */
    long getClaimedVolume();

    /**
     * Checks if the given cuboid overlaps this claimed land.
     * @param cuboid The cuboid to check.
     * @return True if there exists a block at position pos such that this.isClaimed(pos) and cuboid.isClaimed(pos) return true, false otherwise.
     */
    boolean isIntersecting(Cuboid cuboid);

    /**
     * Retrieves the volume of the intersection between this and the given cuboid.
     * @param cuboid The cuboid to intersect with this.
     * @return The volume in blocks (i.e. cubic metres)
     */
    long getIntersectingVolume(Cuboid cuboid);

    /**
     * Checks if the given cuboid is entirely contained within this object.
     * If this returns true, then {@link #isIntersecting(Cuboid)} also returns true.
     * @param cuboid The cuboid to check.
     * @return True if for all blocks to which cuboid.isClaimed return true, this.isClaimed also returns true, false otherwise.
     */
    boolean isContaining(Cuboid cuboid);
}
