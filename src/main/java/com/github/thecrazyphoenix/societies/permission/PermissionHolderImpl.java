package com.github.thecrazyphoenix.societies.permission;

import com.github.thecrazyphoenix.societies.Societies;
import com.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.event.PermissionChangeEventImpl;
import com.github.thecrazyphoenix.societies.society.SocietyElementImpl;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.event.cause.Cause;

import java.util.Map;

public class PermissionHolderImpl<T extends Enum<T>> extends SocietyElementImpl implements PermissionHolder<T> {
    private PermissionHolder<T> parent;
    private Map<T, PermissionState> permissions;

    public PermissionHolderImpl(Societies societies, Society society, PermissionHolder<T> parent) {
        super(societies, society);
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
        return setPermission(permission, newState, new PermissionChangeEventImpl(cause, society, permission, newState));
    }

    protected boolean setPermission(T permission, PermissionState newState, PermissionChangeEvent event) {
        if (!societies.queueEvent(event)) {
            permissions.put(permission, newState);
            return true;
        }
        return false;
    }
}
