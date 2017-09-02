package com.github.thecrazyphoenix.societies.land;

import com.flowpowered.math.vector.Vector3i;
import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.land.Cuboid;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.society.SocietyElementImpl;
import com.github.thecrazyphoenix.societies.util.CommonMethods;

public class CuboidImpl extends SocietyElementImpl implements Cuboid {
    private Vector3i corner1;
    private Vector3i corner2;

    public CuboidImpl(Societies societies, Society society, Vector3i corner1, Vector3i corner2) {
        super(societies, society);
        this.corner1 = corner1;
        this.corner2 = corner2;
    }

    @Override
    public boolean isClaimed(Vector3i block) {
        return corner1.equals(block.min(corner1)) && corner2.equals(block.max(corner2));
    }

    @Override
    public long getClaimedVolume() {
        return CommonMethods.getVolume(corner1, corner2);
    }

    @Override
    public boolean isIntersecting(Cuboid cuboid) {
        return cuboid.isContaining(this) || isClaimed(cuboid.getFirstCorner()) || isClaimed(cuboid.getSecondCorner());
    }

    @Override
    public long getIntersectingVolume(Cuboid cuboid) {
        if (cuboid.isContaining(this)) {
            return getClaimedVolume();
        } else if (isClaimed(cuboid.getFirstCorner())) {
            return CommonMethods.getVolume(cuboid.getFirstCorner(), corner2);
        } else if (isClaimed(cuboid.getSecondCorner())) {
            return CommonMethods.getVolume(corner1, cuboid.getSecondCorner());
        }
        return 0L;
    }

    @Override
    public boolean isContaining(Cuboid cuboid) {
        return corner1.equals(cuboid.getFirstCorner().min(corner1)) && corner2.equals(cuboid.getSecondCorner().max(corner2));
    }

    @Override
    public Vector3i getFirstCorner() {
        return corner1;
    }

    @Override
    public Vector3i getSecondCorner() {
        return corner2;
    }
}
