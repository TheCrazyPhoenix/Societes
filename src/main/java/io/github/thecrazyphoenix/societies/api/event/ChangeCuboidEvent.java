package io.github.thecrazyphoenix.societies.api.event;

import io.github.thecrazyphoenix.societies.api.society.Cuboid;

public interface ChangeCuboidEvent extends ChangeClaimEvent {
    /**
     * Retrieves the cuboid affected by this event.
     * This cuboid represents its old state, except for the Create subtype.
     * @return The modified cuboid.
     */
    Cuboid getCuboid();

    /**
     * Called when a cuboid is created.
     */
    interface Create extends ChangeCuboidEvent, ChangeSocietyElementEvent.Create {}

    /**
     * Called when a cuboid is destroyed.
     * Cuboids should not be destroyable unless the owner is inactive or nobody owns it.
     */
    interface Destroy extends ChangeCuboidEvent, ChangeSocietyElementEvent.Destroy {}
}
