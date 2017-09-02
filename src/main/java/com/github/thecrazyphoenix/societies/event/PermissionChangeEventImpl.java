package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import org.spongepowered.api.event.cause.Cause;

public class PermissionChangeEventImpl extends SocietyChangeEventImpl implements PermissionChangeEvent {
    private Enum<?> permission;
    private PermissionState newValue;

    public PermissionChangeEventImpl(Cause cause, Society society, Enum<?> permission, PermissionState newValue) {
        this(cause, society, newValue);
        this.permission = permission;
    }

    protected PermissionChangeEventImpl(Cause cause, Society society, PermissionState newValue) {
        super(cause, society);
        this.newValue = newValue;
    }

    @Override
    public Enum<?> getChangedPermission() {
        return permission;
    }

    @Override
    public PermissionState getNewValue() {
        return newValue;
    }
}
