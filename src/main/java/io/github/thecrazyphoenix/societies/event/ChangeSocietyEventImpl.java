package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.api.event.ChangeSocietyEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;

public class ChangeSocietyEventImpl extends AbstractEvent implements ChangeSocietyEvent {
    private boolean cancelled;
    private Cause cause;
    private Society society;

    public ChangeSocietyEventImpl(Cause cause, Society society) {
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

    public static class Create extends ChangeSocietyEventImpl implements ChangeSocietyEvent.Create {
        public Create(Cause cause, Society society) {
            super(cause, society);
        }
    }

    public static class Destroy extends ChangeSocietyEventImpl implements ChangeSocietyEvent.Destroy {
        public Destroy(Cause cause, Society society) {
            super(cause, society);
        }
    }
}
