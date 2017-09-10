package io.github.thecrazyphoenix.societies.event;

import io.github.thecrazyphoenix.societies.api.event.ChangePermissionEvent;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.society.SocietyElement;
import org.spongepowered.api.event.cause.Cause;

public class ChangePermissionEventImpl extends ChangeSocietyElementEventImpl implements ChangePermissionEvent {
    private Enum<?> permission;
    private PermissionState newValue;

    public ChangePermissionEventImpl(Cause cause, SocietyElement element, Enum<?> permission, PermissionState newValue) {
        this(cause, element, newValue);
        this.permission = permission;
    }

    protected ChangePermissionEventImpl(Cause cause, SocietyElement element, PermissionState newValue) {
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
