package com.github.thecrazyphoenix.societies.permission;

import com.github.thecrazyphoenix.societies.api.permission.PermissionHolder;
import com.github.thecrazyphoenix.societies.api.permission.PermissionState;
import com.github.thecrazyphoenix.societies.api.society.Society;
import com.github.thecrazyphoenix.societies.society.SocietyElementImpl;

import java.util.Map;

public class PermissionHolderImpl<T extends Enum<T>> extends SocietyElementImpl implements PermissionHolder<T> {
    private PermissionHolder<T> parent;
    private Map<T, PermissionState> permissions;

    public PermissionHolderImpl(Society society, PermissionHolder<T> parent) {
        super(society);
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
    public void setPermission(T permission, PermissionState newState) {
        permissions.put(permission, newState);
    }
}
