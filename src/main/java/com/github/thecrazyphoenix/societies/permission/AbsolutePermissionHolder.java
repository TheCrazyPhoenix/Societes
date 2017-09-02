package com.github.thecrazyphoenix.societies.permission;

import com.github.thecrazyphoenix.societies.api.permission.ClaimPermission;
import com.github.thecrazyphoenix.societies.api.permission.MemberPermission;
import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.permission.SocietyPermission;
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
}
