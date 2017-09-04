package io.github.thecrazyphoenix.societies.permission;

import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import org.spongepowered.api.event.cause.Cause;

public class PowerlessPermissionHolder<T extends Enum<T>> implements PermissionHolder<T> {
    public static final PowerlessPermissionHolder<MemberPermission> MEMBER = new PowerlessPermissionHolder<>();
    public static final PowerlessPermissionHolder<SocietyPermission> SOCIETY = new PowerlessPermissionHolder<>();
    public static final PowerlessPermissionHolder<ClaimPermission> CLAIM = new PowerlessPermissionHolder<>();

    @Override
    public boolean hasPermission(T permission) {
        return false;
    }

    @Override
    public boolean setPermission(T permission, PermissionState newState, Cause cause) {
        return false;
    }

    @Override
    public PermissionState getPermission(T permission) {
        return PermissionState.FALSE;
    }
}
