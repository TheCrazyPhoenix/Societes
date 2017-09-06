package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.CuboidChangeEvent;
import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import org.spongepowered.api.event.cause.Cause;

public class CuboidChangeEventImpl extends ClaimChangeEventImpl implements CuboidChangeEvent {
    private Cuboid cuboid;

    public CuboidChangeEventImpl(Cause cause, Cuboid cuboid) {
        super(cause, cuboid.getParent());
        this.cuboid = cuboid;
    }

    @Override
    public Cuboid getCuboid() {
        return cuboid;
    }

    public static class Create extends CuboidChangeEventImpl implements CuboidChangeEvent.Create {
        public Create(Cause cause, Cuboid cuboid) {
            super(cause, cuboid);
        }
    }

    public static class Destroy extends CuboidChangeEventImpl implements CuboidChangeEvent.Destroy {
        public Destroy(Cause cause, Cuboid cuboid) {
            super(cause, cuboid);
        }
    }
}
