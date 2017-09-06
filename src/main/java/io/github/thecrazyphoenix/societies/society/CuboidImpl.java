package io.github.thecrazyphoenix.societies.society;

import com.flowpowered.math.vector.Vector3i;
import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.society.Claim;
import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import io.github.thecrazyphoenix.societies.event.CuboidChangeEventImpl;
import io.github.thecrazyphoenix.societies.society.internal.SocietyElementImpl;
import io.github.thecrazyphoenix.societies.util.CommonMethods;
import org.spongepowered.api.event.cause.Cause;

import java.util.Optional;

public class CuboidImpl extends SocietyElementImpl implements Cuboid {
    protected ClaimImpl parent;
    private Vector3i corner1;
    private Vector3i corner2;

    protected CuboidImpl(CuboidImpl.Builder builder) {
        super(builder.societies, builder.parent.getSociety());
        parent = builder.parent;
        corner1 = builder.corner1.min(builder.corner2);
        corner2 = builder.corner1.max(builder.corner2);
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
    public Claim getParent() {
        return parent;
    }

    @Override
    public Vector3i getFirstCorner() {
        return corner1;
    }

    @Override
    public Vector3i getSecondCorner() {
        return corner2;
    }

    @Override
    public boolean destroy(Cause cause) {
        if (!societies.queueEvent(new CuboidChangeEventImpl.Destroy(cause, this))) {
            parent.getClaimCuboidsRaw().remove(this);
            return true;
        }
        return false;
    }

    public static class Builder implements Cuboid.Builder {
        private final Societies societies;
        private final ClaimImpl parent;
        private Vector3i corner1;
        private Vector3i corner2;

        Builder(Societies societies, ClaimImpl parent) {
            this.societies = societies;
            this.parent = parent;
        }

        @Override
        public Builder corners(Vector3i corner1, Vector3i corner2) {
            this.corner1 = corner1;
            this.corner2 = corner2;
            return this;
        }

        @Override
        public Optional<? extends CuboidImpl> build(Cause cause) {
            build();
            CuboidImpl cuboid = new CuboidImpl(this);
            if (!societies.queueEvent(new CuboidChangeEventImpl.Create(cause, cuboid))) {
                parent.getClaimCuboidsRaw().add(cuboid);
                societies.onSocietyModified();
                return Optional.of(cuboid);
            }
            return Optional.empty();
        }

        public SocietyImpl getSociety() {
            return parent.getSociety();
        }

        protected void build() {
            CommonMethods.checkNotNullState(corner1, "corner1 is mandatory");
            CommonMethods.checkNotNullState(corner2, "corner2 is mandatory");
        }
    }
}
