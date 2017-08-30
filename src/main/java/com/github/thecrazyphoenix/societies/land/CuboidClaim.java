package com.github.thecrazyphoenix.societies.land;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.society.SocietyElementImpl;

public class CuboidClaim extends SocietyElementImpl {
    private Vector3i corner1;
    private Vector3i corner2;

    public CuboidClaim(Society society) {
        super(society);
        corner1 = Vector3i.ZERO;
        corner2 = Vector3i.ZERO;
    }

    public void setCorners(Vector3i a, Vector3i b) {
        corner1 = a;
        corner2 = b;
    }

    public boolean isClaimed(Vector3i block) {
        return corner1.equals(block.min(corner1)) && corner2.equals(block.max(corner2));
    }

    public int getClaimedVolume() {
        return (corner2.getX() - corner1.getX()) * (corner2.getY() - corner1.getY()) * (corner2.getZ() - corner1.getZ());
    }
}
