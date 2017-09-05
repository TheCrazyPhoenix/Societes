package io.github.thecrazyphoenix.societies.permission;

import io.github.thecrazyphoenix.societies.Societies;
import io.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.Society;
import io.github.thecrazyphoenix.societies.event.PermissionChangeEventImpl;
import io.github.thecrazyphoenix.societies.society.SocietyElementImpl;
import org.spongepowered.api.event.cause.Cause;

import java.util.HashMap;
import java.util.Map;

public class PermissionHolderImpl<T extends Enum<T>> extends SocietyElementImpl implements PermissionHolder<T> {
    private PermissionHolder<T> parent;
    private Map<T, PermissionState> permissions;

    public PermissionHolderImpl(Societies societies, Society society, PermissionHolder<T> parent) {
        super(societies, society);
        this.parent = parent;
        permissions = new HashMap<>();
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
        return setPermission(permission, newState, new PermissionChangeEventImpl(cause, this, permission, newState));
    }

    @Override
    public PermissionState getPermission(T permission) {
        return permissions.getOrDefault(permission, PermissionState.NONE);
    }

    protected boolean setPermission(T permission, PermissionState newState, PermissionChangeEvent event) {
        if (!societies.queueEvent(event)) {
            permissions.put(permission, newState);
            societies.onSocietyModified();
            return true;
        }
        return false;
    }
}
