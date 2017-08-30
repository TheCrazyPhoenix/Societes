package com.github.thecrazyphoenix.societies.event;

import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.api.event.PermissionChangeEvent;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import org.spongepowered.api.event.cause.Cause;

public abstract class PermissionChangeEventImpl extends SocietyChangeEventImpl implements PermissionChangeEvent {
    private PermissionState newValue;

    public PermissionChangeEventImpl(Cause cause, Society society, PermissionState newValue) {
        super(cause, society);
        this.newValue = newValue;
    }

    @Override
    public PermissionState getNewValue() {
        return newValue;
    }
}
