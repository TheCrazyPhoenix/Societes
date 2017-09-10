package io.github.thecrazyphoenix.societies.permission;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.ChangePermissionEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.event.ChangePermissionEventImpl;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;
import org.spongepowered.api.event.cause.Cause;

public class DefaultPermissionHolder<T extends Enum<T>> extends AbstractPermissionHolder<T> {
    private DefaultPermissionHolder(DefaultPermissionHolder.Builder<T> builder) {
        super(builder);
    }

    @Override
    protected ChangePermissionEvent createEvent(T permission, PermissionState newState, Cause cause) {
        return new ChangePermissionEventImpl(cause, this, permission, newState);
    }

    public static <P extends Enum<P>> Builder<P> builder(Societies societies, SocietyImpl society, PermissionHolder<P> parent) {
        return new Builder<>(societies, society, parent);
    }

    public static class Builder<P extends Enum<P>> extends AbstractPermissionHolder.Builder<Builder<P>, P> {
        private Builder(Societies societies, SocietyImpl society, PermissionHolder<P> parent) {
            super(societies, society, parent);
        }

        public DefaultPermissionHolder<P> build() {
            return new DefaultPermissionHolder<>(this);
        }
    }
}
