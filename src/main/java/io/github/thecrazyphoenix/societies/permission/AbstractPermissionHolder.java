package io.github.thecrazyphoenix.societies.permission;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.society.SocietyImpl;
import io.github.thecrazyphoenix.societies.society.internal.SocietyElementImpl;
import org.spongepowered.api.event.cause.Cause;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractPermissionHolder<T extends Enum<T>> extends SocietyElementImpl implements PermissionHolder<T> {
    private PermissionHolder<T> parent;
    private Map<T, PermissionState> permissions;

    protected AbstractPermissionHolder(Builder<?, T> builder) {
        super(builder.societies, builder.society);
        parent = builder.parent;
        permissions = new HashMap<>(builder.permissions);
    }

    @Override
    public boolean hasPermission(T permission) {
        switch (permissions.getOrDefault(permission, PermissionState.NONE)) {
            case NONE:
                return parent.hasPermission(permission);
            case TRUE:
                return true;
            case FALSE:
            default:        // In case the enum is modified (otherwise there's a compilation error)
                return false;
        }
    }

    @Override
    public boolean setPermission(T permission, PermissionState newState, Cause cause) {
        if (!societies.queueEvent(createEvent(permission, newState, cause))) {
            permissions.put(permission, newState);
            societies.onSocietyModified();
            return true;
        }
        return false;
    }

    @Override
    public PermissionState getPermission(T permission) {
        return permissions.getOrDefault(permission, PermissionState.NONE);
    }

    protected abstract PermissionChangeEvent createEvent(T permission, PermissionState newState, Cause cause);

    @SuppressWarnings("unchecked")
    public static class Builder<T, P extends Enum<P>> {
        private final Societies societies;
        private final SocietyImpl society;
        private final PermissionHolder<P> parent;
        private Map<P, PermissionState> permissions;

        protected Builder(Societies societies, SocietyImpl society, PermissionHolder<P> parent) {
            this.societies = societies;
            this.society = society;
            this.parent = parent;
            permissions = new HashMap<>();
        }

        public T permission(P permission, PermissionState value) {
            permissions.put(permission, value);
            return (T) this;
        }

        public SocietyImpl getSociety() {
            return society;
        }
    }
}
