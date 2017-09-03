package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.society.SocietyElement;
import org.spongepowered.api.event.cause.Cause;

public class PermissionChangeEventImpl extends SocietyElementChangeEventImpl implements PermissionChangeEvent {
    private Enum<?> permission;
    private PermissionState newValue;

    public PermissionChangeEventImpl(Cause cause, SocietyElement element, Enum<?> permission, PermissionState newValue) {
        this(cause, element, newValue);
        this.permission = permission;
    }

    protected PermissionChangeEventImpl(Cause cause, SocietyElement element, PermissionState newValue) {
        super(cause, element);
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
