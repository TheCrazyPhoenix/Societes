package com.github.thecrazyphoenix.societies.permission;

import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
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
