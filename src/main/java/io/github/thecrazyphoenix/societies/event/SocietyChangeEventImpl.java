package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.event.SocietyChangeEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class SocietyChangeEventImpl extends AbstractEvent implements SocietyChangeEvent {
    private boolean cancelled;
    private Cause cause;
    private Society society;

    public SocietyChangeEventImpl(Cause cause, Society society) {
        this.cause = cause;
        this.society = society;
    }

    @Override
    public Society getSociety() {
        return society;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        cancelled = cancel;
    }

    @Override
    public Cause getCause() {
        return cause;
    }

    public static class Create extends SocietyChangeEventImpl implements SocietyChangeEvent.Create {
        public Create(Cause cause, Society society) {
            super(cause, society);
        }
    }

    public static class Destroy extends SocietyChangeEventImpl implements SocietyChangeEvent.Destroy {
        public Destroy(Cause cause, Society society) {
            super(cause, society);
        }
    }
}
