package io.github.thecrazyphoenix.societies.permission;

import io.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import io.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import io.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import io.github.thecrazyphoenix.societies.api.permission.PermissionState;
import io.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
import org.spongepowered.api.event.cause.Cause;

public class AbsolutePermissionHolder<T extends Enum<T>> implements PermissionHolder<T> {
    public static final AbsolutePermissionHolder<MemberPermission> MEMBER = new AbsolutePermissionHolder<>();
    public static final AbsolutePermissionHolder<SocietyPermission> SOCIETY = new AbsolutePermissionHolder<>();
    public static final AbsolutePermissionHolder<ClaimPermission> CLAIM = new AbsolutePermissionHolder<>();

    @Override
    public boolean hasPermission(T permission) {
        return true;
    }

    @Override
    public boolean setPermission(T permission, PermissionState newState, Cause cause) {
        return false;
    }

    @Override
    public PermissionState getPermission(T permission) {
        return PermissionState.TRUE;
    }
}
