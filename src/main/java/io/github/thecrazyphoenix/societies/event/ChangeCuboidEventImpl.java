package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangeCuboidEvent;
import io.github.thecrazyphoenix.societies.api.society.Cuboid;
import org.spongepowered.api.event.cause.Cause;

public class ChangeCuboidEventImpl extends ChangeClaimEventImpl implements ChangeCuboidEvent {
    private Cuboid cuboid;

    public ChangeCuboidEventImpl(Cause cause, Cuboid cuboid) {
        super(cause, cuboid.getParent());
        this.cuboid = cuboid;
    }

    @Override
    public Cuboid getCuboid() {
        return cuboid;
    }

    public static class Create extends ChangeCuboidEventImpl implements ChangeCuboidEvent.Create {
        public Create(Cause cause, Cuboid cuboid) {
            super(cause, cuboid);
        }
    }

    public static class Destroy extends ChangeCuboidEventImpl implements ChangeCuboidEvent.Destroy {
        public Destroy(Cause cause, Cuboid cuboid) {
            super(cause, cuboid);
        }
    }
}
